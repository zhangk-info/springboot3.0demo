package com.xlj.system.exception;

import com.xlj.common.exception.ServiceException;

/**
 * 验证码失效异常类
 * 
 * @author ruoyi
 */
public class CaptchaExpireException extends ServiceException
{
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException()
    {
        super("user.jcaptcha.expire");
    }
}
