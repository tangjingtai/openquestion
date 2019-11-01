package com.jt.openquestion.algorithm;

import com.jt.openquestion.entity.*;
import com.jt.openquestion.enums.TikuPlatformEnum;

import java.util.*;
import java.util.stream.Collectors;

import com.jt.openquestion.utils.StringExtensions;

public class OpenQuestionLabelCredibilityAlgorithm {

    /**
     * 第三方题库题目标签权重配置
     */
    private static List<TikuPlatformWeightConfig> platformLabelWeightConfig;

    private final Double PLATFORM_BASE_WEIGHT = 0.5;

    private final static Object lock = new Object();

    /**
     * 来源题库平台的知识点与MOTK知识点对应关系
     */
    private final Map<Integer, Map<ThirdPartyKnowledgePointIdTuple, Integer>> sourcePlatformKnowledgePointRelations
            = new HashMap<>();

    /**
     * 计算题目标签整体可信度得分以及得分最高的N个知识点和题目难度
     *
     * @param openQuestion       开发标签库题目对象
     * @param duplicateQuestions 第三方平台相似题相似度超过重复题目阈值的Id集合
     * @param topKnowledges      返回得分最高的几个知识点
     * @param topQuestionLevels  返回得分最高的几个题目难度
     * @return 完成标签整体可信度得分计算、知识点赋值、题目难度等级赋值后的开放标签题目对象
     */
    private OpenQuestion CalculateOpenQuestionLabelCredibility(OpenQuestion openQuestion, List<OpenQuestionSimilarity> duplicateQuestions,
                                                               int topKnowledges, int topQuestionLevels) {
        if (platformLabelWeightConfig == null) {
            buildSourcePlatformLabelWeight();
        }
        List<SimilarQuestion> calculateQuestions = getCalculateQuestions(openQuestion, duplicateQuestions);

//        var knowledgeScore = CalculateKnowledgeCredibility(calculateQuestions, topKnowledges);
//        var questionLevelScore = CalculateQuestionLevelCredibility(calculateQuestions, topQuestionLevels);
//        var finalCredibilityScore = Sigmoid(knowledgeScore.Item2 * 0.8 + questionLevelScore.Item2 * 0.2);
//
//        var openQuestionLabels = new List<OpenQuestionLabel>();
//        openQuestionLabels.AddRange(knowledgeScore.Item1);
//        openQuestionLabels.AddRange(questionLevelScore.Item1);
//
//        openQuestion.OpenQuestionLabels = openQuestionLabels;
//        openQuestion.Credibility = finalCredibilityScore;

        return openQuestion;
    }

    /**
     * 根据重复题获取用于计算题目标签的题目集合
     * <para>
     * 如果重复题中没有包含当前处理的题目，则将当前处理题目添加到重复题中，重复题相似度为1.0
     * </para>
     *
     * @param openQuestion       当前处理的开发标签题目对象
     * @param duplicateQuestions 与当前处理的开发标签题目重复的题目Id集合
     * @return 包含当前题目的重复题目集合
     */
    private List<SimilarQuestion> getCalculateQuestions(OpenQuestion openQuestion, List<OpenQuestionSimilarity> duplicateQuestions) {
        ArrayList<SimilarQuestion> result = new ArrayList<>(duplicateQuestions.size() + 1);
        boolean hasCurOpenQuestion = false;
        for (OpenQuestionSimilarity item : duplicateQuestions) {
            result.add(convertToSimilarQuestion(item.getOpenQuestion(),item.getSimilarScore()));
            if(item.getOpenQuestion().getOpenQuestionId().equals(openQuestion.getOpenQuestionId())
                    || (item.getOpenQuestion().getSourcePlatform().equals(openQuestion.getSourcePlatform())
                            && item.getOpenQuestion().getSourceQuestionId().equals(openQuestion.getSourceQuestionId()))){
                hasCurOpenQuestion = true;
            }
        }

        if (!hasCurOpenQuestion)
        {
            result.add(convertToSimilarQuestion(openQuestion, 1.0));
        }

        return result;
    }

    /// <summary>
    /// 开放标签题目转换成相似题对象
    /// </summary>
    /// <param name="openQuestion">开放标签题目对象</param>
    /// <param name="similar">相似度</param>
    /// <returns>题目相似度对象</returns>
    private SimilarQuestion convertToSimilarQuestion(OpenQuestion openQuestion, double similar) {
        SimilarQuestion similarQuestion = new SimilarQuestion();
        similarQuestion.setOpenQuestionId(openQuestion.getOpenQuestionId());
        similarQuestion.setSourceQuestionId(openQuestion.getSourceQuestionId());
        similarQuestion.setSourcePlatform(openQuestion.getSourcePlatform());
        similarQuestion.setPlatformWeight(GetPlatformCourseWeight(openQuestion.getSourcePlatform(), openQuestion.getCourseId()));
        similarQuestion.setSimilarScore(similar);
        similarQuestion.setCalculateKnowledgePointLabels(getMotkKnowledgePoints(openQuestion.getOriginalKnowledgePointLabels(),
                openQuestion.getSourcePlatform(), openQuestion.getCourseId()));
//        similarQuestion.setCalculateSecondaryKnowledgePointLabels(getMotkKnowledgePoints(openQuestion.OriginalSecondaryKnowledgePointLabels, openQuestion.SourcePlatform, openQuestion.CourseId),
        similarQuestion.setCalculateQuestionLevel(openQuestion.getCalculateQuestionLevel());
        return similarQuestion;
    }

