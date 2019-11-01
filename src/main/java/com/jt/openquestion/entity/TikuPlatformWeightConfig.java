package com.jt.openquestion.entity;

import com.jt.openquestion.enums.TikuPlatformEnum;

import java.util.Map;

public class TikuPlatformWeightConfig {
    /**
     * 平台枚举
     */
    private TikuPlatformEnum platform ;

    /**
     * 平台的整体可信度，如果未配置平台下学科的权重，则使用该值
     */
    private double platformWeight ;

    /**
     * 题库平台指定学科的题目标签可信度权重配置
     * <p>
     *     key: CourseId, value: CoursePlatformWeight
     * </p>
     */
    private Map<Integer, Double> coursePlatformWeights ;

    public TikuPlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(TikuPlatformEnum platform) {
        this.platform = platform;
    }

    public double getPlatformWeight() {
        return platformWeight;
    }

    public void setPlatformWeight(double platformWeight) {
        this.platformWeight = platformWeight;
    }

    public Map<Integer, Double> getCoursePlatformWeights() {
        return coursePlatformWeights;
    }

    public void setCoursePlatformWeights(Map<Integer, Double> coursePlatformWeights) {
        this.coursePlatformWeights = coursePlatformWeights;
    }
}
