package com.xlj.framework.captch;

import com.xlj.common.constants.CacheConstants;
import com.xlj.common.constants.Constants;
import com.xlj.common.utils.MessageUtils;
import com.xlj.framework.manager.AsyncManager;
import com.xlj.framework.manager.factory.AsyncFactory;
import com.xlj.system.configuration.RedisService;
import com.xlj.system.exception.CaptchaException;
import com.xlj.system.exception.CaptchaExpireException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaptchService {

    @Autowired
    private RedisService redisCache;

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + (StringUtils.isBlank(uuid) ? uuid : "");
        String captcha = (String) redisCache.get(verifyKey);
        redisCache.del(verifyKey);
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }
}
