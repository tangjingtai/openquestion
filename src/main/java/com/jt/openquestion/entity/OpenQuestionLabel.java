package com.jt.openquestion.entity;

public class OpenQuestionLabel {
    private Long openQuestionId;
    private Integer LabelId;
    private Integer LabelType;
    private Double LabelScore;

    public Long getOpenQuestionId() {
        return openQuestionId;
    }

    public void setOpenQuestionId(Long openQuestionId) {
        this.openQuestionId = openQuestionId;
    }

    public Integer getLabelId() {
        return LabelId;
    }

    public void setLabelId(Integer labelId) {
        LabelId = labelId;
    }

    public Integer getLabelType() {
        return LabelType;
    }

    public void setLabelType(Integer labelType) {
        LabelType = labelType;
    }

    public Double getLabelScore() {
        return LabelScore;
    }

    public void setLabelScore(Double labelScore) {
        LabelScore = labelScore;
    }
}
