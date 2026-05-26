package com.anji.captcha.model.common;

/**
 * 验证码底图类型。
 */
public enum CaptchaBaseMapEnum {
    ROTATE("ROTATE", "旋转拼图底图"),
    ROTATE_BLOCK("ROTATE_BLOCK", "旋转拼图旋转块底图"),
    ORIGINAL("ORIGINAL", "滑动拼图底图"),
    SLIDING_BLOCK("SLIDING_BLOCK", "滑动拼图滑块底图"),
    PIC_CLICK("PIC_CLICK", "文字点选底图");

    private final String codeValue;
    private final String codeDesc;

    CaptchaBaseMapEnum(String codeValue, String codeDesc) {
        this.codeValue = codeValue;
        this.codeDesc = codeDesc;
    }

    /**
     * 获取编码值。
     *
     * @return 编码值
     */
    public String getCodeValue() {
        return codeValue;
    }

    /**
     * 获取描述。
     *
     * @return 描述
     */
    public String getCodeDesc() {
        return codeDesc;
    }
}
