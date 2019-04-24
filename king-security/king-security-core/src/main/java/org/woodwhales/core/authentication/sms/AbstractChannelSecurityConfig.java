package org.woodwhales.core.authentication.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.woodwhales.core.constants.SecurityConstants;

public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    protected AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler authenticationFailureHandler;

    protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)                      // 登录页面回调
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)        // 自定义的登录接口
                .successHandler(authenticationSuccessHandler)                                 // 认证成功回调
                .failureHandler(authenticationFailureHandler);                                // 认证失败回调
    }
}
