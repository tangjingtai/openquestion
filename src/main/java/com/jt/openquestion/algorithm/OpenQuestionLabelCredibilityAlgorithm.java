package com.jt.openquestion.algorithm;

import com.jt.openquestion.entity.*;
import com.jt.openquestion.enums.OpenQuestionLabelTypeEnum;
import com.jt.openquestion.enums.TikuPlatformEnum;
import java.util.*;
import java.util.stream.Collectors;
import com.jt.openquestion.utils.CastArrayUtil;
import com.jt.openquestion.utils.CollectionExtension;
import com.jt.openquestion.utils.StringExtensions;
import com.jt.openquestion.utils.Tuple;

public class OpenQuestionLabelCredibilityAlgorithm {

    /**
     * 第三方题库题目标签权重配置
     */
    private static List<TikuPlatformWeightConfig> platformLabelWeightConfig;

    private final Double PLATFORM_BASE_WEIGHT = 0.5;

    /**
     * 次要知识点相对主要知识点的权重因子，对次要知识点进行降权
     */
    private final Double SECONDARY_KNOWLEDGEPOINT_WEIGHT_FACTOR = 0.65;

    private final static Object lock = new Object();

    /**
     * 来源题库平台的知识点与MOTK知识点对应关系
     */
    private final Map<Integer, Map<ThirdPartyKnowledgePointIdTuple, Integer>> sourcePlatformKnowledgePointRelations
            = new HashMap<>();

    /**
     * motk知识点
     */
    private static List<KnowledgePoint> motkKnowledgePoints;

    /**
     * 计算题目标签整体可信度得分以及得分最高的N个知识点和题目难度
     *
     * @param openQuestion       开发标签库题目对象
     * @param duplicateQuestions 第三方平台相似题相似度超过重复题目阈值的Id集合
     * @param topKnowledges      返回得分最高的几个知识点
     * @param topQuestionLevels  返回得分最高的几个题目难度
     * @return 完成标签整体可信度得分计算、知识点赋值、题目难度等级赋值后的开放标签题目对象
     */
    private OpenQuestion calculateOpenQuestionLabelCredibility(OpenQuestion openQuestion, List<OpenQuestionSimilarity> duplicateQuestions,
                                                               int topKnowledges, int topQuestionLevels) {
        if (platformLabelWeightConfig == null) {
            buildSourcePlatformLabelWeight();
        }
        List<SimilarQuestion> calculateQuestions = getCalculateQuestions(openQuestion, duplicateQuestions);

        Tuple.Tuple2<List<OpenQuestionLabel>, Double> knowledgeScore = calculateKnowledgeCredibility(calculateQuestions, topKnowledges);
        Tuple.Tuple2<List<OpenQuestionLabel>, Double> questionLevelScore = calculateQuestionLevelCredibility(calculateQuestions, topQuestionLevels);
        double finalCredibilityScore = sigmoid(knowledgeScore.getItem2() * 0.8 + questionLevelScore.getItem2() * 0.2);

        ArrayList<OpenQuestionLabel> openQuestionLabels = new ArrayList<>();
        openQuestionLabels.addAll(knowledgeScore.getItem1());
        openQuestionLabels.addAll(questionLevelScore.getItem1());

        openQuestion.setOpenQuestionLabels(openQuestionLabels);
        openQuestion.setCredibility(finalCredibilityScore);

        return openQuestion;
    }

