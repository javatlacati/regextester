package org.javapro.regextester;

import com.mifmif.common.regex.Generex;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;
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

    @Override
    public String getPreferences(String key) {
        return Preferences.userNodeForPackage(Main.class).get(key, null);
    }

    @Override
    public void setPreferences(String key, String value) {
        Preferences.userNodeForPackage(Main.class).put(key, value);
    }

    @Override
    public void openWebBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
//                new ProcessBuilder("x-www-browser", url).start();
        } catch (IOException | URISyntaxException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public Set<String> nPossibilities(String regexText) {
        Set<String> allPossibilities = new HashSet<>();
        try {
            Generex generex = new Generex(regexText);
            for (int i = 0; i < 5; i++) {
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
