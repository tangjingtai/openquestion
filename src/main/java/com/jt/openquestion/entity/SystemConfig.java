package com.jt.openquestion.entity;

/**
 * 系统配置项
 */
public class SystemConfig {
    /**
     * AI.API站点token加密密钥
     */
    private String apiSecretKey ;

    /**
     * 第三方题库平台题目标签可信度权重
     * 值存储格式：platform1-weight1,platform2-weight2,...
     */
    private String thirdPartyPlatformLabelWeight ;

    /**
     * 需要爬取题目的第三方题库平台id
     * 值存储格式：platform1,platform2,...
     */
    private String crawlingQuestionThirdPartyPlatform ;

    /**
     * 第三方平台的题目中，重复题目的相似度阈值
     */
    private double thirdPartyQuestionDuplicateScore ;

    /**
     * 获取开放标签时，判断开放标签题目相似度的阈值
     */
    private double openQuestionSimilarScore ;

    /**
     * 不同搜索题目题目入口需要搜索的题库平台
     */
    private String searchQuestionPlatform ;

    /**
     * 更新相似题题目来源用到的相似度阈值
     */
    private double updateQuestionSourceSimilarScore ;

    /**
     * 记录第三方题库的题目功能用途，格式：platformId1-useType,platformId2-useType；用途枚举见OpenQuestionUseTypeEnum
     /// <p>如 1-15 表示题库的题目支持“打知识点标签”、“打题目难度标签”、“获取题目来源”、“获取题目真实难度”；</p>
     */
    private String platformQuestionUseType ;

    /**
     * 秘钥
     */
    private String configKey ;

    /**
     * OSS token
     */
    private String aliyunToken ;

    /**
     * OSS访问ID
     */
    private String aliyunAccessId ;

    /**
     * OSS访问密码
     */
    private String aliyunAccessKey ;

    /**
     * OSS URL
     */
    private String aliyunBaseUrl ;

    /**
     * OSS下载地址
     */
    private String aliyunDownloadBaseUrl ;

    /**
     * OSS 文件Bucket
     */
    private String qbmsFileBucket ;

    /**
     *各题库平台中，题目内容里图片url替换配置，主要用于应对其他题库更换图片服务器域名情况
     */
    private String urlReplaceForPlatform ;

    public String getApiSecretKey() {
        return apiSecretKey;
    }

    public void setApiSecretKey(String apiSecretKey) {
        this.apiSecretKey = apiSecretKey;
    }

    public String getThirdPartyPlatformLabelWeight() {
        return thirdPartyPlatformLabelWeight;
    }

    public void setThirdPartyPlatformLabelWeight(String thirdPartyPlatformLabelWeight) {
        this.thirdPartyPlatformLabelWeight = thirdPartyPlatformLabelWeight;
    }

    public String getCrawlingQuestionThirdPartyPlatform() {
        return crawlingQuestionThirdPartyPlatform;
    }

    public void setCrawlingQuestionThirdPartyPlatform(String crawlingQuestionThirdPartyPlatform) {
        this.crawlingQuestionThirdPartyPlatform = crawlingQuestionThirdPartyPlatform;
    }

    public double getThirdPartyQuestionDuplicateScore() {
        return thirdPartyQuestionDuplicateScore;
    }

    public void setThirdPartyQuestionDuplicateScore(double thirdPartyQuestionDuplicateScore) {
        this.thirdPartyQuestionDuplicateScore = thirdPartyQuestionDuplicateScore;
    }

    public double getOpenQuestionSimilarScore() {
        return openQuestionSimilarScore;
    }

    public void setOpenQuestionSimilarScore(double openQuestionSimilarScore) {
        this.openQuestionSimilarScore = openQuestionSimilarScore;
    }

    public String getSearchQuestionPlatform() {
        return searchQuestionPlatform;
    }

    public void setSearchQuestionPlatform(String searchQuestionPlatform) {
        this.searchQuestionPlatform = searchQuestionPlatform;
    }

    public double getUpdateQuestionSourceSimilarScore() {
        return updateQuestionSourceSimilarScore;
    }

    public void setUpdateQuestionSourceSimilarScore(double updateQuestionSourceSimilarScore) {
        this.updateQuestionSourceSimilarScore = updateQuestionSourceSimilarScore;
    }

    public String getPlatformQuestionUseType() {
        return platformQuestionUseType;
    }

    public void setPlatformQuestionUseType(String platformQuestionUseType) {
        this.platformQuestionUseType = platformQuestionUseType;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getAliyunToken() {
        return aliyunToken;
    }

    public void setAliyunToken(String aliyunToken) {
        this.aliyunToken = aliyunToken;
    }

    public String getAliyunAccessId() {
        return aliyunAccessId;
    }

    public void setAliyunAccessId(String aliyunAccessId) {
        this.aliyunAccessId = aliyunAccessId;
    }

    public String getAliyunAccessKey() {
        return aliyunAccessKey;
    }

    public void setAliyunAccessKey(String aliyunAccessKey) {
        this.aliyunAccessKey = aliyunAccessKey;
    }

    public String getAliyunBaseUrl() {
        return aliyunBaseUrl;
    }

    public void setAliyunBaseUrl(String aliyunBaseUrl) {
        this.aliyunBaseUrl = aliyunBaseUrl;
    }

    public String getAliyunDownloadBaseUrl() {
        return aliyunDownloadBaseUrl;
    }

    public void setAliyunDownloadBaseUrl(String aliyunDownloadBaseUrl) {
        this.aliyunDownloadBaseUrl = aliyunDownloadBaseUrl;
    }

    public String getQbmsFileBucket() {
        return qbmsFileBucket;
    }

    public void setQbmsFileBucket(String qbmsFileBucket) {
        this.qbmsFileBucket = qbmsFileBucket;
    }

    public String getUrlReplaceForPlatform() {
        return urlReplaceForPlatform;
    }

    public void setUrlReplaceForPlatform(String urlReplaceForPlatform) {
        this.urlReplaceForPlatform = urlReplaceForPlatform;
    }
}
