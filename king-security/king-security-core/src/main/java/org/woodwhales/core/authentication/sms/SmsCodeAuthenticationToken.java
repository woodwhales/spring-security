package org.woodwhales.core.authentication.sms;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private final Object principal;

	public SmsCodeAuthenticationToken(Object mobile) {
		super(null);
		this.principal = mobile;
		setAuthenticated(false);
	}

	public SmsCodeAuthenticationToken(Object mobile, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = mobile;
		super.setAuthenticated(true); // must use super, as we override
	}

	public Object getPrincipal() {
		return this.principal;
	}
	
	public Object getCredentials() {
		return null;
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}

		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
	}

}
