package com.anji.captcha.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64 工具。
 */
public abstract class Base64Utils {

    private static final java.nio.charset.Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Base64 编码。
     *
     * @param source 原始字节
     * @return 编码后的字节
     */
    public static byte[] encode(byte[] source) {
        if (source.length == 0) {
            return source;
        }
        return Base64.getEncoder().encode(source);
    }

    /**
     * Base64 解码。
     *
     * @param source 编码字节
     * @return 解码后的字节
     */
    public static byte[] decode(byte[] source) {
        if (source.length == 0) {
            return source;
        }
        return Base64.getDecoder().decode(source);
    }

    /**
     * Base64 编码成字符串。
     *
     * @param source 原始字节
     * @return Base64 字符串
     */
    public static String encodeToString(byte[] source) {
        if (source.length == 0) {
            return "";
        }
        return new String(encode(source), DEFAULT_CHARSET);
    }

    /**
     * Base64 字符串解码。
     *
     * @param source Base64 字符串
     * @return 原始字节
     */
    public static byte[] decodeFromString(String source) {
        if (source == null || source.isEmpty()) {
            return new byte[0];
        }
        return decode(source.getBytes(DEFAULT_CHARSET));
    }
}
