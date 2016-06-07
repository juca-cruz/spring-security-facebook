package org.springframework.security.facebook;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author JCruz
 * @since <pre>07/27/2010</pre>
 */
public class FacebookUserNotConnected extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public FacebookUserNotConnected() {
		super("Unknown Error");
	}

	public FacebookUserNotConnected(String msg) {
		super(msg);
	}

	public FacebookUserNotConnected(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
