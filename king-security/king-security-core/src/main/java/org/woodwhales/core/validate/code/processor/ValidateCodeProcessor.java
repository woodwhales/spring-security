package org.woodwhales.core.validate.code.processor;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeProcessor {

	/**
     * 验证码放入session时的前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    /**
     * 创建验证码
     *
     * @param request Spring工具类,封装请求和响应(request,response)
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验验证码
     *
     * @param request
     */
    void validate(ServletWebRequest request);
}
