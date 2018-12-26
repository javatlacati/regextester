package org.javapro.regextester.js;

import java.util.HashSet;
import java.util.Set;
import net.java.html.js.JavaScriptBody;

/** Use {@link JavaScriptBody} annotation on methods to
 * directly interact with JavaScript. See
 * http://bits.netbeans.org/html+java/1.3/net/java/html/js/package-summary.html
 * to understand how.
 */
public class PlatformServices {
    
     /**
     * Opens a new browser at the specified url.
     *
     * @param url the specified url.
     */
    public void openWebBrowser(String url) {
        openWebBrowserImpl(url);
    }

    @JavaScriptBody(
            args = {"url"}, body =
            "window.open(url, \n" +
                    "                         'newwindow', \n" +
                    "                         'width=300,height=250'); \n" +
                    "              return false;"
    )
    public static native void openWebBrowserImpl(String url);

    public Set<String> nPossibilities(String regexText) {
        Set<String> allPossibilities = new HashSet<>();
        try {
            for (int i = 0; i < 5; i++) {
                allPossibilities.add(RandExp.gen(regexText));
                //RandExp randExp = RandExp.create(regexText);
                //allPossibilities.add(randExp.gen());
            }
        } catch (IllegalArgumentException iae) {
            allPossibilities.add(iae.getMessage());
            return allPossibilities;
        }
        return allPossibilities;
    }
}
