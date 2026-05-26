package com.anji.captcha.service;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;

import java.util.Properties;

/**
 * 验证码服务。
 */
public interface CaptchaService {

    /**
     * 初始化服务。
     *
     * @param properties 配置项
     */
    void init(Properties properties);

    /**
     * 获取验证码。
     *
     * @param captchaVO 请求对象
     * @return 响应对象
     */
    ResponseModel get(CaptchaVO captchaVO);

    /**
     * 校验验证码。
     *
     * @param captchaVO 请求对象
     * @return 响应对象
     */
    ResponseModel check(CaptchaVO captchaVO);

    /**
     * 二次校验验证码。
     *
     * @param captchaVO 请求对象
     * @return 响应对象
     */
    ResponseModel verification(CaptchaVO captchaVO);

    /**
     * 获取验证码类型。
     *
     * @return 验证码类型
     */
    String captchaType();

    /**
     * 销毁服务。
     *
     * @param properties 配置项
     */
    void destroy(Properties properties);
}
