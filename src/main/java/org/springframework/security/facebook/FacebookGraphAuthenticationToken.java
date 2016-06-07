package org.springframework.security.facebook;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

/**
 *
 * @author JCruz
 * @since <pre>07/27/2010</pre>
 */
public class FacebookGraphAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;

    private Long uid;
    private String accessToken = null;
    private Map<String, Object> facebookParams = null;

    private String graphCode = null;

    public FacebookGraphAuthenticationToken() {
        this(null, null);
    }

    public FacebookGraphAuthenticationToken(Long uid) {
        this(uid, null);
    }

    public FacebookGraphAuthenticationToken(Long uid,
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

    public Long getUid() {
        //parse uid from accessToken
        if (uid == null) {
            uid = parseUIDFromAccessToken(accessToken);
        }
        
        return uid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long parseUIDFromAccessToken(String accessToken) {
        try {
            if(!accessToken.contains("|")){
                return null;
            }
            String uidFragment = accessToken.split("\\|")[1];
            String uidString = uidFragment.split("-")[1];
            Long uidValue = Long.parseLong(uidString);
            return uidValue;
        }
        catch (NullPointerException e) {
            System.out.println("Unable to parse access Token AccessToken: " + accessToken);
        }
        return null;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Map<String, Object> getFacebookParams() {
        return facebookParams;
    }

    public void setFacebookParams(Map<String, Object> facebookParams) {
        if(uid == null && facebookParams.containsKey("id")){
            uid =  Long.parseLong((String) facebookParams.get("id"));
        }
        this.facebookParams = facebookParams;
    }

    public String getGraphCode() {
        return graphCode;
    }

    public void setGraphCode(String graphCode) {
        this.graphCode = graphCode;
    }
}
