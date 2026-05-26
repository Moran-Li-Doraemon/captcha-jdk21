package com.anji.captcha.model.vo;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 点选坐标对象。
 */
public class PointVO {

    private String secretKey;

    public int x;

    public int y;

    /**
     * 无参构造。
     */
    public PointVO() {
    }

    /**
     * 创建坐标。
     *
     * @param x x 坐标
     * @param y y 坐标
     */
    public PointVO(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 创建坐标。
     *
     * @param x x 坐标
     * @param y y 坐标
     * @param secretKey 加密密钥
     */
    public PointVO(int x, int y, String secretKey) {
        this.x = x;
        this.y = y;
        this.secretKey = secretKey;
    }

    /**
     * 序列化为 JSON 字符串。
     *
     * @return JSON 字符串
     */
    public String toJsonString() {
        return String.format("{\"secretKey\":\"%s\",\"x\":%d,\"y\":%d}", secretKey, x, y);
    }

    /**
     * 从 JSON 片段解析坐标。
     *
     * @param json JSON 片段
     * @return 当前对象
     */
    public PointVO parse(String json) {
        if (json == null) {
            return this;
        }
        this.x = parseInt(json, "\\\"x\\\"\\s*:\\s*(-?\\d+)");
        this.y = parseInt(json, "\\\"y\\\"\\s*:\\s*(-?\\d+)");
        this.secretKey = parseString(json, "\\\"secretKey\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"");
        return this;
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
     * 获取 x 坐标。
     *
     * @return x 坐标
     */
    public int getX() {
        return x;
    }

    /**
     * 设置 x 坐标。
     *
     * @param x x 坐标
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * 获取 y 坐标。
     *
     * @return y 坐标
     */
    public int getY() {
        return y;
    }

    /**
     * 设置 y 坐标。
     *
     * @param y y 坐标
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PointVO pointVO = (PointVO) object;
        return x == pointVO.x && y == pointVO.y && Objects.equals(secretKey, pointVO.secretKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secretKey, Integer.valueOf(x), Integer.valueOf(y));
    }

    private static int parseInt(String value, String pattern) {
        String result = parseString(value, pattern);
        if (result == null || result.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(result);
    }

    private static String parseString(String value, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(value);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
