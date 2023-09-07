package com.zspc.hw.common.enums;

import com.zspc.hw.common.constant.ExceptionConstant;

public enum BusinessExceptionEnums {

    SYSTEM_ERROR(ExceptionConstant.SYSTEM_ERROR, "系统繁忙，请稍后重试");

    private final String errorCode;
    private final String errorMessage;


    BusinessExceptionEnums(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
