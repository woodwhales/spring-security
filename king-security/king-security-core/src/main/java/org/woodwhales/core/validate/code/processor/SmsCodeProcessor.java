package org.woodwhales.core.validate.code.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.woodwhales.core.constants.SecurityConstants;
import org.woodwhales.core.sender.SmsCodeSender;
import org.woodwhales.core.validate.code.ValidateCode;


@Component("smsValidateCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {
    
	// 短信验证码发送器
    @Autowired
    private SmsCodeSender smsCodeSender;

    // 发送短信
    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        String paramName = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), paramName);
        smsCodeSender.send(mobile, validateCode.getCode());
    }
}
