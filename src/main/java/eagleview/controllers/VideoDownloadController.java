package eagleview.controllers;

import eagleview.App;
import eagleview.data.Config;
import eagleview.data.Utilities;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Video download window initialized!");
    }

    public void downloadVideo(String url) throws Exception {
        File f = new File(Config.settings.youtubeDlBin);
        if (!f.exists()) {
            System.out.println("Unable to locate 'youtube-dl' binary! Please reinstall.");

            closeWindow();
            Utilities.showSimpleAlert("Unable to locate 'youtube-dl' binary! Please reinstall.");
            return;
        }


        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                BufferedReader bufferedReader = null;

                try {
                    System.out.println("Starting video download thread...");

                    String[] cmdArgs = {
                            App.config.settings.youtubeDlBin,
                            "-o %(title)s.%(ext)s",
                            //String.format("-o \"%s%s%s\"", App.config.settings.videoCollectionDir, File.separator, "%(title)s.%(ext)s"),
                            "-f worstvideo[ext=mp4]",
                            //"--no-certificate-check",
                            url
                    };

                    ProcessBuilder procBuilder = new ProcessBuilder(cmdArgs);
                    procBuilder.directory(new File(App.config.settings.videoCollectionDir));
                    procBuilder.redirectErrorStream(true);

                    Process proc = procBuilder.start();

                    InputStream inputStream = proc.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);

                    System.out.println("Checking for program response...");
                    String consoleLine = bufferedReader.readLine();
                    while(consoleLine != null) {
                        System.out.println(consoleLine);
                        consoleLine  = bufferedReader.readLine();
                    }

                    proc.waitFor();
                } catch(Exception ex) {
                    // TBD
                    ex.printStackTrace();
                } finally {
                    System.out.println("Finished.. closing dialog.");
                    if(bufferedReader != null) {
                        try {bufferedReader.close();} catch(Exception ex) {}
                    }

                    closeWindow();
                }


                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void closeWindow() {
        ((Stage)anchorPane.getScene().getWindow()).close();
    }
}
