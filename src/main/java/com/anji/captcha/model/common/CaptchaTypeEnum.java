package com.anji.captcha.model.common;

/**
 * 验证码类型。
 */
public enum CaptchaTypeEnum {
    ROTATEPUZZLE("rotatePuzzle", "旋转拼图"),
    BLOCKPUZZLE("blockPuzzle", "滑块拼图"),
    CLICKWORD("clickWord", "文字点选"),
    DEFAULT("default", "默认");

    private final String codeValue;
    private final String codeDesc;

    CaptchaTypeEnum(String codeValue, String codeDesc) {
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
