package xc.crabapple.core.file.image.captcha.anji;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import com.anji.captcha.util.ImageUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 点选验证码工具类。
 */
public final class AnjiCaptchaUtil {

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    private static volatile CaptchaService CAPTCHA_SERVICE;

    private AnjiCaptchaUtil() {
    }

    /**
     * 使用默认缓存类型初始化。
     *
     * @param clickWordBgimgPath 点选底图目录
     */
    public static void init(String clickWordBgimgPath) {
        init("local", clickWordBgimgPath);
    }

    /**
     * 初始化点选验证码工具。
     *
     * @param cacheType 缓存类型，目前仅支持 local
     * @param clickWordBgimgPath 点选底图目录，空值时自动生成默认底图
     */
    public static void init(String cacheType, String clickWordBgimgPath) {
        if (!INITIALIZED.compareAndSet(false, true)) {
            return;
        }
        System.setProperty("java.awt.headless", "true");
        Map<String, String> picClickMap = loadPicClickMap(clickWordBgimgPath);
        ImageUtils.cacheBootImage(new HashMap<String, String>(), new HashMap<String, String>(), picClickMap);
        Properties properties = new Properties();
        properties.setProperty("captcha.type", "clickWord");
        properties.setProperty("captcha.cacheType", "local");
        properties.setProperty("captcha.init.original", "true");
        properties.setProperty("captcha.aes.status", "true");
        properties.setProperty("captcha.water.mark", "");
        properties.setProperty("captcha.water.font", "SansSerif");
        properties.setProperty("captcha.font.type", "Dialog");
        properties.setProperty("captcha.font.style", "1");
        properties.setProperty("captcha.font.size", "30");
        properties.setProperty("captcha.word.count", "4");
        properties.setProperty("captcha.cache.number", "1000");
        properties.setProperty("captcha.timing.clear", "60");
        properties.setProperty("captcha.slip.offset", "5");
        properties.setProperty("captcha.interference.options", "0");
        CAPTCHA_SERVICE = CaptchaServiceFactory.getInstance(properties);
    }

    /**
     * 获取验证码。
     *
     * @param captchaVO 请求对象
     * @return 响应对象
     */
    public static ResponseModel get(CaptchaVO captchaVO) {
        ensureInitialized();
        return CAPTCHA_SERVICE.get(captchaVO);
    }

    /**
     * 校验验证码。
     *
     * @param captchaVO 请求对象
     * @return 响应对象
     */
    public static ResponseModel check(CaptchaVO captchaVO) {
        ensureInitialized();
        return CAPTCHA_SERVICE.check(captchaVO);
    }

    /**
     * 二次校验验证码。
     *
     * @param captchaVO 请求对象
     * @return 响应对象
     */
    public static ResponseModel verify(CaptchaVO captchaVO) {
        ensureInitialized();
        return CAPTCHA_SERVICE.verification(captchaVO);
    }

    private static void ensureInitialized() {
        if (CAPTCHA_SERVICE == null) {
            init("local", null);
        }
    }

    private static Map<String, String> loadPicClickMap(String clickWordBgimgPath) {
        File directory = null;
        if (clickWordBgimgPath != null && clickWordBgimgPath.trim().length() > 0) {
            directory = new File(clickWordBgimgPath);
            if (directory.exists() && directory.isDirectory()) {
                Map<String, String> imageMap = ImageUtils.loadImageMapFromDirectory(directory);
                if (!imageMap.isEmpty()) {
                    return imageMap;
                }
            }
        }
        return buildDefaultPicClickMap();
    }

    private static Map<String, String> buildDefaultPicClickMap() {
        Map<String, String> imageMap = new HashMap<String, String>();
        for (int index = 1; index <= 6; index++) {
            BufferedImage image = new BufferedImage(240, 180, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = image.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color background = new Color(210 - index * 8, 225 - index * 4, 240 - index * 2);
            graphics2D.setColor(background);
            graphics2D.fillRect(0, 0, image.getWidth(), image.getHeight());
            graphics2D.setColor(new Color(255 - index * 12, 180 + index * 5, 120 + index * 8));
            graphics2D.fillOval(18 + index * 6, 20 + index * 4, 60, 60);
            graphics2D.setColor(new Color(80 + index * 20, 110 + index * 10, 160 - index * 8));
            graphics2D.fillRoundRect(120, 35 + index * 5, 90, 70, 16, 16);
            graphics2D.setFont(new Font("Dialog", Font.BOLD, 26));
            graphics2D.setColor(Color.DARK_GRAY);
            graphics2D.drawString("pic-click-" + index, 36, 150);
            graphics2D.dispose();
            imageMap.put(index + ".png", ImageUtils.getImageToBase64Str(image));
        }
        return imageMap;
    }
}
