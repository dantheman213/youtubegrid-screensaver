package youtubegrid.data;

import youtubegrid.App;
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

    public static String getVideoCollectionDiskSize() {
        return humanReadableByteCount(folderSize(new File(App.config.data.videoCollectionDir)), true);
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

    public static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
