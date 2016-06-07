package org.springframework.security.facebook;

import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author JCruz
 * @since <pre>07/27/2010</pre>
 */
public class FacebookAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private Long uid;
    private String sessionkey = null ;

	public FacebookAuthenticationToken() {
		this(null, null);
	}

	public FacebookAuthenticationToken(Long uid) {
		this(uid, null);
	}

	public FacebookAuthenticationToken(Long uid,
			List<GrantedAuthority> authorities) {
		super(authorities);
		this.uid = uid;
		super.setAuthenticated(true);
	}

	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		throw new IllegalArgumentException(
				"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
	}

	public Object getCredentials() {
		return String.valueOf(uid);
	}

	public Object getPrincipal() {
		return null;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getUid() {
		return uid;
	}

    public String getSessionkey() {
        return sessionkey;
    }

    public void setSessionkey(String sessionkey) {
        this.sessionkey = sessionkey;
    }
}