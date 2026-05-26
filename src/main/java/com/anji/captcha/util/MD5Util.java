package com.anji.captcha.util;

import java.security.MessageDigest;

/**
 * MD5 工具。
 */
public abstract class MD5Util {

    private MD5Util() {
    }

    /**
     * 计算 MD5。
     *
     * @param value 原始字符串
     * @return MD5 值
     */
    public static String md5(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(value.getBytes("UTF8"));
            byte[] digest = messageDigest.digest();
            StringBuilder builder = new StringBuilder();
            for (int index = 0; index < digest.length; index++) {
                builder.append(Integer.toHexString(255 & digest[index] | -256).substring(6));
            }
            return builder.toString();
        } catch (Exception throwable) {
            return "";
        }
    }
}