    /**
     * 计算各个平台重复题目中所有知识点可信度得分，并返回得分最高的N个知识点与题目知识点综合得分
     * <p>
     * 次要知识点计算得分时，平台权重需要降权，乘以次要知识点权重因子(SecondaryKnowledgePointWeightFactor)
     * </p>
     *
     * @param duplicateQuestions 所有重复题目集合
     * @param topKnowledges      返回得分最高的几个知识点
     * @return 得分最高的N个知识点：题目知识点综合得分
     */
    private Tuple.Tuple2<List<OpenQuestionLabel>, Double> calculateKnowledgeCredibility(List<SimilarQuestion> duplicateQuestions, int topKnowledges) {
        List<Integer> knowledgeIds = new ArrayList<>();
        duplicateQuestions.forEach(x -> {
            knowledgeIds.addAll(Arrays.asList(CastArrayUtil.toWrap(x.getCalculateKnowledgePointLabels())));
            knowledgeIds.addAll(Arrays.asList(CastArrayUtil.toWrap(x.getCalculateSecondaryKnowledgePointLabels())));
        });

        List<Integer> distinctKnowledgeIds = knowledgeIds.stream().distinct().collect(Collectors.toList());
        List<Tuple.Tuple2<Integer, Double>> knowledgeScores = new ArrayList<>();
        for (Integer knowledgeId : distinctKnowledgeIds) {
            ArrayList<CalculateCredibilityEntity> tempQuestions = new ArrayList<>();
            for (SimilarQuestion duplicateQuestion : duplicateQuestions) {
                for (int id : duplicateQuestion.getCalculateKnowledgePointLabels()) {
                    if (id == knowledgeId) {
                        tempQuestions.add(new CalculateCredibilityEntity(duplicateQuestion.getSourcePlatform(), duplicateQuestion.getPlatformWeight(), duplicateQuestion.getSimilarScore()));
                        break;
                    }
                }
                for (int id : duplicateQuestion.getCalculateSecondaryKnowledgePointLabels()) {
                    if (id == knowledgeId) {
                        tempQuestions.add(new CalculateCredibilityEntity(duplicateQuestion.getSourcePlatform(),
                                duplicateQuestion.getPlatformWeight() * SECONDARY_KNOWLEDGEPOINT_WEIGHT_FACTOR, duplicateQuestion.getSimilarScore()));
                        break;
                    }
                }
            }
            double score = calculateLabelCredibility(tempQuestions);
            knowledgeScores.add(new Tuple.Tuple2<>(knowledgeId, score));
        }

        knowledgeScores = removeParentKnowledgePointScore(knowledgeScores);
        knowledgeScores.sort((x, y) -> Double.compare(y.getItem2(), x.getItem2()));
        List<OpenQuestionLabel> finalKnowledges = knowledgeScores.subList(0, Math.min(knowledgeScores.size(), topKnowledges)).stream()
                .map(x -> new OpenQuestionLabel(0L, x.getItem1(), OpenQuestionLabelTypeEnum.KnowledgePoint.getValue(), x.getItem2()))
                .collect(Collectors.toList());
        double finalScore = knowledgeScores.size() > 0 ? CollectionExtension.max(knowledgeScores, Tuple.Tuple2::getItem2) : 0.0;

        return new Tuple.Tuple2<>(finalKnowledges, finalScore);
    }

    /**
     * 计算各个平台重复题目中所有题目难度可信度得分，并返回得分最高的题目难度以及题目难度的综合得分
     *
     * @param duplicateQuestions 重复题目集合
     * @param topQuestionLevels  返回得分最高的几个题目难度
     * @return 得分最高的题目难度等级：题目难度的综合得分
     */
    private Tuple.Tuple2<List<OpenQuestionLabel>, Double> calculateQuestionLevelCredibility(List<SimilarQuestion> duplicateQuestions,
                                                                                            int topQuestionLevels) {
        List<Integer> distinctQuestionLevels = duplicateQuestions.stream().map(SimilarQuestion::getCalculateQuestionLevel).distinct().collect(Collectors.toList());
        ArrayList<Tuple.Tuple2<Integer, Double>> questionLevelScores = new ArrayList<>();
        for (int level : distinctQuestionLevels) {
            List<CalculateCredibilityEntity> tempQuestions = duplicateQuestions.stream().filter(x -> x.getCalculateQuestionLevel() == level)
                    .map(x -> new CalculateCredibilityEntity(x.getSourcePlatform(), x.getPlatformWeight(), x.getSimilarScore()))
                    .collect(Collectors.toList());
            double score = calculateLabelCredibility(tempQuestions);
            questionLevelScores.add(new Tuple.Tuple2<>(level, score));
        }
        List<OpenQuestionLabel> finalQuestionLevels = questionLevelScores
                .stream().sorted((x, y) -> Double.compare(y.getItem2(), x.getItem2()))
                .map(x -> new OpenQuestionLabel(0L, x.getItem1(), OpenQuestionLabelTypeEnum.QuestionLevel.getValue(), x.getItem2()))
                .collect(Collectors.toList())
                .subList(0, Math.min(questionLevelScores.size(), topQuestionLevels));
        double finalScore = questionLevelScores.size() > 0 ? CollectionExtension.max(questionLevelScores, Tuple.Tuple2::getItem2) : 0.0;

        return new Tuple.Tuple2<List<OpenQuestionLabel>, Double>(finalQuestionLevels, finalScore);
    }

