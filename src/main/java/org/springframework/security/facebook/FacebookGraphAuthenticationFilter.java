package org.springframework.security.facebook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author JCruz
 * @since <pre>07/27/2010</pre>
 */
public class FacebookGraphAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter implements
		ApplicationContextAware {

	public static final String DEFAULT_FILTER_PROCESS_URL = "/j_spring_facebook_security_check";
    private FacebookHelper facebookHelper = null ;
	private ApplicationContext ctx;

//    private String FACEBOOKCOOKIEPREFIX = "fbs_";
	

    protected FacebookGraphAuthenticationFilter() {
		super(DEFAULT_FILTER_PROCESS_URL);
	}

	public Authentication attemptAuthentication(HttpServletRequest req,
			HttpServletResponse res) throws AuthenticationException,
			IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

        String fbGraphCode = req.getParameter(FacebookGraphParameters.FACEBOOK_GRAPH_PARAMETER_CODE);


        FacebookGraphAuthenticationToken token = new FacebookGraphAuthenticationToken();
        token.setGraphCode(fbGraphCode);

        token.setDetails(authenticationDetailsSource.buildDetails(request));

		AuthenticationManager authenticationManager = getAuthenticationManager();
		Authentication authentication = authenticationManager
				.authenticate(token);

		return authentication;
	}

    

//    private Cookie getFacebookCookie(HttpServletRequest request) {
//        for (Cookie cookie : request.getCookies()) {
//            if(cookie.getName() != null && cookie.getName().equalsIgnoreCase(FACEBOOKCOOKIEPREFIX + applicationID)){
//                return cookie;
//            }
//        }
//        return null;
//    }

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