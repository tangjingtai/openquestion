package com.jt.openquestion.entity;

public class OpenQuestionExtension {
    private long openQuestionId ;
    private String questionAnswer ;
    private String questionAnalysis ;
    private int originalQuestionTypeId;
    /**
     * 对应MOTK题型Id
     */
    private int questionTypeId ;

    public long getOpenQuestionId() {
        return openQuestionId;
    }

    public void setOpenQuestionId(long openQuestionId) {
        this.openQuestionId = openQuestionId;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getQuestionAnalysis() {
        return questionAnalysis;
    }

    public void setQuestionAnalysis(String questionAnalysis) {
        this.questionAnalysis = questionAnalysis;
    }

    public int getOriginalQuestionTypeId() {
        return originalQuestionTypeId;
    }

    public void setOriginalQuestionTypeId(int originalQuestionTypeId) {
        this.originalQuestionTypeId = originalQuestionTypeId;
    }

    public int getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(int questionTypeId) {
        this.questionTypeId = questionTypeId;
    }
}
