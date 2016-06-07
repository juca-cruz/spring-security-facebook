package facebook;

import junit.framework.TestCase;
import org.springframework.security.facebook.FacebookGraphAuthenticationToken;

/**
 *
 * @author JCruz
 * @since <pre>07/27/2010</pre>
 */
public class FacebookGraphAuthenticationTokenTest extends TestCase {
    public FacebookGraphAuthenticationTokenTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseUIDFromAccessToke() {
        FacebookGraphAuthenticationToken token = new FacebookGraphAuthenticationToken();
        String testString = "126153854093012|3c776ea88f8808a563782b4b-1321383876|6ypvwgxdjUHitQJ8NF41HFnvSt8.";
        Long l = token.parseUIDFromAccessToken(testString);
        assertEquals(Long.valueOf(1321383876), l);
        System.out.println("Long: " + l);
    }

}
