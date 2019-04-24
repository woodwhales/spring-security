package org.woodwhales.core.properties;

import lombok.Data;

@Data
public class SmsCodeProperties {
	
	private int length = 6; // 短信验证码长度默认6
    private int expireIn = 60; // 默认60秒
    private String url; // 多个url时用逗号隔开
    
}
