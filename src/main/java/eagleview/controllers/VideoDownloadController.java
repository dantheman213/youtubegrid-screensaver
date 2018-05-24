package eagleview.controllers;

import eagleview.App;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoDownloadController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Config window initialized!");
    }

    public void downloadVideo(String url) throws Exception {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Process proc = new ProcessBuilder(App.config.settings.youtubeDlBin, "-f \"worstvideo[ext=mp4]\"", "--no-certificate-check", url).start();
                InputStream inputStream = proc.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                // TBD ...

                return null;
            }
        };
    }
}
