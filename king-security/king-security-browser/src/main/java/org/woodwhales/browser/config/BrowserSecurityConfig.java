package org.woodwhales.browser.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.woodwhales.core.authentication.sms.AbstractChannelSecurityConfig;
import org.woodwhales.core.authentication.sms.SmsCodeAuthenticationSecurityConfig;
import org.woodwhales.core.constants.SecurityConstants;
import org.woodwhales.core.properties.SecurityProperties;
import org.woodwhales.core.validate.code.config.ValidateCodeSecurityConfig;

@Component
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    /**
     * 配置TokenRepository
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        applyPasswordAuthenticationConfig(http);

    	http.apply(validateCodeSecurityConfig) // 验证码的校验配置

        // 短信登录认证的配置
        .and()
        .apply(smsCodeAuthenticationSecurityConfig)

        // 记住我的配置
        .and()
        .rememberMe()
        .tokenRepository(persistentTokenRepository())
        .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
        .userDetailsService(userDetailsService)

        // 对需要认证和不需要认证的资源的配置
        .and()
        .authorizeRequests() // 对请求做授权
        // 不需要认证的页面
        .antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                securityProperties.getBrowser().getLoginPage(),
                SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                "/index.html",
                "/")
            .permitAll() 
        .anyRequest() // 任何请求
        .authenticated() // 都需要身份认证

        // 暂时将防护跨站请求伪造的功能置为不可用
        .and()
        .csrf().disable();
    }

}
