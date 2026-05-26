package com.anji.captcha.model.common;

import com.anji.captcha.util.StringUtils;

import java.io.Serializable;

/**
 * 接口返回封装。
 */
public class ResponseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String repCode;

    private String repMsg;

    private Object repData;

    /**
     * 默认成功响应。
     */
    public ResponseModel() {
        this.repCode = RepCodeEnum.SUCCESS.getCode();
    }

    /**
     * 按返回码初始化响应。
     *
     * @param repCodeEnum 返回码枚举
     */
    public ResponseModel(RepCodeEnum repCodeEnum) {
        setRepCodeEnum(repCodeEnum);
    }

    /**
     * 创建成功响应。
     *
     * @return 成功响应
     */
    public static ResponseModel success() {
        return successMsg("成功");
    }

    /**
     * 创建成功响应。
     *
     * @param msg 成功消息
     * @return 成功响应
     */
    public static ResponseModel successMsg(String msg) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepMsg(msg);
        return responseModel;
    }

    /**
     * 创建成功响应并携带数据。
     *
     * @param data 响应数据
     * @return 成功响应
     */
    public static ResponseModel successData(Object data) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.SUCCESS.getCode());
        responseModel.setRepData(data);
        return responseModel;
    }

    /**
     * 创建错误响应。
     *
     * @param repCodeEnum 返回码枚举
     * @return 错误响应
     */
    public static ResponseModel errorMsg(RepCodeEnum repCodeEnum) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCodeEnum(repCodeEnum);
        return responseModel;
    }

    /**
     * 创建错误响应。
     *
     * @param msg 错误消息
     * @return 错误响应
     */
    public static ResponseModel errorMsg(String msg) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.ERROR.getCode());
        responseModel.setRepMsg(msg);
        return responseModel;
    }

    /**
     * 创建错误响应。
     *
     * @param repCodeEnum 返回码枚举
     * @param msg 错误消息
     * @return 错误响应
     */
    public static ResponseModel errorMsg(RepCodeEnum repCodeEnum, String msg) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(repCodeEnum.getCode());
        responseModel.setRepMsg(msg);
        return responseModel;
    }

    /**
     * 创建异常响应。
     *
     * @param msg 异常消息
     * @return 异常响应
     */
    public static ResponseModel exceptionMsg(String msg) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.EXCEPTION.getCode());
        responseModel.setRepMsg(RepCodeEnum.EXCEPTION.getDesc() + ":" + msg);
        return responseModel;
    }

    /**
     * 是否成功。
     *
     * @return true=成功
     */
    public boolean isSuccess() {
        return StringUtils.equals(repCode, RepCodeEnum.SUCCESS.getCode());
    }

    /**
     * 获取返回码。
     *
     * @return 返回码
     */
    public String getRepCode() {
        return repCode;
    }

    /**
     * 设置返回码。
     *
     * @param repCode 返回码
     */
    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }

    /**
     * 按枚举设置返回码和描述。
     *
     * @param repCodeEnum 返回码枚举
     */
    public void setRepCodeEnum(RepCodeEnum repCodeEnum) {
        this.repCode = repCodeEnum.getCode();
        this.repMsg = repCodeEnum.getDesc();
    }

    /**
     * 获取返回消息。
     *
     * @return 返回消息
     */
    public String getRepMsg() {
        return repMsg;
    }

    /**
     * 设置返回消息。
     *
     * @param repMsg 返回消息
     */
    public void setRepMsg(String repMsg) {
        this.repMsg = repMsg;
    }

    /**
     * 获取返回数据。
     *
     * @return 返回数据
     */
    public Object getRepData() {
        return repData;
    }

    /**
     * 设置返回数据。
     *
     * @param repData 返回数据
     */
    public void setRepData(Object repData) {
        this.repData = repData;
    }

    @Override
    public String toString() {
        return "ResponseModel{repCode='" + repCode + '\'' + ", repMsg='" + repMsg + '\'' + ", repData=" + repData + '}';
    }
}
