package org.springframework.security.facebook;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookXmlRestClient;
import com.google.code.facebookapi.IFacebookRestClient;
import com.google.code.facebookapi.ProfileField;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author JCruz
 * @since <pre>07/27/2010</pre>
 */
public class FacebookAuthenticationProviderHelper {
    private Set<ProfileField> userInfoFields;
    private Set<String> stringInfoFields;
    private String apiKey = null;
    private String secret = null;


    public Map<String, String> getUserInfo(Long uid, String sessionKey) throws FacebookUserNotConnected, FacebookException {


        Map<String, String> userInfo = new HashMap<String, String>();


        String queryFields = null;
        if ((getUserInfoFields() != null && getUserInfoFields().size() > 0) || (getStringInfoFields() != null && getStringInfoFields().size() > 0)) {
            queryFields = constructRequestString();
        } else {
            queryFields = "uid, first_name, last_name, proxied_email";
        }

        Document userInfoDoc = query(sessionKey, "select " + queryFields + " from user where uid in " + uid);

        for (ProfileField pfield : ProfileField.values()) {
            NodeList nodes = userInfoDoc.getElementsByTagName(pfield
                    .fieldName());
            if (nodes != null && nodes.getLength() > 0) {
                userInfo
                        .put(pfield.fieldName(), nodes.item(0).getTextContent());
            }
        }
        for (String fieldName : stringInfoFields) {
            NodeList nodes = userInfoDoc.getElementsByTagName(fieldName);
            userInfo.put(fieldName, nodes.item(0).getTextContent());    
        }

        return userInfo;
    }

    private String constructRequestString() {
        StringBuffer stringBufferQueryFields = new StringBuffer();
        for (ProfileField userInfoField : getUserInfoFields()) {
            if (stringBufferQueryFields.length() > 0) stringBufferQueryFields.append(',');
            stringBufferQueryFields.append(userInfoField.fieldName());
        }
        for (String stringInfoField : getStringInfoFields()) {
            if (stringBufferQueryFields.length() > 0) stringBufferQueryFields.append(',');
            stringBufferQueryFields.append(stringInfoField);
        }
        return stringBufferQueryFields.toString();
    }

    protected IFacebookRestClient<Document> getFacebookClient(String sessionKey) throws FacebookUserNotConnected {
        return new FacebookXmlRestClient(apiKey, secret, sessionKey);
    }


    protected Document query(String sessionKey, String fql)
            throws FacebookUserNotConnected, FacebookException {

		IFacebookRestClient<Document> client = getFacebookClient(sessionKey);

		Document document = null;
		document = client.fql_query(fql);
		

		return document;
	}

    public Set<ProfileField> getUserInfoFields() {
        return userInfoFields;
    }

    public void setUserInfoFields(Set<ProfileField> userInfoFields) {
        this.userInfoFields = userInfoFields;
    }

    public Set<String> getStringInfoFields() {
        return stringInfoFields;
    }

    public void setStringInfoFields(Set<String> stringInfoFields) {
        this.stringInfoFields = stringInfoFields;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
