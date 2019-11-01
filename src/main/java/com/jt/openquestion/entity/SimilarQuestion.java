package com.jt.openquestion.entity;

public class SimilarQuestion {
    /**
     * 开放标签库题目Id
     */
    private long openQuestionId;

    /**
     * 来源题目Id
     */
    private String sourceQuestionId;

    /**
     * 来源题库平台
     */
    private int sourcePlatform;

    /**
     * 题目来源平台可信度权重
     */
    private double platformWeight;

    /**
     * 用于计算题目可信度的题目知识点Id集合，已转换成MOTK知识点Id
     */
    private int[] calculateKnowledgePointLabels;

    /**
     * 用于计算题目可信度的题目次要知识点Id集合，已转换成MOTK知识点Id
     */
    private int[] calculateSecondaryKnowledgePointLabels;

    /**
     * 与目标题目的相似度
     */
    private double similarScore;

    /**
     * 用于计算题目可信度的题目难度等级
     */
    private int calculateQuestionLevel;

    public long getOpenQuestionId() {
        return openQuestionId;
    }

    public void setOpenQuestionId(long openQuestionId) {
        this.openQuestionId = openQuestionId;
    }

    public String getSourceQuestionId() {
        return sourceQuestionId;
    }

    public void setSourceQuestionId(String sourceQuestionId) {
        this.sourceQuestionId = sourceQuestionId;
    }

    public int getSourcePlatform() {
        return sourcePlatform;
    }

    public void setSourcePlatform(int sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }

    public double getPlatformWeight() {
        return platformWeight;
    }

    public void setPlatformWeight(double platformWeight) {
        this.platformWeight = platformWeight;
    }

    public int[] getCalculateKnowledgePointLabels() {
        return calculateKnowledgePointLabels;
    }

    public void setCalculateKnowledgePointLabels(int[] calculateKnowledgePointLabels) {
        this.calculateKnowledgePointLabels = calculateKnowledgePointLabels;
    }

    public int[] getCalculateSecondaryKnowledgePointLabels() {
        return calculateSecondaryKnowledgePointLabels;
    }

    public void setCalculateSecondaryKnowledgePointLabels(int[] calculateSecondaryKnowledgePointLabels) {
        this.calculateSecondaryKnowledgePointLabels = calculateSecondaryKnowledgePointLabels;
    }

    public double getSimilarScore() {
        return similarScore;
    }

    public void setSimilarScore(double similarScore) {
        this.similarScore = similarScore;
    }

    public int getCalculateQuestionLevel() {
        return calculateQuestionLevel;
    }

    public void setCalculateQuestionLevel(int calculateQuestionLevel) {
        this.calculateQuestionLevel = calculateQuestionLevel;
    }
}
