package com.xlj.common.context;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * <p>
 * 用户上下文
 * </p>
 *
 * @since 2019-08-14
 */
public final class UserContext {

    private static final ThreadLocal<CurrentUser> USER_LOCAL = new ThreadLocal<>();

    private UserContext() {
    }

    public static void set(CurrentUser user) {
        USER_LOCAL.set(user);
    }

    public static CurrentUser get() {
        return USER_LOCAL.get();
    }

    public static Long getId() {
        return Objects.isNull(get()) ? null : get().getUserId();
    }

    public static String getUsername() {
        return Objects.isNull(get()) ? null : get().getUsername();
    }

    /**
     * 是否超级管理员
     *
     * @return 是否超级管理员
     */
    public static boolean isSuperAdmin() {
        return getId() != null && 1L == getId();
    }

    /**
     * 是否后台用户
     *
     * @return 是否后台用户
     */
    public static boolean isSysUser() {
        return Objects.nonNull(get().getSysUser()) && get().getSysUser();
    }

}
