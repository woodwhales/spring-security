package org.woodwhales.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "king.security")
public class SecurityProperties {

	private ValidateCodeProperties code = new ValidateCodeProperties();
	
	private BrowserProperties browser = new BrowserProperties();
}
