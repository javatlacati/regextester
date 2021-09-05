package org.javapro.regextester;

import com.mifmif.common.regex.Generex;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import org.javapro.regextester.js.PlatformServices;
import net.java.html.boot.BrowserBuilder;

public final class Main {
    private Main() {
    }

    public static void main(String... args) throws Exception {
        BrowserBuilder.newBrowser().
            loadPage("pages/index.html").
            loadClass(Main.class).
            invoke("onPageLoad", args).
            showAndWait();
        System.exit(0);
    }

    /**
     * Called when the page is ready.
     */
    public static void onPageLoad(PlatformServices services) throws Exception {
        DataModel.onPageLoad(services);
    }


    public static void onPageLoad() throws Exception {
        // don't put "common" initialization stuff here, other platforms (iOS, Android, Bck2Brwsr) may not call this method. They rather call DataModel.onPageLoad
        DataModel.onPageLoad(new DesktopServices());
    }

    private static final class DesktopServices extends PlatformServices {

    private static final int POSSIBILITIES_TO_SHOW = 5;

    @Override
    public void openWebBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (IOException | URISyntaxException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public Set<String> nPossibilities(String regexText) {
        Set<String> allPossibilities = new HashSet<>(POSSIBILITIES_TO_SHOW);
        try {
            Generex generex = new Generex(regexText);
            for (int i = 0; i < POSSIBILITIES_TO_SHOW; i++) {
                allPossibilities.add(generex.random());
            }
        } catch (IllegalArgumentException iae) {
            allPossibilities.add(iae.getMessage());
            return allPossibilities;
        }
        return allPossibilities;
    }

    //
//    @Override
//    public String generateExample(String regexText) {
//        Generex generex = new Generex(regexText);
//        return generex.random();
//    }
}
}
