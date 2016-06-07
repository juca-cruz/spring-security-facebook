package facebook;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.junit.Assert;
import org.springframework.security.facebook.FacebookUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author JCruz
 * @since <pre>07/27/2010</pre>
 */
public class FacebookUtilTest extends TestCase {
    public FacebookUtilTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     *
     * Method: verifySignature(Map<String,String[]> cookieParams, String secret)
     *
     */
    public void testFBSigProcess() throws Exception {
        Map<String, String> alphaMap = new LinkedHashMap<String, String>();
    }

    private String getHashableString(Map<String, String> alphaMap, String secret) {
        StringBuffer sb = new StringBuffer();
        for (String key : alphaMap.keySet()) {
            sb.append(key).append('=').append(alphaMap.get(key));
            System.out.println(key + "=" + alphaMap.get(key));
        }
        sb.append(secret);
        System.out.println(sb.toString());
        return sb.toString();
    }

    public void testVerifySignature() throws Exception {
    }






    public static Test suite() {
        return new TestSuite(FacebookUtilTest.class);
    }
}