    /**
     * 剔除知识点集合中存在父子关系的父知识点，将父知识点的得分传递到子知识点得分<br />
     * 存在父子关系（包括父知识点的父知识点）的知识点计算如下：
     * <p>
     * 1. 从父知识点开始向子知识点计算得分，公式：score = Math.Max(parentScore, score) + Math.Min(parentScore, score) * 0.1;
     * 2. 从结果中排除存在父子关系的父知识点（既同时存在父、子知识点得分时，保留子知识点的得分）
     * </p>
     *
     * @param knowledgeScores 知识点标签集合
     * @return 排除了父知识点后的标签集合
     */
    private List<Tuple.Tuple2<Integer, Double>> removeParentKnowledgePointScore(List<Tuple.Tuple2<Integer, Double>> knowledgeScores) {
        List<Integer> knowledgeIds = knowledgeScores.stream().map(Tuple.Tuple2::getItem1).collect(Collectors.toList());
        ArrayList<Tuple.Tuple2<Integer, Double>> result = new ArrayList<>();
        ArrayList<Integer> parentKnowledges = new ArrayList<>();
        for (Tuple.Tuple2<Integer, Double> knowledgeScore : knowledgeScores) {
            List<Integer> parentKnowledgeIdChain = getKnowledgePointParentChain(knowledgeScore.getItem1());
            List<Integer> intersectKnowledges = new ArrayList<>(knowledgeIds.size());
            Collections.copy(intersectKnowledges, knowledgeIds);
            intersectKnowledges.retainAll(parentKnowledgeIdChain);
            double score = knowledgeScore.getItem2();
            if (intersectKnowledges.size() > 0) {
                score = 0.0;
                parentKnowledges.addAll(intersectKnowledges);
                for (int i = intersectKnowledges.size() - 1; i >= 0; i--) {
                    final int index = i;
                    Optional<Tuple.Tuple2<Integer, Double>> parentKnowledgeScore = CollectionExtension.firstOrDefault(knowledgeScores, x -> x.getItem1() == intersectKnowledges.get(index));
                    score = Math.max(score, parentKnowledgeScore.get().getItem2()) + Math.min(score, parentKnowledgeScore.get().getItem2()) * 0.1;
                }
                score = Math.max(score, knowledgeScore.getItem2()) + Math.min(score, knowledgeScore.getItem2()) * 0.1;
            }
            result.add(new Tuple.Tuple2<Integer, Double>(knowledgeScore.getItem1(), score));
        }
        return result.stream().filter(x -> !parentKnowledges.contains(x.getItem1())).collect(Collectors.toList());
    }

