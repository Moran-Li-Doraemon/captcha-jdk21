package com.anji.captcha.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES 工具。
 */
public final class AESUtil {

    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    private AESUtil() {
    }

    /**
     * 生成 AES 密钥。
     *
     * @return 16 位随机密钥
     */
    public static String getKey() {
        return RandomUtils.getRandomString(16);
    }

    /**
     * 将二进制转为指定进制字符串。
     *
     * @param bytes 字节数组
     * @param radix 进制
     * @return 字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);
    }

    /**
     * Base64 编码。
     *
     * @param bytes 字节数组
     * @return Base64 字符串
     */
    public static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Base64 解码。
     *
     * @param value Base64 字符串
     * @return 字节数组
     * @throws Exception 解码失败时抛出
     */
    public static byte[] base64Decode(String value) throws Exception {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return Base64.getDecoder().decode(value);
    }

    /**
     * AES 加密为字节数组。
     *
     * @param content 明文
     * @param key 密钥
     * @return 密文字节
     * @throws Exception 加密失败时抛出
     */
    public static byte[] aesEncryptToBytes(String content, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * AES 加密。
     *
     * @param content 明文
     * @param key 密钥
     * @return 密文
     * @throws Exception 加密失败时抛出
     */
    public static String aesEncrypt(String content, String key) throws Exception {
        if (StringUtils.isBlank(key)) {
            return content;
        }
        return base64Encode(aesEncryptToBytes(content, key));
    }

    /**
     * AES 解密。
     *
     * @param bytes 密文字节
     * @param key 密钥
     * @return 明文
     * @throws Exception 解密失败时抛出
     */
    public static String aesDecryptByBytes(byte[] bytes, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
        byte[] result = cipher.doFinal(bytes);
        return new String(result);
    }

    /**
     * AES 解密。
     *
     * @param content 密文
     * @param key 密钥
     * @return 明文
     * @throws Exception 解密失败时抛出
     */
    public static String aesDecrypt(String content, String key) throws Exception {
        if (StringUtils.isBlank(key)) {
            return content;
        }
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        return aesDecryptByBytes(base64Decode(content), key);
    }
}
