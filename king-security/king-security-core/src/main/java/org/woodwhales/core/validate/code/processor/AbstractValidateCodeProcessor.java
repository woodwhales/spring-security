package org.woodwhales.core.validate.code.processor;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.woodwhales.core.validate.code.ValidateCode;
import org.woodwhales.core.validate.code.enums.ValidateCodeType;
import org.woodwhales.core.validate.code.exception.ValidateCodeException;
import org.woodwhales.core.validate.code.gennerator.ValidateCodeGenerator;

public abstract class AbstractValidateCodeProcessor<T extends ValidateCode> implements ValidateCodeProcessor {
    // 操作session的工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    // 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;

    @Override
    public void create(ServletWebRequest request) throws Exception {
        // 生成验证码
        T validateCode = generate(request);
        // 保存验证码
        save(request, validateCode);
        // 发送验证码
        send(request, validateCode);
    }

	@Override
	@SuppressWarnings("unchecked")
    public void validate(ServletWebRequest request) {
        ValidateCodeType codeType = getValidateCodeType(request);
        String sessionKey = getSessionKey(request);
        T codeInSession = (T)sessionStrategy.getAttribute(request, sessionKey);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    codeType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取难码的值失败");
        }
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(codeType + "验证码的值不能为空");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException(codeType + "验证码不存在");
        }
        if (codeInSession.isExpried()) {
            throw new ValidateCodeException(codeType + "验证码已过期");
        }
        if (!StringUtils.equals(codeInRequest, codeInSession.getCode())) {
            throw new ValidateCodeException(codeType + "验证码不匹配");
        }
        sessionStrategy.removeAttribute(request, sessionKey);
    }

    // 生成验证码
    @SuppressWarnings("unchecked")
	private T generate(ServletWebRequest request) {
        // 验证码的类型,判断是图片验证码还是短信验证码
        String type = getValidateCodeType(request).toString().toLowerCase();
        // 生成器的名字
        String generatorName = type + ValidateCodeGenerator.class.getSimpleName();
        // 生成器
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
        // 验证生成器是否存在
        if (validateCodeGenerator == null) {
            throw new ValidateCodeException("验证码生成器" + generatorName + "不存在");
        }
        // 返回验证码
        return (T)validateCodeGenerator.generate(request);
    }

    // 保存验证码
    private void save(ServletWebRequest request, T validateCode) {
        sessionStrategy.setAttribute(request, getSessionKey(request), validateCode);
    }

    // 发送验证码,由子类实现
    protected abstract void send(ServletWebRequest request, T validateCode) throws Exception;

    // 根据请求的url获取验证码的类型
    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    // 返回验证码放入session时的key
    private String getSessionKey(ServletWebRequest request) {
        return StringUtils.join(SESSION_KEY_PREFIX, getValidateCodeType(request).toString().toUpperCase());
    }
}
