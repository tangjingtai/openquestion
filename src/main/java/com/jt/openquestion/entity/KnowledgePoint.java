package com.jt.openquestion.entity;

public class KnowledgePoint {
    /**
     *知识点Id
     */
    private int knowledgePointId;

    /**
     * 知识点名称
     */
    private String knowledgePointName;

    /**
     * 学科Id
     */
    private int courseId;

    /**
     * 父知识点Id
     */
    private int parentKnowledgePointId ;

    /**
     * 排序ID
     */
    private int orderIndex ;

    public int getKnowledgePointId() {
        return knowledgePointId;
    }

    public void setKnowledgePointId(int knowledgePointId) {
        this.knowledgePointId = knowledgePointId;
    }

    public String getKnowledgePointName() {
        return knowledgePointName;
    }

    public void setKnowledgePointName(String knowledgePointName) {
        this.knowledgePointName = knowledgePointName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getParentKnowledgePointId() {
        return parentKnowledgePointId;
    }

    public void setParentKnowledgePointId(int parentKnowledgePointId) {
        this.parentKnowledgePointId = parentKnowledgePointId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
