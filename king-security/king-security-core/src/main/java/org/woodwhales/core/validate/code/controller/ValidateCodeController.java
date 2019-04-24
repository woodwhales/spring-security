package org.woodwhales.core.validate.code.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.woodwhales.core.validate.code.processor.ValidateCodeProcessor;
import org.woodwhales.core.validate.code.processor.ValidateCodeProcessorHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ValidateCodeController {

	@Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 创建验证码,根据验证码类型不同,调用不同的{@link ValidateCodeProcessor}接口实现
     *
     * @param request
     * @param response
     * @param type image表示返回图片/sms表示发送短信
     * @throws Exception
     */
    @GetMapping("/code/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
    	log.info("request type = {}", type);
        validateCodeProcessorHolder.findValidateCodeProcessor(type).create(new ServletWebRequest(request, response));
    }
}
