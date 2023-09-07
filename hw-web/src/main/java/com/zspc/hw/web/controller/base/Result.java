package com.zspc.hw.web.controller.base;

import lombok.Data;

@Data
public class Result<T> {


    public static final String SUCCESS_CODE = "200";
    public static final String FAIL_CODE = "500";
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    /**
     * 返回信息的编码
     */
    private String code = FAILURE;

    /**
     * 返回的结果对象
     */
    private T data;

    /**
     * 正常应答信息
     */
    private String message;
    /**
     * 错误的应答信息
     */
    private String exceptionMessage;

    /**
     * 对应的traceid
     */
    private String traceId = null;

    /**
     * 对应的requestToken
     */
    private String requestToken = null;


    /**
     * 成功返回数据结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>(data);
        result.setCode(SUCCESS_CODE);
        result.setMessage(SUCCESS);
        return result;
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>(null);
        result.setCode(SUCCESS_CODE);
        result.setMessage(SUCCESS);
        return result;
    }

    /**
     * 失败返回错误信息
     */
    public static <T> Result<T> failure(String errorCode) {
        return new Result<T>(errorCode, FAILURE);
    }

    public static <T> Result<T> failure(String errorCode, String errorMessage) {
        return new Result<T>(errorCode, errorMessage);
    }

    public static <T> Result<T> failure() {
        return new Result<T>(FAIL_CODE, FAILURE);
    }


    /**
     * 不能用，请勿使用
     */
    private Result() {
    }

    /**
     * 默认构造成功的返回结果
     */
    private Result(T data) {
        this.code = SUCCESS;
        this.data = data;
        this.message = "";
        //this.traceId = TraceContext.traceId();
        //this.requestToken = String.valueOf(IdWorkerUtil.getId());
    }

    /**
     * 构造其他失败的返回结果
     */
    private Result(String errorCode, String exceptionMessage) {
        this.code = errorCode;
        this.message = exceptionMessage;
        this.exceptionMessage = exceptionMessage;
        //this.traceId = TraceContext.traceId();
        //this.requestToken = String.valueOf(IdWorkerUtil.getId());
    }


}
