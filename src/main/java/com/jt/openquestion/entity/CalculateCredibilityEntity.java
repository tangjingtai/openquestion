package com.jt.openquestion.entity;

public class CalculateCredibilityEntity {

    public CalculateCredibilityEntity(int sourcePlatform, double platformWeight, double similarScore) {
        this.sourcePlatform = sourcePlatform;
        this.platformWeight = platformWeight;
        this.similarScore = similarScore;
    }

    /**
     *  来源题库平台
     */
    private int sourcePlatform;

    /**
     * 题目来源平台可信度权重
     */
    private double platformWeight;

    /**
     * 与目标题目的相似度
     */
    private double similarScore;

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

    public double getSimilarScore() {
        return similarScore;
    }

    public void setSimilarScore(double similarScore) {
        this.similarScore = similarScore;
    }
}
