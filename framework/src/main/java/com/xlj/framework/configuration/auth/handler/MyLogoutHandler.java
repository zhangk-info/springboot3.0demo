package com.xlj.framework.configuration.auth.handler;

import cn.hutool.json.JSONUtil;
import com.xlj.common.constants.CacheConstants;
import com.xlj.common.constants.Constants;
import com.xlj.common.entity.DataResp;
import com.xlj.common.exception.ErrorCode;
import com.xlj.common.exception.ServiceException;
import com.xlj.common.spring.SpringUtils;
import com.xlj.common.utils.ServletUtils;
import com.xlj.framework.manager.AsyncManager;
import com.xlj.framework.manager.factory.AsyncFactory;
import com.xlj.system.configuration.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class MyLogoutHandler implements LogoutHandler {

    @Autowired
    private RedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authToken = request.getHeader("Authorization");
        if (null == authToken) {
            authToken = request.getParameter("t");
        }
        if (null == authToken) {
            throw new ServiceException(ErrorCode.NO_API_ACCESS_POWER);
        }
        String token = StringUtils.substringAfter(authToken, "Bearer ").trim();
        String beforeToken = (String) redisService.get(CacheConstants.LOGIN_TOKEN_KEY + token);
        if (StringUtils.isNotBlank(beforeToken)) {
            Map<String, Object> claims = ((JwtDecoder) SpringUtils.getBean("jwtDecoder")).decode(beforeToken).getClaims();
            // 如果包含user字段，那么是LoginUser
            Long userId = (Long) claims.get("userId");
            // 从redis删除
            redisService.del(CacheConstants.LOGIN_TOKEN_KEY + token);
            String userName = (String) claims.get("username");
            log.info(userName + "退出成功");
            // 如果包含user字段，那么是LoginUser 记录日志
            if (claims.containsKey("user")) {
                // 记录用户退出日志
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
                ServletUtils.renderString(response, JSONUtil.toJsonStr(DataResp.success("退出成功")));
                redisService.del(CacheConstants.USER_TOKEN + userId + ":" + token);
            } else {
                redisService.del(CacheConstants.USER_TOKEN + "sys:" + userId + ":" + token);
            }
        }
    }
}