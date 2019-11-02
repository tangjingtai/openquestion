package com.jt.openquestion.enums;

public enum OpenQuestionLabelTypeEnum {
    /**
     * 知识点标签
     */
    KnowledgePoint(1),

    /**
     * 题目难度标签
     */
    QuestionLevel(2);


    private int value;
    private OpenQuestionLabelTypeEnum(int value){
        this.value =value;
    }

    public int getValue(){
        return this.value;
    }
}
