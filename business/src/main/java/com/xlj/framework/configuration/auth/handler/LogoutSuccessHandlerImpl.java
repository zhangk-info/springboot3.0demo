package com.xlj.framework.configuration.auth.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xlj.common.constants.Constants;
import com.xlj.common.entity.DataResp;
import com.xlj.common.utils.ServletUtils;
import com.xlj.framework.manager.AsyncManager;
import com.xlj.framework.manager.factory.AsyncFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ruoyi
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        JSONObject jsonObject = JSONUtil.parseObj(JSONUtil.toJsonStr(authentication.getPrincipal()));
        String userName = jsonObject.getStr("username");
        // 记录用户退出日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        ServletUtils.renderString(response, JSONUtil.toJsonStr(DataResp.success("退出成功")));
    }
}
