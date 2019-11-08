package com.jt.openquestion.entity;

import com.jt.openquestion.enums.TikuPlatformEnum;

import java.util.Date;
import java.util.List;

public class OpenQuestion {
    /**
     * Open question id
     */
    private Long openQuestionId;
    private String sourceQuestionId;
    private Integer sourcePlatform;
    private String questionContent;
    private Double originalQuestionLevel;
    private Integer courseId;
    private String originalKnowledgePointLabels;
    /**
     * 最终知识点（已转换为MOTK中的知识点Id），该知识点是经过计算后可信度最高的一部分；多个以英文逗号分隔
     */
    private String knowledgePointLabels;
    private String questionSource;
    private String useRange;
    private Date questionOnlineTime;
    private Double credibility;
    private Date createdOn;
    private Date modifiedOn;
    private List<OpenQuestionLabel> openQuestionLabels;
    private OpenQuestionExtension openQuestionExtension;
    private Integer labelUseType;

    public Integer getLabelUseType() {
        return labelUseType;
    }

    public void setLabelUseType(Integer labelUseType) {
        this.labelUseType = labelUseType;
    }

    public OpenQuestionExtension getOpenQuestionExtension() {
        return openQuestionExtension;
    }

    public void setOpenQuestionExtension(OpenQuestionExtension openQuestionExtension) {
        this.openQuestionExtension = openQuestionExtension;
    }

    public List<OpenQuestionLabel> getOpenQuestionLabels() {
        return openQuestionLabels;
    }

    public void setOpenQuestionLabels(List<OpenQuestionLabel> openQuestionLabels) {
        this.openQuestionLabels = openQuestionLabels;
    }

    public Long getOpenQuestionId() {
        return openQuestionId;
    }

    public void setOpenQuestionId(Long openQuestionId) {
        this.openQuestionId = openQuestionId;
    }

    public String getSourceQuestionId() {
        return sourceQuestionId;
    }

    public void setSourceQuestionId(String sourceQuestionId) {
        this.sourceQuestionId = sourceQuestionId;
    }

    public Integer getSourcePlatform() {
        return sourcePlatform;
    }

    public void setSourcePlatform(Integer sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public Double getOriginalQuestionLevel() {
        return originalQuestionLevel;
    }

    public void setOriginalQuestionLevel(Double originalQuestionLevel) {
        this.originalQuestionLevel = originalQuestionLevel;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getOriginalKnowledgePointLabels() {
        return originalKnowledgePointLabels;
    }

    public void setOriginalKnowledgePointLabels(String originalKnowledgePointLabels) {
        this.originalKnowledgePointLabels = originalKnowledgePointLabels;
    }

    public String getKnowledgePointLabels() {
        return knowledgePointLabels;
    }

    public void setKnowledgePointLabels(String knowledgePointLabels) {
        this.knowledgePointLabels = knowledgePointLabels;
    }

    public String getQuestionSource() {
        return questionSource;
    }

    public void setQuestionSource(String questionSource) {
        this.questionSource = questionSource;
    }

    public String getUseRange() {
        return useRange;
    }

    public void setUseRange(String useRange) {
        this.useRange = useRange;
    }

    public Date getQuestionOnlineTime() {
        return questionOnlineTime;
    }

    public void setQuestionOnlineTime(Date questionOnlineTime) {
        this.questionOnlineTime = questionOnlineTime;
    }

    public Double getCredibility() {
        return credibility;
    }

    public void setCredibility(Double credibility) {
        this.credibility = credibility;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public int fetchCalculateQuestionLevel()
    {
        TikuPlatformEnum platformEnum = TikuPlatformEnum.parse(this.sourcePlatform);
            switch (platformEnum)
            {
                case Motk:
                case MotkTest:
                case MotkUat:
                case XueKe:
                case WeiLaiNao:
                    return Math.max((int)Math.ceil(this.originalQuestionLevel - 0.2), 1);
                case Jyeoo:
                    return Math.max((int)Math.ceil((1 - this.originalQuestionLevel) * 5 - 0.2), 1);
                case TiKu:
                    return Math.max((int)Math.ceil(this.originalQuestionLevel * 5 - 0.2), 1);
                default:
                    return Math.max((int)Math.ceil(this.originalQuestionLevel - 0.2), 1);
            }
    }
}
