package com.jt.openquestion.entity;

public class ThirdPartyKnowledgePointIdTuple {
    public ThirdPartyKnowledgePointIdTuple(String thirdPartyKnowledgePointId, int sourcePlatform, int courseId) {
        this.thirdPartyKnowledgePointId = thirdPartyKnowledgePointId;
        this.sourcePlatform = sourcePlatform;
        this.courseId = courseId;
    }

    /**
     * 第三方题库知识点Id
     */
    private String thirdPartyKnowledgePointId;

    /**
     * 来源题库平台
     */
    private int sourcePlatform;

    /**
     * 学科Id
     */
    private int courseId;

    public String getThirdPartyKnowledgePointId() {
        return thirdPartyKnowledgePointId;
    }

    public void setThirdPartyKnowledgePointId(String thirdPartyKnowledgePointId) {
        this.thirdPartyKnowledgePointId = thirdPartyKnowledgePointId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThirdPartyKnowledgePointIdTuple that = (ThirdPartyKnowledgePointIdTuple) o;

        if (sourcePlatform != that.sourcePlatform) return false;
        if (courseId != that.courseId) return false;
        return thirdPartyKnowledgePointId != null ? thirdPartyKnowledgePointId.equals(that.thirdPartyKnowledgePointId) : that.thirdPartyKnowledgePointId == null;
    }

    @Override
    public int hashCode() {
        int result = thirdPartyKnowledgePointId != null ? thirdPartyKnowledgePointId.hashCode() : 17;
        result = 13 * result + sourcePlatform;
        result = 13 * result + courseId;
        return result;
    }
}