    /**
     * 获取知识点的父知识点链，返回[父知识点Id, 父知识点的父知识点Id, .....]
     *
     * @param knowledgeId 当前知识点
     * @return 父知识点、父知识点的父知识点....组成的集合
     */
    private List<Integer> getKnowledgePointParentChain(int knowledgeId) {
        ArrayList<Integer> result = new ArrayList<>();
        if (motkKnowledgePoints == null) {
            // TODO: 从数据库中查询魔题库知识点列表
//            motkKnowledgePoints = _knowledgePointDal.GetMotkKnowledgePoints();
            motkKnowledgePoints = new ArrayList<>();
        }
        Optional<KnowledgePoint> knowledge = CollectionExtension.firstOrDefault(motkKnowledgePoints, x -> x.getKnowledgePointId() == knowledgeId);
        if (!knowledge.isPresent() || knowledge.get().getParentKnowledgePointId() == 0) {
            return new ArrayList<>();
        }
        int parentKnowledgeId = knowledge.get().getParentKnowledgePointId();
        while (true) {
            final int pId = parentKnowledgeId;
            result.add(parentKnowledgeId);
            Optional<KnowledgePoint> parentKnowledge = CollectionExtension.firstOrDefault(motkKnowledgePoints, x -> x.getKnowledgePointId() == pId);
            if (!parentKnowledge.isPresent() || parentKnowledge.get().getParentKnowledgePointId() == 0) {
                break;
            }
            if (result.contains(parentKnowledge.get().getParentKnowledgePointId())) {
                break;
            }
            parentKnowledgeId = parentKnowledge.get().getParentKnowledgePointId();
        }
        return result;
    }

    /**
     * 计算重复题目指定一个标签的可信度得分
     *
     * @param sameLabelQuestions 包含同一标签的重复题目
     * @return 指定一个表情的可信度得分
     */
    public double calculateLabelCredibility(List<CalculateCredibilityEntity> sameLabelQuestions) {
        int questionCount = sameLabelQuestions.size();
        long platforms = sameLabelQuestions.stream().map(CalculateCredibilityEntity::getSourcePlatform).distinct().count();

        double wMin = CollectionExtension.min(sameLabelQuestions, CalculateCredibilityEntity::getPlatformWeight);
        double sAvg = CollectionExtension.avg(sameLabelQuestions, CalculateCredibilityEntity::getSimilarScore);
        double sMin = CollectionExtension.min(sameLabelQuestions, CalculateCredibilityEntity::getSimilarScore);

        if (platforms == 1) {
            wMin = 0.5;
        }
        double score = 0;
        for (CalculateCredibilityEntity t : sameLabelQuestions) {
            score += sigmoid(2 * t.getPlatformWeight() - wMin) * sigmoid((2 * t.getSimilarScore() - sAvg) / sAvg);
        }
        score = platforms * score / Math.pow(questionCount, 0.75);
        return score;
    }

    /**
     * sigmoid数学函数
     *
     * @param v 输入
     * @return 函数值
     */
    private double sigmoid(double v) {
        return 1 / (1 + Math.exp(-1 * v));
    }

