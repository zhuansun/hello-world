package com.zspc.hw.common.login;

import java.util.Objects;

public class LoginInfoThreadLocal {
    private static final ThreadLocal<LoginInfo> LoginInfoThreadLocal = new ThreadLocal<>();

    public static void setLoginInfo(LoginInfo loginInfo) {
        LoginInfoThreadLocal.set(loginInfo);
    }

    public static void remove() {
        LoginInfoThreadLocal.remove();
    }

    public static LoginInfo getLoginInfo() {
        return LoginInfoThreadLocal.get();
    }

    public static String getUserCode() {
        LoginInfo loginInfo = LoginInfoThreadLocal.get();
        return Objects.nonNull(loginInfo) ? loginInfo.getUserCode() : null;
    }

    public static String getUserName() {
        LoginInfo loginInfo = LoginInfoThreadLocal.get();
        return Objects.nonNull(loginInfo) ? loginInfo.getUserName() : null;
    }
}
