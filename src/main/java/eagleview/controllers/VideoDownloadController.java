package eagleview.controllers;

import eagleview.App;
import eagleview.data.Config;
import eagleview.data.Utilities;
import eagleview.tasks.ShowDialogCompleteTask;
import eagleview.tasks.UpdateWindowLogTask;
import eagleview.tasks.VideoDownloadTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ResourceBundle;

public class VideoDownloadController implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextArea textLog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Video download window initialized!");
    }

    public void downloadVideo(String url) throws Exception {
        File f = new File(Config.settings.youtubeDlBin);
        if (!f.exists()) {
            System.out.println("Unable to locate 'youtube-dl' binary! Please reinstall.");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = Utilities.showSimpleAlert("Unable to locate 'youtube-dl' binary! Please reinstall.");
                    alert.showAndWait();
                    closeWindow();
                }
            });

            return;
        }

        Task<Void> task = new VideoDownloadTask(url, textLog);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void closeWindow() {
        ((Stage)anchorPane.getScene().getWindow()).close();
    }
}
