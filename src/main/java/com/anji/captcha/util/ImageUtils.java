package com.anji.captcha.util;

import com.anji.captcha.model.common.CaptchaBaseMapEnum;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;

/**
 * 图片缓存与转换工具。
 */
public final class ImageUtils {

    private static final Map<String, String> ORIGINAL_ROTATE_CACHE_MAP = new ConcurrentHashMap<String, String>();

    private static final Map<String, String> ROTATE_BLOCK_CACHE_MAP = new ConcurrentHashMap<String, String>();

    private static final Map<String, String> ORIGINAL_CACHE_MAP = new ConcurrentHashMap<String, String>();

    private static final Map<String, String> SLIDING_BLOCK_CACHE_MAP = new ConcurrentHashMap<String, String>();

    private static final Map<String, String> PIC_CLICK_CACHE_MAP = new ConcurrentHashMap<String, String>();

    private static final Map<String, String[]> FILE_NAME_MAP = new ConcurrentHashMap<String, String[]>();

    private ImageUtils() {
    }

    /**
     * 按目录缓存图片。
     *
     * @param jigsawPath 拼图目录
     * @param picClickPath 点选目录
     * @param rotatePath 旋转目录
     */
    public static void cacheImage(String jigsawPath, String picClickPath, String rotatePath) {
        if (StringUtils.isBlank(rotatePath)) {
            ORIGINAL_ROTATE_CACHE_MAP.putAll(loadImageMapFromDirectory(new File("defaultImages/rotate/original")));
            ROTATE_BLOCK_CACHE_MAP.putAll(loadImageMapFromDirectory(new File("defaultImages/rotate/rotateBlock")));
        } else {
            ORIGINAL_ROTATE_CACHE_MAP.putAll(loadImageMapFromDirectory(new File(rotatePath, "original")));
            ROTATE_BLOCK_CACHE_MAP.putAll(loadImageMapFromDirectory(new File(rotatePath, "rotateBlock")));
        }
        if (StringUtils.isBlank(jigsawPath)) {
            ORIGINAL_CACHE_MAP.putAll(loadImageMapFromDirectory(new File("defaultImages/jigsaw/original")));
            SLIDING_BLOCK_CACHE_MAP.putAll(loadImageMapFromDirectory(new File("defaultImages/jigsaw/slidingBlock")));
        } else {
            ORIGINAL_CACHE_MAP.putAll(loadImageMapFromDirectory(new File(jigsawPath, "original")));
            SLIDING_BLOCK_CACHE_MAP.putAll(loadImageMapFromDirectory(new File(jigsawPath, "slidingBlock")));
        }
        if (StringUtils.isBlank(picClickPath)) {
            PIC_CLICK_CACHE_MAP.putAll(loadImageMapFromDirectory(new File("defaultImages/pic-click")));
        } else {
            PIC_CLICK_CACHE_MAP.putAll(loadImageMapFromDirectory(new File(picClickPath)));
        }
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(), ORIGINAL_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(), SLIDING_BLOCK_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(), PIC_CLICK_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.ROTATE.getCodeValue(), ORIGINAL_ROTATE_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.ROTATE_BLOCK.getCodeValue(), ROTATE_BLOCK_CACHE_MAP.keySet().toArray(new String[0]));
    }

    /**
     * 直接写入内存图片缓存。
     *
     * @param originalMap 原始图
     * @param slidingBlockMap 滑块图
     * @param picClickMap 点选图
     */
    public static void cacheBootImage(Map<String, String> originalMap, Map<String, String> slidingBlockMap, Map<String, String> picClickMap) {
        ORIGINAL_CACHE_MAP.putAll(originalMap);
        SLIDING_BLOCK_CACHE_MAP.putAll(slidingBlockMap);
        PIC_CLICK_CACHE_MAP.putAll(picClickMap);
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(), ORIGINAL_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(), SLIDING_BLOCK_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(), PIC_CLICK_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.ROTATE.getCodeValue(), ORIGINAL_ROTATE_CACHE_MAP.keySet().toArray(new String[0]));
        FILE_NAME_MAP.put(CaptchaBaseMapEnum.ROTATE_BLOCK.getCodeValue(), ROTATE_BLOCK_CACHE_MAP.keySet().toArray(new String[0]));
    }

    /**
     * 获取旋转拼图底图。
     *
     * @return 图片
     */
    public static BufferedImage getRotate() {
        return selectImage(ORIGINAL_ROTATE_CACHE_MAP, FILE_NAME_MAP.get(CaptchaBaseMapEnum.ROTATE.getCodeValue()));
    }

    /**
     * 获取旋转拼图块图片。
     *
     * @return Base64 字符串
     */
    public static String getRotateBlock() {
        return selectBase64(ROTATE_BLOCK_CACHE_MAP, FILE_NAME_MAP.get(CaptchaBaseMapEnum.ROTATE_BLOCK.getCodeValue()));
    }

    /**
     * 获取滑动拼图底图。
     *
     * @return 图片
     */
    public static BufferedImage getOriginal() {
        return selectImage(ORIGINAL_CACHE_MAP, FILE_NAME_MAP.get(CaptchaBaseMapEnum.ORIGINAL.getCodeValue()));
    }

    /**
     * 获取滑动拼图滑块图。
     *
     * @return Base64 字符串
     */
    public static String getslidingBlock() {
        return selectBase64(SLIDING_BLOCK_CACHE_MAP, FILE_NAME_MAP.get(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue()));
    }

    /**
     * 获取点选验证码底图。
     *
     * @return 图片
     */
    public static BufferedImage getPicClick() {
        return selectImage(PIC_CLICK_CACHE_MAP, FILE_NAME_MAP.get(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue()));
    }

    /**
     * 将图片转成 Base64。
     *
     * @param image 图片
     * @return Base64 字符串
     */
    public static String getImageToBase64Str(BufferedImage image) {
        try {
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            return Base64Utils.encodeToString(outputStream.toByteArray()).trim();
        } catch (IOException throwable) {
            return "";
        }
    }

    /**
     * Base64 转图片。
     *
     * @param base64 Base64 字符串
     * @return 图片
     */
    public static BufferedImage getBase64StrToImage(String base64) {
        try {
            return ImageIO.read(new ByteArrayInputStream(Base64Utils.decodeFromString(base64)));
        } catch (IOException throwable) {
            return null;
        }
    }

    /**
     * 从目录读取图片并转 Base64。
     *
     * @param directory 图片目录
     * @return 文件名到 Base64 的映射
     */
    public static Map<String, String> loadImageMapFromDirectory(File directory) {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return Collections.emptyMap();
        }
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return Collections.emptyMap();
        }
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File left, File right) {
                return left.getName().compareToIgnoreCase(right.getName());
            }
        });
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (File file : files) {
            if (!file.isFile() || !file.getName().toLowerCase().endsWith(".png")) {
                continue;
            }
            try {
                result.put(file.getName(), Base64Utils.encodeToString(FileCopyUtils.copyToByteArray(new FileInputStream(file))));
            } catch (IOException ignored) {
            }
        }
        return result;
    }

    private static BufferedImage selectImage(Map<String, String> cacheMap, String[] fileNames) {
        String base64 = selectBase64(cacheMap, fileNames);
        if (StringUtils.isBlank(base64)) {
            return null;
        }
        return getBase64StrToImage(base64);
    }

    private static String selectBase64(Map<String, String> cacheMap, String[] fileNames) {
        if (fileNames == null || fileNames.length == 0) {
            return null;
        }
        int index = RandomUtils.getRandomInt(fileNames.length);
        return cacheMap.get(fileNames[index]);
    }
}
