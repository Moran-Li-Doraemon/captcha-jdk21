package com.anji.captcha.service.impl;

import com.anji.captcha.model.common.CaptchaTypeEnum;
import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.model.vo.PointVO;
import com.anji.captcha.util.AESUtil;
import com.anji.captcha.util.ImageUtils;
import com.anji.captcha.util.JsonUtil;
import com.anji.captcha.util.RandomUtils;
import com.anji.captcha.util.StringUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * 文字点选验证码服务。
 */
public class ClickWordCaptchaServiceImpl extends AbstractCaptchaService {

    public static String HAN_ZI = "的一了是我不在人们有来他这上着个地到大里说就去子得也和那要下看天时过出小么起你都把好还多没为又可家学只以主会样年想生同老中十从自面前头道它后然走很像见两用她国动进成回什边作对开而己些现山民候经发工向事命给长水几义三声于高手知理眼志点心战二问但身方实吃做叫当住听革打呢真全才四已所敌之最光产情路分总条白话东席次亲如被花口放儿常气五第使写军吧文运再果怎定许快明行因别飞外树物活部门无往船望新带队先力完却站代员机更九您每风级跟笑啊孩万少直意夜比阶连车重便斗马哪化太指变社似士者干石满日决百原拿群究各六本思解立河村八难早论吗根共让相研今其书坐接应关信觉步反处记将千找争领或师结块跑谁草越字加脚紧爱等习阵怕月青半火法题建赶位唱海七女任件感准张团屋离色脸片科倒睛利世刚且由送切星导晚表够整认响雪流未场该并底深刻平伟忙提确近亮轻讲农古黑告界拉名呀土清阳照办史改历转画造嘴此治北必服雨穿内识验传业菜爬睡兴形量咱观苦体众通冲合破友度术饭公旁房极南枪读沙岁线野坚空收算至政城劳落钱特围弟胜教热展包歌类渐强数乡呼性音答哥际旧神座章帮啦受系令跳非何牛取入岸敢掉忽种装顶急林停息句区衣般报叶压慢叔背细";

    protected static String clickWordFontStr = "Dialog";

    private int wordTotalCount = 4;

    private boolean fontColorRandom = true;

    /**
     * 获取验证码类型。
     *
     * @return clickWord
     */
    @Override
    public String captchaType() {
        return CaptchaTypeEnum.CLICKWORD.getCodeValue();
    }

