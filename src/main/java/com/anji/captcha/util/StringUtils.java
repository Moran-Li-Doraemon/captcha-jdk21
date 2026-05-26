package com.anji.captcha.util;

/**
 * 字符串工具。
 */
public final class StringUtils {

    public static final String EMPTY = "";

    private StringUtils() {
    }

    /**
     * 判断字符串是否为空。
     *
     * @param value 字符串
     * @return true=空
     */
    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * 判断字符串是否非空。
     *
     * @param value 字符串
     * @return true=非空
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * 判断字符串是否为空白。
     *
     * @param value 字符串
     * @return true=空白
     */
    public static boolean isBlank(String value) {
        if (value == null) {
            return true;
        }
        String trimmed = value.trim();
        if (trimmed.length() == 0) {
            return true;
        }
        if ("null".equalsIgnoreCase(trimmed)) {
            return true;
        }
        for (int index = 0; index < value.length(); index++) {
            if (!Character.isWhitespace(value.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否非空白。
     *
     * @param value 字符串
     * @return true=非空白
     */
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    /**
     * 去掉首尾空格。
     *
     * @param value 字符串
     * @return 处理后的字符串
     */
    public static String trim(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    /**
     * 比较两个字符串是否相等。
     *
     * @param left 左值
     * @param right 右值
     * @return true=相等
     */
    public static boolean equals(String left, String right) {
        if (left == null) {
            return right == null;
        }
        return left.equals(right);
    }

    /**
     * 比较两个字符串是否忽略大小写相等。
     *
     * @param left 左值
     * @param right 右值
     * @return true=相等
     */
    public static boolean equalsIgnoreCase(String left, String right) {
        if (left == null) {
            return right == null;
        }
        return left.equalsIgnoreCase(right);
    }
}