    /**
     * 获取第三方题库平台的知识点Id对应的motk知识点Id
     *
     * @param thirdPartKnowledgePointIds 第三方题库平台的知识点Id集合
     * @param sourcePlatform             来源平台
     * @param courseId                   学科Id
     * @return 第三方题库平台的知识点对应的motk知识点Id集合
     */
    public int[] getMotkKnowledgePoints(String thirdPartKnowledgePointIds, int sourcePlatform, int courseId) {
        if (sourcePlatform == TikuPlatformEnum.Motk.getValue() || sourcePlatform == TikuPlatformEnum.MotkTest.getValue()
                || sourcePlatform == TikuPlatformEnum.MotkUat.getValue()) {
            return StringExtensions.splitIntItems(thirdPartKnowledgePointIds);
        }
        String[] knowledgePointIds = thirdPartKnowledgePointIds.split(",");
        if (!sourcePlatformKnowledgePointRelations.containsKey(sourcePlatform)) {
            synchronized (this.lock)
            {
                if (!sourcePlatformKnowledgePointRelations.containsKey(sourcePlatform)) {
                    // var knowledgePoints = _knowledgePointDal.GetThirdPartyKnowledgePointByPlatform(sourcePlatform);
                    ArrayList<ThirdPartyKnowledgePoint> knowledgePoints = null;
                    // TODO: 读取第三方知识点
                    if (knowledgePoints != null) {
                        knowledgePoints.sort(Comparator.comparingInt(ThirdPartyKnowledgePoint::getStatusFlag));
                        HashMap<ThirdPartyKnowledgePointIdTuple, Integer> map = new HashMap<>(knowledgePoints.size());
                        for(ThirdPartyKnowledgePoint knowledgePoint : knowledgePoints){
                            map.put(new ThirdPartyKnowledgePointIdTuple(knowledgePoint.getThirdPartyKnowledgePointId(), knowledgePoint.getSourcePlatform(),knowledgePoint.getCourseID()),
                                    knowledgePoint.getMotkKnowledgePointId());
                        }

                        sourcePlatformKnowledgePointRelations.put(sourcePlatform, map);
                    }
                }
            }
        }
        if (!sourcePlatformKnowledgePointRelations.containsKey(sourcePlatform)
                || sourcePlatformKnowledgePointRelations.get(sourcePlatform) == null
                || sourcePlatformKnowledgePointRelations.get(sourcePlatform).size() == 0) {
            return new int[0];
        }
        return convertToMotkKnowledgePoints(knowledgePointIds, sourcePlatform, courseId);
    }


    /**
     *  转换成MOTK知识点
     * @param thirdPartKnowledgePointIds 第三方题库知识点
     * @param sourcePlatform 第三方平台枚举值
     * @param courseId 学科Id
     * @return MOTK知识点集合
     */
    private int[] convertToMotkKnowledgePoints(String[] thirdPartKnowledgePointIds, int sourcePlatform, int courseId)
    {
        List<Integer> result = new ArrayList<>();
        for (String knowledgePointId : thirdPartKnowledgePointIds)
        {
            ThirdPartyKnowledgePointIdTuple knowledgePointTuple = new ThirdPartyKnowledgePointIdTuple(knowledgePointId, sourcePlatform, courseId);
            if (sourcePlatformKnowledgePointRelations.get(sourcePlatform).containsKey(knowledgePointTuple))
            {
                result.add(sourcePlatformKnowledgePointRelations.get(sourcePlatform).get(knowledgePointTuple));
            }
        }
        result= result.stream().filter(x->x>0).collect(Collectors.toList());
        int[] array = new int[result.size()];
        for (int i =0 ;i<result.size();i++){
            array[i] = result.get(i);
        }
        return array;
    }

    private void buildSourcePlatformLabelWeight() {
        platformLabelWeightConfig = new ArrayList<>(2);
        TikuPlatformWeightConfig tikuPlatformWeightConfig = new TikuPlatformWeightConfig();
        tikuPlatformWeightConfig.setPlatform(TikuPlatformEnum.XueKe);
        tikuPlatformWeightConfig.setPlatformWeight(0.88);
        HashMap<Integer, Double> map = new HashMap<Integer, Double>() {
            {
                put(1, 0.75);
                put(2, 0.80);
            }
        };
        tikuPlatformWeightConfig.setCoursePlatformWeights(map);
    }

    public double GetPlatformCourseWeight(int platform, int courseId) {
        if (platformLabelWeightConfig == null) {
            buildSourcePlatformLabelWeight();
        }
        TikuPlatformWeightConfig config = null;
        for (TikuPlatformWeightConfig item : platformLabelWeightConfig) {
            if (config.getPlatform().getValue().equals(platform)) {
                config = item;
                break;
            }
        }
        if (config == null || config.getPlatformWeight() <= 0.0) {
            return PLATFORM_BASE_WEIGHT;
        }
        if (config.getCoursePlatformWeights() != null && config.getCoursePlatformWeights().containsKey(courseId)) {
            return config.getCoursePlatformWeights().get(courseId);
        }
        return config.getPlatformWeight();
    }
}
