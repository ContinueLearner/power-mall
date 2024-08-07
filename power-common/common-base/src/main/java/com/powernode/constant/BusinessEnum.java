package com.powernode.constant;

public enum BusinessEnum {

    UN_AUTHORIZATION(401,"未授权"),
    OPERATION_FAIL(-1,"操作失败");

    BusinessEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 状态码描述
     */
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
