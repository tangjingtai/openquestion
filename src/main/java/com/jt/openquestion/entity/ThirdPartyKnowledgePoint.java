package com.jt.openquestion.entity;

public class ThirdPartyKnowledgePoint {
    /**
     * 第三方题库平台的知识点Id
     */
    private String thirdPartyKnowledgePointId;

    /**
     * 来源平台Id， 1：Motk，2：学科网，3：菁优网，4：未来脑，...
     */
    private int sourcePlatform;

    /**
     * 第三方题库平台的知识点名称
     */
    private String thirdPartyKnowledgePointName;

    /// <summary>
    /// 学科Id
    /// </summary>
    private int courseID;

    /**
     * 是否叶子节点，既没有子知识点
     */
    private boolean isLeaf;

    /// <summary>
    /// 父知识点Id
    /// </summary>
    /**
     *
     */
    private String parentKnowledgePointID;

    /**
     * 第三方题库平台的知识点ID对应的MOTK知识点ID
     */
    private int motkKnowledgePointId;

    /**
     * 状态，1 有效，0 无效；
     * 主要为了兼容一些之前爬取的题目使用老知识点的情况。
     */
    private int statusFlag;

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

    public String getThirdPartyKnowledgePointName() {
        return thirdPartyKnowledgePointName;
    }

    public void setThirdPartyKnowledgePointName(String thirdPartyKnowledgePointName) {
        this.thirdPartyKnowledgePointName = thirdPartyKnowledgePointName;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public String getParentKnowledgePointID() {
        return parentKnowledgePointID;
    }

    public void setParentKnowledgePointID(String parentKnowledgePointID) {
        this.parentKnowledgePointID = parentKnowledgePointID;
    }

    public int getMotkKnowledgePointId() {
        return motkKnowledgePointId;
    }

    public void setMotkKnowledgePointId(int motkKnowledgePointId) {
        this.motkKnowledgePointId = motkKnowledgePointId;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }
}
