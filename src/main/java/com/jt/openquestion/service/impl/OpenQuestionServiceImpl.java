package com.jt.openquestion.service.impl;

import com.jt.openquestion.algorithm.ContentComparator;
import com.jt.openquestion.algorithm.WordSegmenter;
import com.jt.openquestion.entity.*;
import com.jt.openquestion.entity.request.OpenQuestionSearchRequest;
import com.jt.openquestion.enums.OpenQuestionUseTypeEnum;
import com.jt.openquestion.enums.SimilarityComparisonEnum;
import com.jt.openquestion.mapper.ai.OpenQuestionLabelMapper;
import com.jt.openquestion.mapper.ai.OpenQuestionMapper;
import com.jt.openquestion.service.OpenQuestionSearchService;
import com.jt.openquestion.service.OpenQuestionService;
import com.jt.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Open题库题目服务实现
 */
@Service
public class OpenQuestionServiceImpl implements OpenQuestionService {
    @Autowired
    private OpenQuestionMapper openQuestionMapper;
    @Autowired
    private OpenQuestionLabelMapper labelMapper;
    @Autowired
    private WordSegmenter wordSegmenter;
    @Autowired
    private OpenQuestionSearchService searchService;
    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private ContentComparator contentComparator;
    @Autowired
    private RedisUtil<OpenQuestion> redisUtil;

    private final Object LOCK = new Object();

    /**
     * 不同平台题目的用途类型
     */
    private static Map<Integer, Integer> platformQuestionUseType;

    /**
     * 保存一道题目
     *
     * @param openQuestion 题目对象
     * @return 新保存的题目Id
     */
    public Long saveOpenQuestion(OpenQuestion openQuestion) {
        openQuestionMapper.saveOpenQuestion(openQuestion);
        return openQuestion.getOpenQuestionId();
    }

    /**
     * 根据OpenQuestion Id获取题目对象
     *
     * @param openQuestionId 题目id
     * @return 题目对象
     */
    public OpenQuestion getByOpenQuestionId(long openQuestionId) {
        String key = "OpenQuestion_" + openQuestionId;
        OpenQuestion cached = redisUtil.get(key);
        if (cached != null) {
            return cached;
        }

        List<OpenQuestion> openQuestions = getByOpenQuestionIds(new long[]{openQuestionId});
        if (openQuestions == null || openQuestions.size() == 0) {
            return null;
        }
        OpenQuestion openQuestion = openQuestions.get(0);
        redisUtil.set(key, openQuestion, 10 * 60 * 60L);

        return openQuestion;
    }

    /**
     * 根据OpenQuestion Id集合获取题目对象集合
     *
     * @param openQuestionIds OpenQuestion Id集合
     * @return 题目对象集合
     */
    public List<OpenQuestion> getByOpenQuestionIds(long[] openQuestionIds) {
        List<OpenQuestion> openQuestions = openQuestionMapper.getByOpenQuestionIds(openQuestionIds);
        if (openQuestions == null || openQuestions.size() == 0) {
            return null;
        }
        List<OpenQuestionLabel> questionLabels = labelMapper.getQuestionLabelByIds(openQuestionIds);
        HashMap<Long, OpenQuestion> openQuestionHashMap = new HashMap<>();
        for (OpenQuestion question : openQuestions) {
            openQuestionHashMap.put(question.getOpenQuestionId(), question);
            question.setOpenQuestionLabels(new ArrayList<>());
        }
        for (OpenQuestionLabel label : questionLabels) {
            openQuestionHashMap.get(label.getOpenQuestionId()).getOpenQuestionLabels().add(label);
        }
        return openQuestions;
    }

    /**
     * 根据题目内容搜索相似题
     *
     * @param questionContent          用于搜索的题目内容
     * @param courseId                 题目内容所属于学科Id
     * @param similarScoreThreshold    相似度得分阈值，即相似度超过该值的题目才返回
     *                                 值范围(0.0,1.0]
     * @param similarityComparisonType 相似度比较的方式
     * @return 相似的题目
     */
    @Override
    public List<OpenQuestion> getSimilarOpenQuestions(String questionContent, int courseId,
                                                      double similarScoreThreshold, SimilarityComparisonEnum similarityComparisonType) throws IOException {
        OpenQuestionSearchRequest searchRequest = new OpenQuestionSearchRequest();
        searchRequest.setCourseId(courseId);
        searchRequest.setKeyword(questionContent);
        ArrayList<Integer> sourcePlatforms = new ArrayList<>();
        Map<Integer, Integer> platformUseTypes = getPlatformQuestionUseTypes();
        for (Map.Entry<Integer, Integer> entry : platformUseTypes.entrySet()) {
            if ((entry.getValue() & OpenQuestionUseTypeEnum.KnowledgePointLabel.getValue()) > 0) {
                sourcePlatforms.add(entry.getKey());
            }
        }
        searchRequest.setSourcePlatforms(sourcePlatforms.stream().mapToInt(Integer::valueOf).toArray());
        List<LiteOpenQuestion> liteOpenQuestions = this.searchService.searchQuestionDetails(searchRequest);
        if (liteOpenQuestions == null || liteOpenQuestions.size() == 0) {
            return null;
        }
        ArrayList<Long> similarOpenQuestionIds = new ArrayList<>(liteOpenQuestions.size());
        List<KeywordRecord> keywords = wordSegmenter.seg(questionContent);
        for (LiteOpenQuestion liteOpenQuestion : liteOpenQuestions) {
            List<KeywordRecord> compareKeywords = wordSegmenter.seg(liteOpenQuestion.getContent());
            double similarScore = contentComparator.compareSimilarityPercentage(keywords, compareKeywords, similarityComparisonType.getValue());
            if (similarScore >= similarScoreThreshold) {
                similarOpenQuestionIds.add(liteOpenQuestion.getOpenQuestionId());
            }
        }
        if (similarOpenQuestionIds.size() == 0)
            return null;
        return getByOpenQuestionIds(similarOpenQuestionIds.stream().mapToLong(Long::valueOf).toArray());
    }

    /**
     * 获取题目平台的题目用途类型
     *
     * @return 各题库来源平台对应的题目用途
     */
    private Map<Integer, Integer> getPlatformQuestionUseTypes() {
        if (platformQuestionUseType == null) {
            synchronized (LOCK) {
                if (platformQuestionUseType == null) {
                    String platformQuestionUseTypeStr = systemConfig.getPlatformQuestionUseType();
                    platformQuestionUseType = new HashMap<Integer, Integer>();
                    if (platformQuestionUseTypeStr == null || platformQuestionUseTypeStr.length() == 0) {
                        return platformQuestionUseType;
                    }
                    String[] platforms = platformQuestionUseTypeStr.split("[,;]");
                    for (String platform : platforms) {
                        String[] items = platform.split("-");
                        if (items.length == 2) {
                            platformQuestionUseType.put(Integer.parseInt(items[0]), Integer.parseInt(items[1]));
                        }
                    }
                }
            }
        }
        return platformQuestionUseType;
    }
}
