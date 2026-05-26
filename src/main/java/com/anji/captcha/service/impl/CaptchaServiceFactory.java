package com.anji.captcha.service.impl;

import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.service.CaptchaService;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证码服务工厂。
 */
public final class CaptchaServiceFactory {

    public static volatile Map<String, CaptchaService> instances;

    public static volatile Map<String, CaptchaCacheService> cacheService;

    static {
        instances = new ConcurrentHashMap<String, CaptchaService>();
        cacheService = new ConcurrentHashMap<String, CaptchaCacheService>();
        CaptchaCacheServiceMemImpl cacheServiceMem = new CaptchaCacheServiceMemImpl();
        cacheService.put(cacheServiceMem.type(), cacheServiceMem);
        ClickWordCaptchaServiceImpl clickWordCaptchaService = new ClickWordCaptchaServiceImpl();
        instances.put(clickWordCaptchaService.captchaType(), clickWordCaptchaService);
        instances.put("default", clickWordCaptchaService);
    }

    private CaptchaServiceFactory() {
    }

    /**
     * 获取验证码服务实例。
     *
     * @param properties 配置项
     * @return 验证码服务
     */
    public static CaptchaService getInstance(Properties properties) {
        String captchaType = properties.getProperty("captcha.type", "default");
        CaptchaService captchaService = instances.get(captchaType);
        if (captchaService == null) {
            throw new RuntimeException("unsupported-[captcha.type]=" + captchaType);
        }
        captchaService.init(properties);
        return captchaService;
    }

    /**
     * 获取缓存实现。
     *
     * @param type 缓存类型
     * @return 缓存实现
     */
    public static CaptchaCacheService getCache(String type) {
        return cacheService.get(type);
    }
}
