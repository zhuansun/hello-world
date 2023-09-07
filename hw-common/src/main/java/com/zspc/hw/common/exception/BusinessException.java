package com.zspc.hw.common.exception;

import com.zspc.hw.common.constant.ExceptionConstant;
import com.zspc.hw.common.enums.ExceptionLevelEnum;

public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1721373866786099144L;

    private ExceptionLevelEnum level = ExceptionLevelEnum.INFO;

    /**
     * 请勿使用
     */
    private BusinessException() {
        System.out.println("BusinessException.BusinessException()");
    }

    /**
     * 私有构造函数
     */
    private BusinessException(Exception e) {
        super(e);
        this.level = ExceptionLevelEnum.ERROR;
        this.code = ExceptionConstant.SYSTEM_ERROR;
    }

    /**
     * 私有构造函数
     *
     * @param code
     * @param details
     */
    private BusinessException(String code, String error) {
        super(error);
        this.code = code;
    }

    /**
     * 私有构造函数
     *
     * @param e     异常
     * @param code  异常编码
     * @param error 异常信息
     */
    protected BusinessException(Throwable e, String code, String error) {
        super(error, e);
        this.code = code;
    }

    /**
     * 私有构造函数
     *
     * @param e     异常
     * @param code  异常编码
     * @param level 异常等级(指定错误的等级，目前主要为发送异常的邮件使用，只有设定了警告以及以上的级别时才发送，否则都将发送邮件)
     * @param error 异常信息
     */
    protected BusinessException(Throwable e, String code, ExceptionLevelEnum level, String error) {
        super(error, e);
        this.code = code;
        this.level = level;
    }


    public static BusinessException get(String msg) {
        return new BusinessException(ExceptionConstant.SYSTEM_ERROR, msg);
    }

    public static BusinessException get(String code, String msg) {
        return new BusinessException(code, msg);
    }

    /**
     * 获取一个业务异常实例对象，基本用法,将异常堆栈往下传递
     *
     * @param e     异常
     * @param code  异常编码
     * @param level 异常等级 (指定错误的等级，目前主要为发送异常的邮件使用，只有设定了警告以及以上的级别时才发送，否则都将发送邮件)
     * @return {@link BusinessException}
     */
    public static BusinessException get(Throwable e, String code, String error, ExceptionLevelEnum level) {
        if (e instanceof BusinessException) {
            ((BusinessException) e).level = level;
            return (BusinessException) e;
        }
        return new BusinessException(e, code, level, error);
    }

    /**
     * 获取一个业务异常实例对象，可将其他异常封装成业务异常
     *
     * @param e 其他异常{@link Exception}
     * @return {@link BusinessException}
     */
    public static BusinessException get(Exception e) {
        if (e instanceof BusinessException) {
            return (BusinessException) e;
        }
        return new BusinessException(e);
    }

    /**
     * 获取异常编码
     * @return 异常编码 {@link ExceptionConstant}
     */
    public String getCode() {
        return code;
    }

    /* (non-Javadoc)
     * @see java.lang.Throwable#toString()
     */
    @Override
    public String toString() {
        return super.toString() + "-[" + this.getCode() + "]";
    }

    /**
     * @return the level
     */
    public ExceptionLevelEnum getLevel() {
        return level;
    }

    /**
     * 设置等级
     *
     * @param level
     */
    public void setLevel(ExceptionLevelEnum level) {
        this.level = level;
    }
}
