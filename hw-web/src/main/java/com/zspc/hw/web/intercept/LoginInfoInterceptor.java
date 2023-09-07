package com.zspc.hw.web.intercept;

import cn.hutool.core.util.StrUtil;
import com.zspc.hw.common.exception.BusinessException;
import com.zspc.hw.common.login.LoginInfo;
import com.zspc.hw.common.login.LoginInfoThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 非映射到方法，直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String requestUrl = request.getRequestURI();
        String userCode = request.getHeader("x-hw-userid");
        String userName = request.getHeader("x-hw-username");
        log.info("LoginInfoInterceptor url {},x-hw-userid {},x-hw-username {}", requestUrl,
                userCode, userName);

        if (StrUtil.isBlank(userCode)) {
            throw BusinessException.get("用户未登录");
        }

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserCode(userCode);
        loginInfo.setUserName(userName);

        LoginInfoThreadLocal.setLoginInfo(loginInfo);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        LoginInfoThreadLocal.remove();
    }
}
