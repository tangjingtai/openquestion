package com.jt.openquestion.controller.request;


public class SimilarOpenQuestionRequest {
    private String content;
    private Integer courseId;
    private Double similarScoreThreshold;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Double getSimilarScoreThreshold() {
        return similarScoreThreshold;
    }

    public void setSimilarScoreThreshold(Double similarScoreThreshold) {
        this.similarScoreThreshold = similarScoreThreshold;
    }
}
