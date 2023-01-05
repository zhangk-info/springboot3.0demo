package com.xlj.common.exception;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”
 *
 * @author zhangkun
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -6642202125653148265L;
    private int code = ErrorCode.DEFAULT.code;

    public ServiceException() {
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ServiceException(ErrorCode err) {
        super(err.message);
        this.code = err.code;
    }

    public ServiceException(ErrorCode err, Object... params) {
        super(String.format(err.message, params));
        this.code = err.code;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
