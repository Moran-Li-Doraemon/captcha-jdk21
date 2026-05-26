package com.anji.captcha.service;

/**
 * 验证码缓存抽象。
 */
public interface CaptchaCacheService {

    /**
     * 写入缓存。
     *
     * @param key 缓存键
     * @param value 缓存值
     * @param expiresInSeconds 过期时间，单位秒
     */
    void set(String key, String value, long expiresInSeconds);

    /**
     * 判断缓存是否存在。
     *
     * @param key 缓存键
     * @return true=存在
     */
    boolean exists(String key);

    /**
     * 删除缓存。
     *
     * @param key 缓存键
     */
    void delete(String key);

    /**
     * 获取缓存值。
     *
     * @param key 缓存键
     * @return 缓存值
     */
    String get(String key);

    /**
     * 获取缓存类型。
     *
     * @return 缓存类型
     */
    String type();

    /**
     * 自增接口。
     *
     * @param key 缓存键
     * @param num 自增值
     * @return 自增后的值
     */
    default Long increment(String key, long num) {
        return Long.valueOf(num);
    }

    /**
     * 设置过期时间。
     *
     * @param key 缓存键
     * @param expiresInSeconds 过期时间，单位秒
     */
    default void setExpire(String key, long expiresInSeconds) {
    }
}