    /**
     * 初始化点选验证码。
     *
     * @param properties 配置项
     */
    @Override
    public void init(Properties properties) {
        super.init(properties);
        clickWordFontStr = properties.getProperty("captcha.font.type", "Dialog");
        HAN_ZI_SIZE = Integer.valueOf(properties.getProperty("captcha.font.size", "25")).intValue();
        if (clickWordFontStr.toLowerCase().endsWith(".ttf") || clickWordFontStr.toLowerCase().endsWith(".ttc") || clickWordFontStr.toLowerCase().endsWith(".otf")) {
            try {
                java.io.InputStream inputStream = getClass().getResourceAsStream("/fonts/" + clickWordFontStr);
                if (inputStream != null) {
                    clickWordFont = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(Font.BOLD, (float) HAN_ZI_SIZE);
                } else {
                    clickWordFont = new Font("Dialog", Font.BOLD, HAN_ZI_SIZE);
                }
            } catch (Exception throwable) {
                clickWordFont = new Font("Dialog", Font.BOLD, HAN_ZI_SIZE);
            }
        } else {
            int fontStyle = Integer.valueOf(properties.getProperty("captcha.font.style", "1")).intValue();
            clickWordFont = new Font(clickWordFontStr, fontStyle, HAN_ZI_SIZE);
        }
        wordTotalCount = Integer.valueOf(properties.getProperty("captcha.word.count", "4")).intValue();
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
     * 获取点选验证码。
     *
     * @param captchaVO 请求对象
     * @return 响应对象
     */
    @Override
    public ResponseModel get(CaptchaVO captchaVO) {
        ResponseModel responseModel = super.get(captchaVO);
        if (!validatedReq(responseModel)) {
            return responseModel;
        }
        BufferedImage image = ImageUtils.getPicClick();
        if (image == null) {
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_BASEMAP_NULL);
        }
        CaptchaVO data = getImageData(image);
        if (data == null || StringUtils.isBlank(data.getOriginalImageBase64())) {
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_ERROR);
        }
        return ResponseModel.successData(data);
    }

    /**
     * 校验点选验证码。
     *
     * @param captchaVO 请求对象
     * @return 响应对象
     */
    @Override
    public ResponseModel check(CaptchaVO captchaVO) {
        ResponseModel responseModel = super.check(captchaVO);
        if (!validatedReq(responseModel)) {
            return responseModel;
        }
        String tokenKey = String.format(REDIS_CAPTCHA_KEY, captchaVO.getToken());
        if (!CaptchaServiceFactory.getCache(cacheType).exists(tokenKey)) {
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
        }
        String pointJsonFromCache = CaptchaServiceFactory.getCache(cacheType).get(tokenKey);
        CaptchaServiceFactory.getCache(cacheType).delete(tokenKey);
        List<PointVO> realPointList;
        List<PointVO> userPointList;
        String decryptPointJson;
        try {
            realPointList = JsonUtil.parseArray(pointJsonFromCache, PointVO.class);
            decryptPointJson = AESUtil.aesDecrypt(captchaVO.getPointJson(), realPointList.get(0).getSecretKey());
            userPointList = JsonUtil.parseArray(decryptPointJson, PointVO.class);
        } catch (Exception throwable) {
            afterValidateFail(captchaVO);
            return ResponseModel.errorMsg(throwable.getMessage());
        }
        if (realPointList == null || userPointList == null || realPointList.size() != userPointList.size()) {
            afterValidateFail(captchaVO);
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR);
        }
        for (int index = 0; index < realPointList.size(); index++) {
            PointVO realPoint = realPointList.get(index);
            PointVO userPoint = userPointList.get(index);
            if (realPoint.getX() - HAN_ZI_SIZE > userPoint.getX() || userPoint.getX() > realPoint.getX() + HAN_ZI_SIZE || realPoint.getY() - HAN_ZI_SIZE > userPoint.getY() || userPoint.getY() > realPoint.getY() + HAN_ZI_SIZE) {
                afterValidateFail(captchaVO);
                return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR);
            }
        }
        String secretKey = realPointList.get(0).getSecretKey();
        String captchaVerification;
        try {
            captchaVerification = AESUtil.aesEncrypt(captchaVO.getToken() + "---" + decryptPointJson + secretKey, secretKey);
        } catch (Exception throwable) {
            afterValidateFail(captchaVO);
            return ResponseModel.errorMsg(throwable.getMessage());
        }
        String verificationCacheKey = String.format(REDIS_SECOND_CAPTCHA_KEY, captchaVerification);
        CaptchaServiceFactory.getCache(cacheType).set(verificationCacheKey, captchaVO.getToken(), EXPIRESIN_THREE.longValue());
        captchaVO.setResult(Boolean.TRUE);
        captchaVO.resetClientFlag();
        captchaVO.setCaptchaVerification(captchaVerification);
        return ResponseModel.successData(captchaVO);
    }

    /**
     * 二次校验点选验证码。
     *
     * @param captchaVO 请求对象
     * @return 响应对象
     */
    @Override
    public ResponseModel verification(CaptchaVO captchaVO) {
        ResponseModel responseModel = super.verification(captchaVO);
        if (!validatedReq(responseModel)) {
            return responseModel;
        }
        String verificationCacheKey = String.format(REDIS_SECOND_CAPTCHA_KEY, captchaVO.getCaptchaVerification());
        if (!CaptchaServiceFactory.getCache(cacheType).exists(verificationCacheKey)) {
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
        }
        CaptchaServiceFactory.getCache(cacheType).delete(verificationCacheKey);
        return ResponseModel.success();
    }

    /**
     * 返回实际写入缓存的题面数据。
     *
     * @param image 底图
     * @return 验证码对象
     */
    private CaptchaVO getImageData(BufferedImage image) {
        CaptchaVO captchaVO = new CaptchaVO();
        List<String> wordList = new ArrayList<String>();
        List<PointVO> pointList = new ArrayList<PointVO>();
        Graphics graphics = image.getGraphics();
        int width = image.getWidth();
        int height = image.getHeight();
        int wordCount = wordTotalCount;
        int targetIndex = RandomUtils.getRandomInt(1, wordCount).intValue();
        Set<String> randomWords = getRandomWords(wordCount);
        String secretKey = null;
        if (captchaAesStatus.booleanValue()) {
            secretKey = AESUtil.getKey();
        }
        int index = 0;
        for (String word : randomWords) {
            PointVO pointVO = randomWordPoint(width, height, index, wordCount);
            pointVO.setSecretKey(secretKey);
            if (fontColorRandom) {
                graphics.setColor(new Color(RandomUtils.getRandomInt(1, 255).intValue(), RandomUtils.getRandomInt(1, 255).intValue(), RandomUtils.getRandomInt(1, 255).intValue()));
            } else {
                graphics.setColor(Color.BLACK);
            }
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(RandomUtils.getRandomInt(-45, 45).intValue()), 0D, 0D);
            Font currentFont = clickWordFont.deriveFont(affineTransform);
            graphics.setFont(currentFont);
            graphics.drawString(word, pointVO.getX(), pointVO.getY());
            if (targetIndex - 1 != index) {
                wordList.add(word);
                pointList.add(pointVO);
            }
            index++;
        }
        graphics.setFont(waterMarkFont);
        graphics.setColor(Color.white);
        graphics.drawString(waterMark, width - getEnOrChLength(waterMark), height - HAN_ZI_SIZE / 2 + 7);
        captchaVO.setOriginalImageBase64(ImageUtils.getImageToBase64Str(image).replaceAll("\r|\n", ""));
        captchaVO.setWordList(wordList);
        captchaVO.setToken(RandomUtils.getUUID());
        captchaVO.setSecretKey(secretKey);
        String cacheKey = String.format(REDIS_CAPTCHA_KEY, captchaVO.getToken());
        CaptchaServiceFactory.getCache(cacheType).set(cacheKey, JsonUtil.toJSONString(pointList), EXPIRESIN_SECONDS.longValue());
        return captchaVO;
    }

    /**
     * 生成不重复的随机汉字集合。
     *
     * @param count 数量
     * @return 随机汉字集合
     */
    private Set<String> getRandomWords(int count) {
        Set<String> result = new HashSet<String>();
        int max = HAN_ZI.length();
        while (result.size() < count) {
            result.add(String.valueOf(HAN_ZI.charAt(RandomUtils.getRandomInt(max))));
        }
        return result;
    }

    /**
     * 生成单个文字的坐标。
     *
     * @param width 图片宽度
     * @param height 图片高度
     * @param currentIndex 当前索引
     * @param totalCount 总数量
     * @return 坐标对象
     */
    private static PointVO randomWordPoint(int width, int height, int currentIndex, int totalCount) {
        int split = width / (totalCount + 1);
        int x;
        if (split < HAN_ZI_SIZE_HALF) {
            x = RandomUtils.getRandomInt(1 + HAN_ZI_SIZE_HALF, width).intValue();
        } else if (currentIndex == 0) {
            x = RandomUtils.getRandomInt(1 + HAN_ZI_SIZE_HALF, split - HAN_ZI_SIZE_HALF).intValue();
        } else {
            x = RandomUtils.getRandomInt(split * currentIndex + HAN_ZI_SIZE_HALF, split * (currentIndex + 1) - HAN_ZI_SIZE_HALF).intValue();
        }
        int y = RandomUtils.getRandomInt(HAN_ZI_SIZE, height - HAN_ZI_SIZE).intValue();
        return new PointVO(x, y, null);
    }

    /**
     * 计算中文和英文混排长度。
     *
     * @param value 文本
     * @return 宽度估算值
     */
    protected static int getEnOrChLength(String value) {
        int chineseCount = 0;
        int englishCount = 0;
        for (int index = 0; index < value.length(); index++) {
            int byteLength = String.valueOf(value.charAt(index)).getBytes(java.nio.charset.StandardCharsets.UTF_8).length;
            if (byteLength > 1) {
                chineseCount++;
            } else {
                englishCount++;
            }
        }
        int chineseWidth = HAN_ZI_SIZE / 2 * chineseCount + 5;
        int englishWidth = englishCount * 8;
        return chineseWidth + englishWidth;
    }
}
