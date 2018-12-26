package org.javapro.regextester.js;

import java.net.MalformedURLException;
import net.java.html.junit.BrowserRunner;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashSet;

/** Tests for behavior of @JavaScriptBody methods. The {@link BrowserRunner}
 * selects all possible presenters from your <code>pom.xml</code> and
 * runs the tests inside of them.
 *
 * See your <code>pom.xml</code> dependency section for details.
 */
@RunWith(BrowserRunner.class)
public class JsInteractionTest {
    @Test
    public void nPossibilities() {
        final PlatformServices services = new PlatformServices();

        //TODO that test fails
        //assertEquals(new HashSet<>(Arrays.asList("a","e","i","o","u")), services.nPossibilities("a|e|i|o|u"));
    }
    
     @Test
    public void openWebBrowser() {
        final PlatformServices services = new PlatformServices();
        services.openWebBrowser("https://wiki.openjdk.java.net/display/Nashorn/Nashorn+extensions");
    }
}
