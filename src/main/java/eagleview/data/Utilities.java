package eagleview.data;

import eagleview.models.SettingsModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.Random;

public class Utilities {
    private static Random random;

    public Utilities() {
        if(random == null) {
            random = new Random();
        }
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
