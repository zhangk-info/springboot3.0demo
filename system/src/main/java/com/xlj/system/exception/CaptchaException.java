package com.xlj.system.exception;

import com.xlj.common.exception.ServiceException;

/**
 * 验证码错误异常类
 *
 * @author ruoyi
 */
public class CaptchaException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public CaptchaException() {
        super("user.jcaptcha.error", null);
    }
}
