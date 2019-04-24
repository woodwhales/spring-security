package org.woodwhales.core.validate.code.gennerator;

import org.springframework.web.context.request.ServletWebRequest;
import org.woodwhales.core.validate.code.ValidateCode;


public interface ValidateCodeGenerator {
	
    ValidateCode generate(ServletWebRequest request);

}
