package com.xlj.common.exception;

/**
 * @author zhangkun
 */

public enum ErrorCode {

    /**
     * -1 默认 自定义异常
     */
    DEFAULT(-1, "default"),

    /*----  权限相关  -----------------------------------------*/
    NO_DATA_ACCESS_POWER(1000, "无数据访问权限"),
    NO_API_ACCESS_POWER(1001, "无接口访问权限"),
    /*----  数据相关  -----------------------------------------*/
    DATA_NOT_EXIST(2000, "%s数据已清除"),
    DATA_EXIST(2001, "数据已存在"),
    PARAMS_ERR(2002, "%s参数错误"),
    SQL_ERROR(2003, "SQL执行错误: %s"),
    /*----  服务相关  -----------------------------------------*/
    SERVICE_TIMEOUT(3000, "当前服务繁忙，请稍后重试"),
    PASSWORD_RETRY_LIT(3001, "user.password.retry.limit.exceed");
    /**
     * code
     */
    public final Integer code;
    /**
     * message
     */
    public final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(this.message, args);
    }

}
