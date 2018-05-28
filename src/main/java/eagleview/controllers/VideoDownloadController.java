package eagleview.controllers;

import eagleview.data.Config;
import eagleview.data.Utilities;
import eagleview.tasks.VideoDownloadTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
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
        checkForYouTubeDlBinary();

        Task<Void> task = new VideoDownloadTask(url, textLog);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void checkForYouTubeDlBinary() throws Exception {
        File f = new File(Config.data.youtubeDlBin);
        if (!f.exists()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = Utilities.showSimpleAlert("Unable to locate 'youtube-dl' binary! Please reinstall.");
                    alert.showAndWait();
                    closeWindow();
                }
            });

            System.out.println("Unable to locate 'youtube-dl' binary! Please reinstall.");
            throw new Exception("`youtube-dl` binary doesn't exist.");
        }
    }

    private void closeWindow() {
        ((Stage)anchorPane.getScene().getWindow()).close();
    }
}
