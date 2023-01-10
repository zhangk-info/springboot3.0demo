package com.xlj.system.controller;

import com.xlj.common.constants.Constants;
import com.xlj.common.entity.AjaxResult;
import com.xlj.common.entity.DataResp;
import com.xlj.system.domain.entity.SysMenu;
import com.xlj.system.domain.entity.SysUser;
import com.xlj.system.domain.model.LoginBody;
import com.xlj.framework.configuration.auth.SecurityUtils;
import com.xlj.system.service.ISysMenuService;
import com.xlj.system.service.SysLoginService;
import com.xlj.system.service.SysPermissionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody, HttpServletRequest request) {
        AjaxResult dataResp = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid(), request);
        dataResp.put(Constants.TOKEN, token);
        return dataResp;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult dataResp = AjaxResult.success();
        dataResp.put("user", user);
        dataResp.put("roles", roles);
        dataResp.put("permissions", permissions);
        return dataResp;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public DataResp getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return DataResp.success(menuService.buildMenus(menus));
    }
}
