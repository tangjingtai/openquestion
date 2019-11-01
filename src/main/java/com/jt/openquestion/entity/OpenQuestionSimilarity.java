package com.jt.openquestion.entity;

public class OpenQuestionSimilarity {
    private OpenQuestion openQuestion;
    private Double similarScore;

    public OpenQuestion getOpenQuestion() {
        return openQuestion;
    }

    public void setOpenQuestion(OpenQuestion openQuestion) {
        this.openQuestion = openQuestion;
    }

    public Double getSimilarScore() {
        return similarScore;
    }

    public void setSimilarScore(Double similarScore) {
        this.similarScore = similarScore;
    }
}
