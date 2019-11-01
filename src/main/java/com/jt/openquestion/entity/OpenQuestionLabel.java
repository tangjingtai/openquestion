package com.jt.openquestion.entity;

public class OpenQuestionLabel {
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
