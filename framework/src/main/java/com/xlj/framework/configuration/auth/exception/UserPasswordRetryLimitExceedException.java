package com.xlj.framework.configuration.auth.exception;

import com.xlj.common.exception.ErrorCode;
import com.xlj.common.exception.ServiceException;

/**
 * 用户错误最大次数异常类
 *
 * @author ruoyi
 */
public class UserPasswordRetryLimitExceedException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public UserPasswordRetryLimitExceedException(int retryLimitCount, int lockTime) {
        super(ErrorCode.valueOf(""), retryLimitCount, lockTime);
    }
}
