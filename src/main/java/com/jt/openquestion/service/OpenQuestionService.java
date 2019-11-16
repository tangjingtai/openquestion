package com.jt.openquestion.service;

import com.jt.openquestion.entity.OpenQuestion;
import com.jt.openquestion.entity.OpenQuestionLabel;
import com.jt.openquestion.enums.SimilarityComparisonEnum;
import com.jt.openquestion.mapper.ai.OpenQuestionLabelMapper;
import com.jt.openquestion.mapper.ai.OpenQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 提供Open题库题目服务
 */
public interface OpenQuestionService {

    /**
     * 保存一道题目
     * @param openQuestion 题目对象
     * @return 新保存的题目Id
     */
    Long saveOpenQuestion(OpenQuestion openQuestion);

    /**
     * 根据OpenQuestion Id获取题目对象
     * @param openQuestionId 题目id
     * @return 题目对象
     */
    OpenQuestion getByOpenQuestionId(long openQuestionId);

    /**
     * 根据OpenQuestion Id集合获取题目对象集合
     * @param openQuestionIds OpenQuestion Id集合
     * @return 题目对象集合
     */
    List<OpenQuestion> getByOpenQuestionIds(long[] openQuestionIds);

    /**
     *  根据题目内容搜索相似题
     * @param questionContent 用于搜索的题目内容
     * @param courseId 题目内容所属于学科Id
     * @param similarScoreThreshold 相似度得分阈值，即相似度超过该值的题目才返回
     *                                 值范围(0.0,1.0]
     * @param similarityComparisonType 相似度比较的方式
     * @return 相似的题目
     */
    List<OpenQuestion> getSimilarOpenQuestions(String questionContent, int courseId, double similarScoreThreshold,
                                               SimilarityComparisonEnum similarityComparisonType) throws IOException;
}
