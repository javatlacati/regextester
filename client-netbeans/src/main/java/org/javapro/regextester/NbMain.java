package org.javapro.regextester;

import java.util.HashSet;
import java.util.Set;
import org.javapro.regextester.js.PlatformServices;
import org.netbeans.api.htmlui.OpenHTMLRegistration;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbPreferences;

public class NbMain {
    private NbMain() {
    }
    
    @ActionID(
        category = "Games",
        id = "org.javapro.regextester.OpenPage"
    )
    @OpenHTMLRegistration(
        url="index.html",
        displayName = "Open Your Page",
        iconBase = "org/javapro/regextester/icon.png"
    )
    @ActionReferences({
        @ActionReference(path = "Menu/Window"),
        @ActionReference(path = "Toolbars/Games")
    })
    public static void onPageLoad() throws Exception {
        Main.onPageLoad(new NbServices());
    }

    private static class NbServices extends PlatformServices {
        public NbServices() {
        }

        @Override
        public String getPreferences(String key) {
            return NbPreferences.forModule(NbMain.class).get(key, null);
        }

        @Override
        public void setPreferences(String key, String value) {
            NbPreferences.forModule(NbMain.class).put(key, value);
        }
        
        @Override
    public void openWebBrowser(String url) {
//        try {
//            Desktop.getDesktop().browse(new URL(url).toURI());
//        } catch (IOException | URISyntaxException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public Set<String> nPossibilities(String regexText) {
        Set<String> allPossibilities = new HashSet<>();
//        try {
//            Generex generex = new Generex(regexText);
//            for (int i = 0; i < 5; i++) {
//                allPossibilities.add(generex.random());
//            }
//        } catch (IllegalArgumentException iae) {
//            allPossibilities.add(iae.getMessage());
//            return allPossibilities;
//        }
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
