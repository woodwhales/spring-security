package org.woodwhales.core.validate.code.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.woodwhales.core.constants.SecurityConstants;
import org.woodwhales.core.properties.SecurityProperties;
import org.woodwhales.core.validate.code.enums.ValidateCodeType;
import org.woodwhales.core.validate.code.exception.ValidateCodeException;
import org.woodwhales.core.validate.code.processor.ValidateCodeProcessorHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    // 验证码校验失败处理器
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    // 系统配置信息
    @Autowired
    private SecurityProperties securityProperties;

    // 校验码处理器
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    // 存放需要校验码的url
    private Map<String, ValidateCodeType> urlMap = new HashMap<>();

    // 验证请求url与配置的url是否匹配的工具类
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    // 初始化要拦截的url配置信息
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        // 登录请求默认要校验图片验证码
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
        // 自定义需要校验图片验证码的url
        addUrlToMap(securityProperties.getCode().getImage().getUrl(), ValidateCodeType.IMAGE);

        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
        addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);
    }

    // 将需要验证的url和验证的类型放入map
    protected void addUrlToMap(String urlString, ValidateCodeType validateCodeType) {
        if (StringUtils.isNotBlank(urlString)) {
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                urlMap.put(url, validateCodeType);
            }
        }
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // 获取验证码的类型
        ValidateCodeType validateCodeType = getValidateCodeType(request);

        // 非空就是需要校验
        if (validateCodeType != null) {
            log.info("校验请求(" + request.getRequestURI() + ")中的验证码，验证码类型为" + validateCodeType);
            try {
                validateCodeProcessorHolder.findValidateCodeProcessor(validateCodeType).validate(new ServletWebRequest(request, response));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // 获取校验码的类型
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType validateCodeType = null;
        // get方法不校验
        if (StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            return validateCodeType;
        }
        for (String url : urlMap.keySet()) {
            if (antPathMatcher.match(url, request.getRequestURI())) {
                validateCodeType = urlMap.get(url);
                break;
            }
        }
        return validateCodeType;
    }
}
