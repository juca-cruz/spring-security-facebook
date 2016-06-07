package org.springframework.security.facebook;

import java.util.Map;

/**
 *
 * @author JCruz
 * @since <pre>07/27/2010</pre>
 */
public class FacebookUtil {
    public boolean verifySignature(Map<String,String[]> cookieParams, String secret){
        return com.google.code.facebookapi.FacebookSignatureUtil.autoVerifySignature(cookieParams, secret);
    }
}
