package org.springframework.security.facebook;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 *
 * @author JCruz
 * @since <pre>07/27/2010</pre>
 */
public class FacebookAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter implements
		ApplicationContextAware {

	public static final String DEFAULT_FILTER_PROCESS_URL = "/j_spring_facebook_security_check";
    private FacebookHelper facebookHelper = null ;
	private ApplicationContext ctx;
    protected FacebookAuthenticationFilter() {
		super(DEFAULT_FILTER_PROCESS_URL);
	}

	public Authentication attemptAuthentication(HttpServletRequest req,
			HttpServletResponse res) throws AuthenticationException,
			IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;


        if(facebookHelper == null){
            facebookHelper = (FacebookHelper) ctx
                    .getBean("facebookHelper");
        }

		Long uid = null;
        String sessionkey = null ;
		try {
			uid = facebookHelper.getLoggedInUserId(request, response);
            sessionkey = facebookHelper.lookupSessionKey(request);
		} catch (FacebookUserNotConnected e) {
			throw new AuthenticationCredentialsNotFoundException(
					"Facebook user not connected", e);
		}

		FacebookAuthenticationToken token = new FacebookAuthenticationToken(uid);
        token.setSessionkey(sessionkey);
        token.setDetails(authenticationDetailsSource.buildDetails(request));

		AuthenticationManager authenticationManager = getAuthenticationManager();
		Authentication authentication = authenticationManager
				.authenticate(token);

		return authentication;
	}

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.ctx = ctx;
	}

    public FacebookHelper getFacebookHelper() {
        return facebookHelper;
    }

    public void setFacebookHelper(FacebookHelper facebookHelper) {
        this.facebookHelper = facebookHelper;
    }

}