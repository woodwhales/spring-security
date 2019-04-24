package org.woodwhales.core.validate.code.enums;

import org.woodwhales.core.constants.SecurityConstants;

public enum ValidateCodeType {
	
	SMS {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_SMS;
        }
    },
    IMAGE {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
        }
    };
	
	public abstract String getParamNameOnValidate();
}
