package com.anji.captcha.service.impl;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.util.CacheUtil;
import com.anji.captcha.util.ImageUtils;
import com.anji.captcha.util.MD5Util;
import com.anji.captcha.util.StringUtils;

import java.awt.Font;
import java.util.Properties;

/**
 * 验证码抽象服务。
 */
public abstract class AbstractCaptchaService implements CaptchaService {

    protected static final String IMAGE_TYPE_PNG = "png";

    protected static int HAN_ZI_SIZE = 25;

    protected static int HAN_ZI_SIZE_HALF = 12;

    protected static String REDIS_CAPTCHA_KEY = "RUNNING:CAPTCHA:%s";

    protected static String REDIS_SECOND_CAPTCHA_KEY = "RUNNING:CAPTCHA:second-%s";

    protected static Long EXPIRESIN_SECONDS = Long.valueOf(120L);

    protected static Long EXPIRESIN_THREE = Long.valueOf(180L);

    protected static String waterMark = "我的水印";

    protected static String waterMarkFontStr = "WenQuanZhengHei.ttf";

    protected Font waterMarkFont = new Font("SansSerif", Font.PLAIN, HAN_ZI_SIZE / 2);

    protected static String slipOffset = "5";

    protected static Boolean captchaAesStatus = Boolean.TRUE;

    protected static String clickWordFontStr = "NotoSerif-Light.ttf";

    protected Font clickWordFont = new Font("SansSerif", Font.BOLD, HAN_ZI_SIZE);

    protected static String cacheType = "local";

    protected static int captchaInterferenceOptions = 0;

    /**
     * 初始化公共配置。
     *
     * @param properties 配置项
     */
    @Override
    public void init(Properties properties) {
        boolean initOriginal = Boolean.parseBoolean(properties.getProperty("captcha.init.original", "true"));
        if (!initOriginal) {
            ImageUtils.cacheImage(properties.getProperty("captcha.captchaOriginalPath.jigsaw"), properties.getProperty("captcha.captchaOriginalPath.pic-click"), properties.getProperty("captcha.captchaOriginalPath.rotate"));
        }
        waterMark = properties.getProperty("captcha.water.mark", "我的水印");
        slipOffset = properties.getProperty("captcha.slip.offset", "5");
        waterMarkFontStr = properties.getProperty("captcha.water.font", "WenQuanZhengHei.ttf");
        captchaAesStatus = Boolean.valueOf(Boolean.parseBoolean(properties.getProperty("captcha.aes.status", "true")));
        clickWordFontStr = properties.getProperty("captcha.font.type", "Dialog");
        cacheType = properties.getProperty("captcha.cacheType", "local");
        captchaInterferenceOptions = Integer.parseInt(properties.getProperty("captcha.interference.options", "0"));
        loadWaterMarkFont();
        if ("local".equals(cacheType)) {
            CacheUtil.init(Integer.parseInt(properties.getProperty("captcha.cache.number", "1000")), Long.parseLong(properties.getProperty("captcha.timing.clear", "180")));
        }
    }

    /**
     * 获取缓存实现。
     *
     * @param type 缓存类型
     * @return 缓存实现
     */
    protected CaptchaCacheService getCacheService(String type) {
        return CaptchaServiceFactory.getCache(type);
    }

    /**
     * 销毁服务。
     *
     * @param properties 配置项
     */
    @Override
    public void destroy(Properties properties) {
    }

    /**
     * 获取验证码。
     *
     * @param captchaVO 请求对象
     * @return 校验结果
     */
    @Override
    public ResponseModel get(CaptchaVO captchaVO) {
        return null;
    }

    /**
     * 校验验证码。
     *
     * @param captchaVO 请求对象
     * @return 校验结果
     */
    @Override
    public ResponseModel check(CaptchaVO captchaVO) {
        return null;
    }

    /**
     * 二次校验验证码。
     *
     * @param captchaVO 请求对象
     * @return 校验结果
     */
    @Override
    public ResponseModel verification(CaptchaVO captchaVO) {
        return null;
    }

    /**
     * 判断响应是否有效。
     *
     * @param responseModel 响应对象
     * @return true=有效
     */
    protected boolean validatedReq(ResponseModel responseModel) {
        return responseModel == null || responseModel.isSuccess();
    }

    /**
     * 获取校验客户端标识。
     *
     * @param captchaVO 请求对象
     * @return 客户端标识
     */
    protected String getValidateClientId(CaptchaVO captchaVO) {
        if (StringUtils.isNotEmpty(captchaVO.getBrowserInfo())) {
            return MD5Util.md5(captchaVO.getBrowserInfo());
        }
        if (StringUtils.isNotEmpty(captchaVO.getClientUid())) {
            return captchaVO.getClientUid();
        }
        return null;
    }

    /**
     * 校验失败后的回调。
     *
     * @param captchaVO 请求对象
     */
    protected void afterValidateFail(CaptchaVO captchaVO) {
    }

    private void loadWaterMarkFont() {
        try {
            if (waterMarkFontStr.toLowerCase().endsWith(".ttf") || waterMarkFontStr.toLowerCase().endsWith(".ttc") || waterMarkFontStr.toLowerCase().endsWith(".otf")) {
                java.io.InputStream inputStream = getClass().getResourceAsStream("/fonts/" + waterMarkFontStr);
                if (inputStream != null) {
                    waterMarkFont = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(Font.BOLD, HAN_ZI_SIZE / 2.0f);
                    return;
                }
            }
            waterMarkFont = new Font(waterMarkFontStr, Font.BOLD, HAN_ZI_SIZE / 2);
        } catch (Exception throwable) {
            waterMarkFont = new Font("SansSerif", Font.BOLD, HAN_ZI_SIZE / 2);
        }
    }
}
