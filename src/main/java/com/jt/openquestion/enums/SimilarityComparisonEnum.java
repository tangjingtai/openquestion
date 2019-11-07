package com.jt.openquestion.enums;

public enum SimilarityComparisonEnum {
    /**
     * 正向比对，A对比B时，内容A与在内容B中的匹配度（即A有多少内容在B中出现过）
     */
    POSITIVE(0x01),

    /**
     * 反向比对，A对比B时，内容B与在内容A中的匹配度（即B有多少内容在A中出现过）
     */
    REVERSE(0x02),

    /**
     * 全面比对，正向+反向
     */
    Overall(POSITIVE.value | REVERSE.value);

    private int value;
    public int getValue() {
        return value;
    }

    private SimilarityComparisonEnum(int value){
        this.value =value;
    }
}
