package com.jt.openquestion.entity.request;

public class OpenQuestionSearchRequest {

    /**
     * 来源平台ID
     */
    private int[] sourcePlatforms;

    /**
     *  学科ID
     */
    private int courseId;

    /**
     * 搜索关键字
     */
    private String keyword;

    /**
     * 是否可用于打知识点标签
     */
    private boolean canKnowledgePointLabel;

    /**
     * 是否可用于打题目难度标签
     */
    private boolean canQuestionLevelLabel;

    /**
     * ES中查询的最低得分限制
     */
    private double minScore = 0.3;

    /**
     * 从ES中查询的数据条数
     */
    private int numberResults = 40;

    public int[] getSourcePlatforms() {
        return sourcePlatforms;
    }

    public void setSourcePlatforms(int[] sourcePlatforms) {
        this.sourcePlatforms = sourcePlatforms;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean getCanKnowledgePointLabel() {
        return canKnowledgePointLabel;
    }

    public void setCanKnowledgePointLabel(boolean canKnowledgePointLabel) {
        this.canKnowledgePointLabel = canKnowledgePointLabel;
    }

    public boolean getCanQuestionLevelLabel() {
        return canQuestionLevelLabel;
    }

    public void setCanQuestionLevelLabel(boolean canQuestionLevelLabel) {
        this.canQuestionLevelLabel = canQuestionLevelLabel;
    }

    public double getMinScore() {
        return minScore;
    }

    public void setMinScore(double minScore) {
        this.minScore = minScore;
    }

    public int getNumberResults() {
        return numberResults;
    }

    public void setNumberResults(int numberResults) {
        this.numberResults = numberResults;
    }
}
