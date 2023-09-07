package com.zspc.hw.common.enums;

public enum ExceptionLevelEnum {

    /**
     * 错误
     */
    ERROR(1, "错误"),

    /**
     * debug
     */
    DEBUG(2, "debug"),

    /**
     * 警告
     */
    WARN(3, "警告"),

    /**
     * 普通信息
     */
    INFO(4, "普通信息");

    /**
     * 数据类型
     */
    private Integer code;

    /**
     * 类型描述
     */
    private String  desc;

    ExceptionLevelEnum(Integer type, String desc) {
        this.code = type;
        this.desc = desc;
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static ExceptionLevelEnum fromValue(int v) {
        for (ExceptionLevelEnum c : ExceptionLevelEnum.values()) {
            if (c.getCode() == v) {
                return c;
            }
        }
        return null;
    }

}
