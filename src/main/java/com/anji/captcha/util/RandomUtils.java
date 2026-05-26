package com.anji.captcha.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具。
 */
public final class RandomUtils {

    private static final String RANDOM_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private RandomUtils() {
    }

    /**
     * 生成无横杠 UUID。
     *
     * @return UUID 字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 从字符池中随机取一个字符。
     *
     * @param source 字符池
     * @return 随机字符
     */
    public static String getRandomHan(String source) {
        return String.valueOf(source.charAt(new java.util.Random().nextInt(source.length())));
    }

    /**
     * 获取随机整数。
     *
     * @param bound 上界
     * @return 随机整数
     */
    public static int getRandomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    /**
     * 获取随机整数。
     *
     * @param startInclusive 起始值
     * @param endExclusive 结束值
     * @return 随机整数
     */
    public static Integer getRandomInt(int startInclusive, int endExclusive) {
        return Integer.valueOf(ThreadLocalRandom.current().nextInt(startInclusive, endExclusive));
    }

    /**
     * 获取随机字符串。
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < length; index++) {
            builder.append(RANDOM_CHARS.charAt(ThreadLocalRandom.current().nextInt(RANDOM_CHARS.length())));
        }
        return builder.toString();
    }
}
