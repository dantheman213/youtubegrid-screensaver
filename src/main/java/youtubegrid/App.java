package youtubegrid;

import youtubegrid.data.Config;
import youtubegrid.data.Utilities;
import youtubegrid.data.WindowManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App extends Application {
    public static Config config;
    public static WindowManager windowManager;

    public static boolean isFullscreenMode = false;
    public static boolean isConfigurationMode = false;
    public static boolean isDialogSelectorPreviewMode = false;
    public static String previewWindowHandle = null;
    public static String[] arguments = null;

    public static void main(String[] args) throws Exception {
        arguments = args;

        Utilities util = new Utilities();
        config = new Config();
        config.data.updateVideoCollection();

        checkAppDirectories();

        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(arguments.length > 0) {
            switch(arguments[0].toLowerCase().substring(0, 2)) {
                case "/s":
                    isFullscreenMode = true;
                    break;
                case "/c":
                    // Windows screensaver manager seems to pass /c:123456 (hwnd?) instead of just /c
                    isConfigurationMode = true;
                    break;
                case "/p":
                    if(arguments.length > 1) {
                        isDialogSelectorPreviewMode = true;
                        previewWindowHandle = arguments[1];
                    } else {
                        Alert alert = Utilities.showSimpleAlert("Preview Window Handle Required!");
                        alert.showAndWait();
                        exitApplication();
                    }
                    break;
            }
        } else {
            Alert alert = Utilities.showSimpleAlert("Install screensaver to use! Or read manual to activate manually.");
            alert.showAndWait();
            exitApplication();
        }

        primaryStage.setOnCloseRequest(e -> {
            exitApplication();
        });

        windowManager = new WindowManager();
        if(isFullscreenMode) {
            windowManager.launchScreensaver(primaryStage);
        } else if(isConfigurationMode) {
            windowManager.launchConfigWindow(primaryStage);
        } else if(isDialogSelectorPreviewMode) {
            exitApplication();
        } else {
            exitApplication();
        }
    }

    private static void checkAppDirectories() throws Exception {
        File f = new File(Config.data.videoCollectionDir);
        if (!f.exists()) {
            Files.createDirectories(Paths.get(Config.data.videoCollectionDir));
        }

        f = new File(Config.data.binDir);
        if(!f.exists()) {
            Files.createDirectories(Paths.get(Config.data.binDir));
        }
    }

    public static void exitApplication() {
        Platform.exit();
        System.exit(0);
    }
}
