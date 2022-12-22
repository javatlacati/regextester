package org.javapro.regextester;

import com.intellij.openapi.diagnostic.Logger;//util
import com.intellij.openapi.project.Project;//platform // core
import com.intellij.openapi.wm.ToolWindow;//platform
import com.intellij.openapi.wm.ToolWindowFactory; // ide
import com.intellij.ui.content.Content; // ide
import com.intellij.ui.content.ContentFactory; // ide
import com.intellij.ui.content.ContentManager; // ide
import com.mifmif.common.regex.Generex;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebView;
import net.java.html.boot.fx.FXBrowsers;
import org.javapro.regextester.js.PlatformServices;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Javatlacati on 24/11/2018.
 */
public class MyToolWindowFactory implements ToolWindowFactory {
    private ToolWindow myToolWindow;
    Logger logger = Logger.getInstance(MyToolWindowFactory.class);

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        if (toolWindow != null) {
            myToolWindow = toolWindow;
            ContentFactory contentFactory = ContentFactory.getInstance();
            final JFXPanel fxPanel = new JFXPanel();
            Content content = contentFactory.createContent(fxPanel, "", false);
            Platform.setImplicitExit(false);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initFX(fxPanel);
                }
            });
            final ContentManager contentManager = myToolWindow.getContentManager();
            if (contentManager != null) {
                contentManager.addContent(content);
            } else {
                logger.error("missing content Manager");
            }
        } else {
            logger.error("missing tool window");
        }
    }


    private void initFX(JFXPanel fxPanel) {
        Scene scene = createScene();
        fxPanel.setScene(scene);
        fxPanel.setAutoscrolls(true);
    }

    private Scene createScene() {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, null);
        WebView webView = new WebView();
        webView.setFontSmoothingType(FontSmoothingType.LCD);
        root.getChildren().add(webView);
        URL resource = this.getClass().getResource("index.html");
        System.out.println(resource);
        FXBrowsers.load(webView, resource,
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DataModel.onPageLoad(new DesktopServices());
                        } catch (Exception e) {
                            logger.error(e);
                        }
                    }
                });
        return (scene);
    }


    private static final class DesktopServices extends PlatformServices {

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
            Set<String> allPossibilities = new HashSet<>(5);
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

    }

}
