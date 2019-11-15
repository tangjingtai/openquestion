package com.jt.openquestion.entity;

public class LiteOpenQuestion {

    /**
     * 开放标签题目Id
     */
    private long openQuestionId;

    /**
     * 开放标签题目内容
     */
    private String content;

    /**
     * 开放标签题目来源题库平台
     */
    private int sourcePlatform;

    /**
     * 开放标签题目学科Id
     */
    private int courseId;

    /**
     * 可用于打标签的类型（位枚举，位说明：1 打知识点标签，2 打题目难度标签 ）
     *  <p>ES中该字段缺失或值为-1表示支持打知识点、题目难度标签</p>
     */
    private int labelUseType;

    /**
     * 开放标签题目答案
     */
    private String answer;

    /**
     * 开放标签题目解析
     */
    private String analysis;

    public long getOpenQuestionId() {
        return openQuestionId;
    }

    public void setOpenQuestionId(long openQuestionId) {
        this.openQuestionId = openQuestionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSourcePlatform() {
        return sourcePlatform;
    }

    public void setSourcePlatform(int sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getLabelUseType() {
        return labelUseType;
    }

    public void setLabelUseType(int labelUseType) {
        this.labelUseType = labelUseType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    /**
     * 获取索引名称
     * @param sourcePlatform 来源平台类型
     * @param courseId 学科Id
     * @return 索引名称
     */
    public static String fetchIndexName(int sourcePlatform, int courseId)
    {
        return String.format("openquestionindex-%d-%d", sourcePlatform, courseId);
    }

    /// <summary>
    /// QuestionId是唯一的
    /// </summary>
    /// <param name="questionId"></param>
    /// <returns></returns>

    /**
     * 获取索引中的文档Id
     * @param openQuestionId open题库Id
     * @return 索引中的文档Id
     */
    public static String fetchIndexDocumentId(long openQuestionId)
    {
        return openQuestionId +"";
    }
}
