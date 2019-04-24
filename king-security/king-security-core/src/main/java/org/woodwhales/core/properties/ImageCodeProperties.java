package org.woodwhales.core.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ImageCodeProperties extends SmsCodeProperties {
	private int width = 60;
    private int height = 23;
    
    public ImageCodeProperties() {
        setLength(4); // 图形验证码长度默认4
    }
    
    
}
