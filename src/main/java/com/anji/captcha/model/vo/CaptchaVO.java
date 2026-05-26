package com.anji.captcha.model.vo;

import java.io.Serializable;
import java.awt.Point;
import java.util.List;

/**
 * 验证码载体对象。
 */
public class CaptchaVO implements Serializable {

    private String captchaId;

    private String projectCode;

    private String captchaType;

    private String captchaOriginalPath;

    private String captchaFontType;

    private Integer captchaFontSize;

    private String secretKey;

    private String originalImageBase64;

    private PointVO point;

    private String jigsawImageBase64;

    private List<String> wordList;

    private List<Point> pointList;

    private String pointJson;

    private String token;

    private Boolean result;

    private String captchaVerification;

    private String clientUid;

    private Long ts;

    private String browserInfo;

    /**
     * 默认构造。
     */
    public CaptchaVO() {
        this.result = Boolean.FALSE;
    }

    /**
     * 清空客户端标识，避免把浏览器信息回传给前端。
     */
    public void resetClientFlag() {
        this.browserInfo = null;
        this.clientUid = null;
    }

    /**
     * 获取验证码 ID。
     *
     * @return 验证码 ID
     */
    public String getCaptchaId() {
        return captchaId;
    }

    /**
     * 设置验证码 ID。
     *
     * @param captchaId 验证码 ID
     */
    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

    /**
     * 获取项目编号。
     *
     * @return 项目编号
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * 设置项目编号。
     *
     * @param projectCode 项目编号
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * 获取验证码类型。
     *
     * @return 验证码类型
     */
    public String getCaptchaType() {
        return captchaType;
    }

    /**
     * 设置验证码类型。
     *
     * @param captchaType 验证码类型
     */
    public void setCaptchaType(String captchaType) {
        this.captchaType = captchaType;
    }

    /**
     * 获取验证码底图路径。
     *
     * @return 验证码底图路径
     */
    public String getCaptchaOriginalPath() {
        return captchaOriginalPath;
    }

    /**
     * 设置验证码底图路径。
     *
     * @param captchaOriginalPath 验证码底图路径
     */
    public void setCaptchaOriginalPath(String captchaOriginalPath) {
        this.captchaOriginalPath = captchaOriginalPath;
    }

    /**
     * 获取验证码字体。
     *
     * @return 验证码字体
     */
    public String getCaptchaFontType() {
        return captchaFontType;
    }

    /**
     * 设置验证码字体。
     *
     * @param captchaFontType 验证码字体
     */
    public void setCaptchaFontType(String captchaFontType) {
        this.captchaFontType = captchaFontType;
    }

    /**
     * 获取验证码字体大小。
     *
     * @return 字体大小
     */
    public Integer getCaptchaFontSize() {
        return captchaFontSize;
    }

    /**
     * 设置验证码字体大小。
     *
     * @param captchaFontSize 字体大小
     */
    public void setCaptchaFontSize(Integer captchaFontSize) {
        this.captchaFontSize = captchaFontSize;
    }

    /**
     * 获取密钥。
     *
     * @return 密钥
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * 设置密钥。
     *
     * @param secretKey 密钥
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * 获取底图 Base64。
     *
     * @return Base64 字符串
     */
    public String getOriginalImageBase64() {
        return originalImageBase64;
    }

    /**
     * 设置底图 Base64。
     *
     * @param originalImageBase64 Base64 字符串
     */
    public void setOriginalImageBase64(String originalImageBase64) {
        this.originalImageBase64 = originalImageBase64;
    }

    /**
     * 获取点对象。
     *
     * @return 点对象
     */
    public PointVO getPoint() {
        return point;
    }

    /**
     * 设置点对象。
     *
     * @param point 点对象
     */
    public void setPoint(PointVO point) {
        this.point = point;
    }

    /**
     * 获取拼图图片 Base64。
     *
     * @return Base64 字符串
     */
    public String getJigsawImageBase64() {
        return jigsawImageBase64;
    }

    /**
     * 设置拼图图片 Base64。
     *
     * @param jigsawImageBase64 Base64 字符串
     */
    public void setJigsawImageBase64(String jigsawImageBase64) {
        this.jigsawImageBase64 = jigsawImageBase64;
    }

    /**
     * 获取文字列表。
     *
     * @return 文字列表
     */
    public List<String> getWordList() {
        return wordList;
    }

    /**
     * 设置文字列表。
     *
     * @param wordList 文字列表
     */
    public void setWordList(List<String> wordList) {
        this.wordList = wordList;
    }

    /**
     * 获取点列表。
     *
     * @return 点列表
     */
    public List<Point> getPointList() {
        return pointList;
    }

    /**
     * 设置点列表。
     *
     * @param pointList 点列表
     */
    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }

    /**
     * 获取点 JSON。
     *
     * @return 点 JSON
     */
    public String getPointJson() {
        return pointJson;
    }

    /**
     * 设置点 JSON。
     *
     * @param pointJson 点 JSON
     */
    public void setPointJson(String pointJson) {
        this.pointJson = pointJson;
    }

    /**
     * 获取 token。
     *
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置 token。
     *
     * @param token token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取结果。
     *
     * @return 结果
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * 设置结果。
     *
     * @param result 结果
     */
    public void setResult(Boolean result) {
        this.result = result;
    }

    /**
     * 获取二次校验串。
     *
     * @return 二次校验串
     */
    public String getCaptchaVerification() {
        return captchaVerification;
    }

    /**
     * 设置二次校验串。
     *
     * @param captchaVerification 二次校验串
     */
    public void setCaptchaVerification(String captchaVerification) {
        this.captchaVerification = captchaVerification;
    }

    /**
     * 获取客户端标识。
     *
     * @return 客户端标识
     */
    public String getClientUid() {
        return clientUid;
    }

    /**
     * 设置客户端标识。
     *
     * @param clientUid 客户端标识
     */
    public void setClientUid(String clientUid) {
        this.clientUid = clientUid;
    }

    /**
     * 获取时间戳。
     *
     * @return 时间戳
     */
    public Long getTs() {
        return ts;
    }

    /**
     * 设置时间戳。
     *
     * @param ts 时间戳
     */
    public void setTs(Long ts) {
        this.ts = ts;
    }

    /**
     * 获取浏览器信息。
     *
     * @return 浏览器信息
     */
    public String getBrowserInfo() {
        return browserInfo;
    }

    /**
     * 设置浏览器信息。
     *
     * @param browserInfo 浏览器信息
     */
    public void setBrowserInfo(String browserInfo) {
        this.browserInfo = browserInfo;
    }
}