    /**
     * 根据重复题获取用于计算题目标签的题目集合
     * <p>
     * 如果重复题中没有包含当前处理的题目，则将当前处理题目添加到重复题中，重复题相似度为1.0
     * </p>
     *
     * @param openQuestion       当前处理的开发标签题目对象
     * @param duplicateQuestions 与当前处理的开发标签题目重复的题目Id集合
     * @return 包含当前题目的重复题目集合
     */
    private List<SimilarQuestion> getCalculateQuestions(OpenQuestion openQuestion, List<OpenQuestionSimilarity> duplicateQuestions) {
        ArrayList<SimilarQuestion> result = new ArrayList<>(duplicateQuestions.size() + 1);
        boolean hasCurOpenQuestion = false;
        for (OpenQuestionSimilarity item : duplicateQuestions) {
            result.add(convertToSimilarQuestion(item.getOpenQuestion(), item.getSimilarScore()));
            if (item.getOpenQuestion().getOpenQuestionId().equals(openQuestion.getOpenQuestionId())
                    || (item.getOpenQuestion().getSourcePlatform().equals(openQuestion.getSourcePlatform())
                    && item.getOpenQuestion().getSourceQuestionId().equals(openQuestion.getSourceQuestionId()))) {
                hasCurOpenQuestion = true;
            }
        }

        if (!hasCurOpenQuestion) {
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
        similarQuestion.setPlatformWeight(getPlatformCourseWeight(openQuestion.getSourcePlatform(), openQuestion.getCourseId()));
        similarQuestion.setSimilarScore(similar);
        similarQuestion.setCalculateKnowledgePointLabels(getMotkKnowledgePoints(openQuestion.getOriginalKnowledgePointLabels(),
                openQuestion.getSourcePlatform(), openQuestion.getCourseId()));
//        similarQuestion.setCalculateSecondaryKnowledgePointLabels(getMotkKnowledgePoints(openQuestion.OriginalSecondaryKnowledgePointLabels, openQuestion.SourcePlatform, openQuestion.CourseId),
        similarQuestion.setCalculateQuestionLevel(openQuestion.fetchCalculateQuestionLevel());
        return similarQuestion;
    }

    /**
     * 获取第三方题库平台的知识点Id对应的motk知识点Id
     *
     * @param thirdPartKnowledgePointIds 第三方题库平台的知识点Id集合，多个使用英文逗号分隔
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
            synchronized (lock) {
                if (!sourcePlatformKnowledgePointRelations.containsKey(sourcePlatform)) {
                    // var knowledgePoints = _knowledgePointDal.GetThirdPartyKnowledgePointByPlatform(sourcePlatform);
                    ArrayList<ThirdPartyKnowledgePoint> knowledgePoints = null;
                    // TODO: 从数据库中读取第三方知识点
                    if (knowledgePoints != null) {
                        knowledgePoints.sort(Comparator.comparingInt(ThirdPartyKnowledgePoint::getStatusFlag));
                        HashMap<ThirdPartyKnowledgePointIdTuple, Integer> map = new HashMap<>(knowledgePoints.size());
                        for (ThirdPartyKnowledgePoint knowledgePoint : knowledgePoints) {
                            map.put(new ThirdPartyKnowledgePointIdTuple(knowledgePoint.getThirdPartyKnowledgePointId(), knowledgePoint.getSourcePlatform(), knowledgePoint.getCourseID()),
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
     * 转换成MOTK知识点
     *
     * @param thirdPartKnowledgePointIds 第三方题库知识点
     * @param sourcePlatform             第三方平台枚举值
     * @param courseId                   学科Id
     * @return MOTK知识点集合
     */
    private int[] convertToMotkKnowledgePoints(String[] thirdPartKnowledgePointIds, int sourcePlatform, int courseId) {
        List<Integer> result = new ArrayList<>();
        for (String knowledgePointId : thirdPartKnowledgePointIds) {
            ThirdPartyKnowledgePointIdTuple knowledgePointTuple = new ThirdPartyKnowledgePointIdTuple(knowledgePointId, sourcePlatform, courseId);
            if (sourcePlatformKnowledgePointRelations.get(sourcePlatform).containsKey(knowledgePointTuple)) {
                result.add(sourcePlatformKnowledgePointRelations.get(sourcePlatform).get(knowledgePointTuple));
            }
        }
        result = result.stream().filter(x -> x > 0).collect(Collectors.toList());
        int[] array = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
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

    public double getPlatformCourseWeight(int platform, int courseId) {
        if (platformLabelWeightConfig == null) {
            buildSourcePlatformLabelWeight();
        }
        Optional<TikuPlatformWeightConfig> config = CollectionExtension.firstOrDefault(platformLabelWeightConfig, x->x.getPlatform().getValue().equals(platform));
        if (!config.isPresent() || config.get().getPlatformWeight() <= 0.0) {
            return PLATFORM_BASE_WEIGHT;
        }
        if (config.get().getCoursePlatformWeights() != null && config.get().getCoursePlatformWeights().containsKey(courseId)) {
            return config.get().getCoursePlatformWeights().get(courseId);
        }
        return config.get().getPlatformWeight();
    }
}
