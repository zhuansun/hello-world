package com.zspc.hw.web.handler;

import com.zspc.hw.common.constant.ExceptionConstant;
import com.zspc.hw.common.enums.BusinessExceptionEnums;
import com.zspc.hw.common.exception.BusinessException;
import com.zspc.hw.web.controller.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常统一处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public Result handleLocalizationException(BusinessException e) {
        log.info("业务异常:code=[{}],msg=[{}]", e.getCode(), e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("服务发生异常", e);
        return Result.failure(ExceptionConstant.SYSTEM_ERROR, BusinessExceptionEnums.SYSTEM_ERROR.getErrorMessage());
    }


    /**
     * 当客户端提交的参数是一个数组或集合类型时，Spring MVC 默认支持 256，扩大这个参数，可以接收更多参数
     * mvc数据绑定
     * 需要基于@ControllerAdvice注解下
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        //数组传参最大为256个修复
        webDataBinder.setAutoGrowCollectionLimit(2000);
    }
}
