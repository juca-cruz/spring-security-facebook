package org.springframework.security.facebook;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

/**
 *
 * @author JCruz
 * @since <pre>07/27/2010</pre>
 */
public class FacebookAuthenticationProvider implements AuthenticationProvider {

	private String[] roles;

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		FacebookAuthenticationToken facebookAuthentication = (FacebookAuthenticationToken) authentication;

		if (facebookAuthentication.getUid() == null)
			throw new BadCredentialsException(
					"User not authenticated through facebook");

		if (roles == null) {
			roles = new String[] {};
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : roles) {
			authorities.add(new GrantedAuthorityImpl(role));
		}

		FacebookAuthenticationToken succeedToken = new FacebookAuthenticationToken(
				facebookAuthentication.getUid(), authorities);
		succeedToken.setDetails(authentication.getDetails());

		return succeedToken;
	}

	public boolean supports(Class<? extends Object> authentication) {
		boolean supports = FacebookAuthenticationToken.class
				.isAssignableFrom(authentication);
		return supports;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String[] getRoles() {
		return roles;
	}

}