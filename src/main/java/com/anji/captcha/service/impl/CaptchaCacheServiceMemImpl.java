package com.anji.captcha.service.impl;

import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.util.CacheUtil;

/**
 * 本地缓存实现。
 */
public class CaptchaCacheServiceMemImpl implements CaptchaCacheService {

    /**
     * 写入缓存。
     *
     * @param key 缓存键
     * @param value 缓存值
     * @param expiresInSeconds 过期时间
     */
    @Override
    public void set(String key, String value, long expiresInSeconds) {
        CacheUtil.set(key, value, expiresInSeconds);
    }

    /**
     * 判断缓存是否存在。
     *
     * @param key 缓存键
     * @return true=存在
     */
    @Override
    public boolean exists(String key) {
        return CacheUtil.exists(key);
    }

    /**
     * 删除缓存。
     *
     * @param key 缓存键
     */
    @Override
    public void delete(String key) {
        CacheUtil.delete(key);
    }

    /**
     * 获取缓存值。
     *
     * @param key 缓存键
     * @return 缓存值
     */
    @Override
    public String get(String key) {
        return CacheUtil.get(key);
    }

    /**
     * 自增缓存。
     *
     * @param key 缓存键
     * @param num 自增值
     * @return 自增结果
     */
    @Override
    public Long increment(String key, long num) {
        String value = CacheUtil.get(key);
        long result;
        if (value == null) {
            result = num;
        } else {
            result = Long.parseLong(value) + num;
        }
        CacheUtil.set(key, String.valueOf(result), 0L);
        return Long.valueOf(result);
    }

    /**
     * 设置过期时间。
     *
     * @param key 缓存键
     * @param expiresInSeconds 过期时间
     */
    @Override
    public void setExpire(String key, long expiresInSeconds) {
        CacheUtil.setExpire(key, expiresInSeconds);
    }

    /**
     * 缓存类型。
     *
     * @return local
     */
    @Override
    public String type() {
        return "local";
    }
}
