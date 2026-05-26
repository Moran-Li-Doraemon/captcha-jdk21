package com.captcha.demo;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.model.vo.PointVO;
import com.anji.captcha.util.AESUtil;
import com.anji.captcha.util.CacheUtil;
import com.anji.captcha.util.JsonUtil;
import xc.crabapple.core.file.image.captcha.anji.AnjiCaptchaUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 点选验证码烟雾测试。
 */
public class CaptchaSmokeTest {

    /**
     * 验证生成、校验、二次校验链路。
     *
     * @throws Exception AES 加密失败时抛出
     */
    @Test
    public void testClickWordCaptchaFlow() throws Exception {
        AnjiCaptchaUtil.init(null);
        ResponseModel getResponseModel = AnjiCaptchaUtil.get(new CaptchaVO());
        Assertions.assertTrue(getResponseModel.isSuccess());
        CaptchaVO captchaVO = (CaptchaVO) getResponseModel.getRepData();
        Assertions.assertNotNull(captchaVO.getOriginalImageBase64());
        Assertions.assertNotNull(captchaVO.getToken());
        Assertions.assertEquals(Integer.valueOf(3), Integer.valueOf(captchaVO.getWordList().size()));
        String cacheKey = "RUNNING:CAPTCHA:" + captchaVO.getToken();
        String pointJson = CacheUtil.get(cacheKey);
        Assertions.assertNotNull(pointJson);
        List<PointVO> pointList = JsonUtil.parseArray(pointJson, PointVO.class);
        String requestPointJson = JsonUtil.toJSONString(pointList);
        CaptchaVO checkCaptchaVO = new CaptchaVO();
        checkCaptchaVO.setToken(captchaVO.getToken());
        checkCaptchaVO.setPointJson(AESUtil.aesEncrypt(requestPointJson, captchaVO.getSecretKey()));
        ResponseModel checkResponseModel = AnjiCaptchaUtil.check(checkCaptchaVO);
        Assertions.assertTrue(checkResponseModel.isSuccess());
        CaptchaVO checkedCaptchaVO = (CaptchaVO) checkResponseModel.getRepData();
        CaptchaVO verifyCaptchaVO = new CaptchaVO();
        verifyCaptchaVO.setCaptchaVerification(checkedCaptchaVO.getCaptchaVerification());
        ResponseModel verifyResponseModel = AnjiCaptchaUtil.verify(verifyCaptchaVO);
        Assertions.assertTrue(verifyResponseModel.isSuccess());
    }
}
