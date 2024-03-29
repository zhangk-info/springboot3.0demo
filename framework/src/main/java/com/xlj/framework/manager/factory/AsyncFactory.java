package com.xlj.framework.manager.factory;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.xlj.common.constants.Constants;
import com.xlj.common.spring.SpringUtils;
import com.xlj.common.utils.LogUtils;
import com.xlj.common.utils.ServletUtils;
import com.xlj.common.utils.ip.IpUtils;
import com.xlj.system.domain.entity.SysLogininfor;
import com.xlj.system.domain.entity.SysOperLog;
import com.xlj.system.service.ISysLogininforService;
import com.xlj.system.service.ISysOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
@Slf4j
public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
                                             final Object... args) {
        UserAgent userAgent = null;
        String ip = null;
        try {
            userAgent = UserAgentUtil.parse(ServletUtils.getRequest().getHeader("User-Agent"));
            ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        } catch (Exception e) {
            log.debug("记录登录信息时捕获异常：" + e.getMessage());
        }
        String finalIp = ip;
        UserAgent finalUserAgent = userAgent;
        return new TimerTask() {
            @Override
            public void run() {
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(finalIp));
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                if (Objects.nonNull(finalUserAgent)) {
                    // 获取客户端操作系统
                    String os = finalUserAgent.getOs().getName();
                    // 获取客户端浏览器
                    String browser = finalUserAgent.getBrowser().getName();
                    logininfor.setBrowser(browser);
                    logininfor.setOs(os);
                }
                logininfor.setUserName(username);
                logininfor.setIpaddr(finalIp);
                logininfor.setLoginLocation(finalIp);
                logininfor.setMsg(message.length() > 255 ? message.substring(0, 254) : message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    logininfor.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
//                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }
}
