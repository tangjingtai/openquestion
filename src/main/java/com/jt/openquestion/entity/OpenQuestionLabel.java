package com.jt.openquestion.entity;

public class OpenQuestionLabel {
    public OpenQuestionLabel(){
        this(0L,0,0,0.0);
    }

    public OpenQuestionLabel(Long openQuestionId, Integer labelId, Integer labelType, Double labelScore) {
        this.openQuestionId = openQuestionId;
        this.labelId = labelId;
        this.labelType = labelType;
        this.labelScore = labelScore;
    }

    private Long openQuestionId;
    private Integer labelId;
    private Integer labelType;
    private Double labelScore;

    public Long getOpenQuestionId() {
        return openQuestionId;
    }

    public void setOpenQuestionId(Long openQuestionId) {
        this.openQuestionId = openQuestionId;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public Integer getLabelType() {
        return labelType;
    }

    public void setLabelType(Integer labelType) {
        this.labelType = labelType;
    }

    public Double getLabelScore() {
        return labelScore;
    }

    public void setLabelScore(Double labelScore) {
        this.labelScore = labelScore;
    }
}
