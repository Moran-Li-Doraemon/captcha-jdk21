package com.anji.captcha.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 流拷贝工具。
 */
public abstract class FileCopyUtils {

    public static final int BUFFER_SIZE = 4096;

    private FileCopyUtils() {
    }

    /**
     * 将输入流转成字节数组。
     *
     * @param inputStream 输入流
     * @return 字节数组
     * @throws IOException 读取失败时抛出
     */
    public static byte[] copyToByteArray(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return new byte[0];
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(BUFFER_SIZE);
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int readCount;
            while ((readCount = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readCount);
            }
            return outputStream.toByteArray();
        } finally {
            try {
                inputStream.close();
            } catch (IOException ignored) {
            }
            try {
                outputStream.close();
            } catch (IOException ignored) {
            }
        }
    }
}
