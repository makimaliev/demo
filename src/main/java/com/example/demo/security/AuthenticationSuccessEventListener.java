package com.example.demo.security;

import com.example.demo.service.LoginAttemptService;
import com.example.demo.service.LogoutService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationSuccessEventListener implements
        ApplicationListener<AuthenticationSuccessEvent> {

    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;
    private final LogoutService logoutService;

    public AuthenticationSuccessEventListener(HttpServletRequest request, LoginAttemptService loginAttemptService, LogoutService logoutService) {
        this.request = request;
        this.loginAttemptService = loginAttemptService;
        this.logoutService = logoutService;
    }

    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent e) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            loginAttemptService.loginSucceeded(request.getRemoteAddr());
            logoutService.loginSucceeded(request.getRemoteAddr());
        } else {
            loginAttemptService.loginSucceeded(xfHeader.split(",")[0]);
            logoutService.loginSucceeded(xfHeader.split(",")[0]);
        }
    }
}
