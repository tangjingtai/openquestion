package com.jt.openquestion.enums;

/**
 * Open题库题目可用作的类型
 */
public enum OpenQuestionUseTypeEnum {

    /**
     *  打知识点标签，与题库平台以及题目相关
     */
    KnowledgePointLabel(0x01),

    /**
     * 打题目难度标签，与题库平台以及题目相关
     */
    QuestionLevelLabel(0x02),

    /**
     * 获取题目来源，与题库平台相关
     */
    QuestionSource(0x04),

    /**
     * 获取题目细化的难度，如 1.2，与题库平台相关
     */
    AccurateQuestionLevel(0x08);


    private int value;
    public int getValue() {
        return value;
    }

    private OpenQuestionUseTypeEnum(int value){
        this.value =value;
    }
}
