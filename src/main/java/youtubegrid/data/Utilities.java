package youtubegrid.data;

import youtubegrid.models.SettingsModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utilities {
    private static Random random;

    public Utilities() {
        if(random == null) {
            random = new Random();
        }
    }

    public static List<String> getVideoCollection() {
        List<String> collection = new ArrayList<String>();

        File dir = new File(Config.data.videoCollectionDir);
        File [] files = dir.listFiles();

        System.out.println("Generating video collection...");
        if(files != null) {
            for (File file : files) {
                if(file.getName().trim().toLowerCase().endsWith(".mp4")) {
                    System.out.println("VIDEO: " + file.getAbsolutePath());
                    collection.add(file.getAbsolutePath());
                }
            }
        }

        return collection;
    }

    public static Alert showSimpleAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING, contentText, ButtonType.OK);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        alert.initModality(Modality.APPLICATION_MODAL);

        return alert;
    }

    public static int generateRandomNumber(int min, int max) {
        return (random.nextInt(max + 1 - min) + min);
    }


    public static String getApplicationPath() throws Exception {
        String fullPath = new File(SettingsModel.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        return Paths.get(fullPath).getParent().toString();
    }
}
