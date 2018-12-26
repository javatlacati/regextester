package org.javapro.regextester;

import net.java.html.js.JavaScriptBody;
import org.javapro.regextester.js.PlatformServices;

public class BrowserMain {

    private BrowserMain() {
    }

    public static void main(String... args) throws Exception {
        Main.onPageLoad(new HTML5Services());
    }

    private static final class HTML5Services extends PlatformServices {
        // default behavior is enough for now

        /**
         * Opens a new browser at the specified url.
         *
         * @param url the specified url.
         */
        @Override
        public void openWebBrowser(String url) {
            openWebBrowserImpl(url);
        }

        @JavaScriptBody(
                args = {"url"}, body
                = "window.open(url, \n"
                + "                         'newwindow', \n"
                + "                         'width=300,height=250'); \n"
                + "              return false;"
        )
        public static native void openWebBrowserImpl(String url);
    }
}
