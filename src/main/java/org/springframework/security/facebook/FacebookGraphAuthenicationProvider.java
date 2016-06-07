package org.springframework.security.facebook;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author JCruz
 * @since <pre>07/27/2010</pre>
 */
public class FacebookGraphAuthenicationProvider implements AuthenticationProvider {
    

    private String facebookGraphAccessTokenUrl = "https://graph.facebook.com/oauth/access_token";
    private String facebookGraphAuthRedirectUrl = "";
    private String applicationID = null;
    private List<String> extendedPermissions = new ArrayList<String>();
    private String redirectURL = "";
    protected String facebookApplicationSecret = "";


    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        FacebookGraphAuthenticationToken fbAuthToken = (FacebookGraphAuthenticationToken) authentication;
        String fbGraphCode = fbAuthToken.getGraphCode();
        String fbAccessToken = getFBAcessToken(fbGraphCode);
        fbAuthToken.setAccessToken(fbAccessToken);


        String graphurl = getGraphRequestURL(fbAuthToken.getAccessToken());
        try {
            String body = HttpUtil.getRequest(graphurl);

            JSONObject jsonObject = new JSONObject(body);
            String[] jsonNames = JSONObject.getNames(jsonObject);
            Map<String, Object> params = new HashMap<String, Object>();
            for (String jsonName : jsonNames) {
                params.put(jsonName, jsonObject.get(jsonName));
            }

            ((FacebookGraphAuthenticationToken) authentication).setFacebookParams(params);
            return authentication;


        } catch (JSONException e) {
            throw new FacebookUserNotConnected(e.getMessage());
        }
    }

    private String getFBAcessToken(String fbGraphCode) throws FacebookUserNotConnected {
        String fbAuthenticationCodeURL = getFacebookAuthenticationCodeUrl(fbGraphCode);
        String body = HttpUtil.getRequest(fbAuthenticationCodeURL);
        if (isJSON(body)) {
            String errorMessage = getJSONErrorMessage(body);
            throw new FacebookUserNotConnected(errorMessage);
        }
        
        String code = body.split("=")[1];
        code = HttpUtil.deocde(code);
        return code;
    }

    private String getJSONErrorMessage(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONObject jsonError = jsonObject.getJSONObject("error");
            String type = jsonError.getString("type");
            String message = jsonError.getString("message");
            return type + ":" + message;
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }

    private boolean isJSON(String body) {
        if (body.charAt(0) == '{') {
            return true;
        } else {
            return false;
        }
    }

    public String getFacebookLoginURL() {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put(FacebookGraphParameters.FACEBOOK_GRAPH_PARAMETER_CLIENT_ID, getApplicationID());
        mParams.put(FacebookGraphParameters.FACEBOOK_GRAPH_PARAMETER_REDIRCT_URL, getRedirectURL());
        if (extendedPermissions.size() > 0) {
            mParams.put(FacebookGraphParameters.FACEBOOK_GRAPH_PARAMETER_SCOPE, getScope());
        }
        String facebookLoginURL = HttpUtil.generateUrl(FacebookGraphParameters.FACEBOOK_GRAPH_OAUTH_URL, mParams);
        return facebookLoginURL;
    }

    private String getFacebookAuthenticationCodeUrl(String fbGraphCode) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put(FacebookGraphParameters.FACEBOOK_GRAPH_PARAMETER_CLIENT_ID, getApplicationID());
        mParams.put(FacebookGraphParameters.FACEBOOK_GRAPH_PARAMETER_REDIRCT_URL, getRedirectURL()               );
        mParams.put( FacebookGraphParameters.FACEBOOK_GRAPH_PARAMETER_CLIENT_SECRET, getFacebookApplicationSecret());
        mParams.put( FacebookGraphParameters.FACEBOOK_GRAPH_PARAMETER_CODE , fbGraphCode);
        String codeAuthTokenSwapUrl = HttpUtil.generateUrl(facebookGraphAccessTokenUrl, mParams);
        return codeAuthTokenSwapUrl;
    }


    public boolean supports(Class<? extends Object> authentication) {
        boolean supports = FacebookGraphAuthenticationToken.class
                .isAssignableFrom(authentication);
        return supports;
    }

    public String getGraphRequestURL(String access_token) {
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put(FacebookGraphParameters.FACEBOOK_GRAPH_PARAMETER_ACCESS_TOKEN, access_token);
        return  HttpUtil.generateUrl(FacebookGraphParameters.FACEBOOK_GRAPH_USER_INFO_ME, mParams);
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public void setExtendedPermissions(List<String> extendedPermissions) {
        this.extendedPermissions = extendedPermissions;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getScope() {
        String scope = "";
        for (String extendedPermission : extendedPermissions) {
            scope += (scope.length() > 0 ? "," : "") + extendedPermission;
        }
        return scope;
    }

    public String getFacebookApplicationSecret() {
        return facebookApplicationSecret;
    }

    public void setFacebookApplicationSecret(String facebookApplicationSecret) {
        this.facebookApplicationSecret = facebookApplicationSecret;
    }
}
