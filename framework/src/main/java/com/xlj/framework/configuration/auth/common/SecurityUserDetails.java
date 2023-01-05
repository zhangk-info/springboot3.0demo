package com.xlj.framework.configuration.auth.common;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 实现UserDetails并写入自定义属性
 */
@Data
public class SecurityUserDetails implements UserDetails {

    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 认证信息
     */
    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean enabled;
    private boolean credentialsNonExpired;

    /**
     * 认证客户端ID
     */
    private String clientId;
    /**
     * 认证中心域,适用于区分多用户源,多认证中心域
     * 没用到
     */
    private String domain;

    /**
     * 自定义属性
     */
    private String nickName;

}
