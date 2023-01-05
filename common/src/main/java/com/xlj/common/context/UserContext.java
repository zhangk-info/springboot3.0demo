package com.xlj.common.context;

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

    public static String getName() {
        return Objects.isNull(get()) ? "" : get().getName();
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
