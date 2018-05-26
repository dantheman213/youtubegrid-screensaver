package eagleview.controllers;

import eagleview.App;
import eagleview.data.Config;
import eagleview.data.Utilities;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                BufferedReader bufferedReaderStdOut = null;
                BufferedReader bufferedReaderStdErr = null;

                boolean isError = false;

                try {
                    System.out.println("Starting video download thread...");

                    String[] cmdArgs = {
                            App.config.settings.youtubeDlBin,
                            "-o %(title)s.%(ext)s",
                            "-f worstvideo[ext=mp4]",
                            url
                    };

                    ProcessBuilder procBuilder = new ProcessBuilder(cmdArgs);
                    procBuilder.directory(new File(App.config.settings.videoCollectionDir));
                    //procBuilder.redirectErrorStream(true);
                    Process proc = procBuilder.start();

                    bufferedReaderStdOut = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    bufferedReaderStdErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

                    System.out.println("Checking for program response...");

                    String stdOutLine = bufferedReaderStdOut.readLine();
                    String stdErrLine = bufferedReaderStdErr.readLine();
                    while(stdOutLine != null || stdErrLine != null) {
                        if(stdOutLine != null) {
                            System.out.println(stdOutLine);
                        }

                        if(stdErrLine != null) {
                            System.out.println(stdErrLine);
                            isError = true;
                        }

                        stdOutLine  = bufferedReaderStdOut.readLine();
                        stdErrLine = bufferedReaderStdErr.readLine();
                    }

                    proc.waitFor();
                } catch(Exception ex) {
                    // TBD
                    isError = true;
                    ex.printStackTrace();
                } finally {
                    System.out.println("Finished.. closing buffer.");
                    if(bufferedReaderStdOut != null) {
                        try {bufferedReaderStdOut.close();} catch(Exception ex) {}
                    }
                }

                // Create an on-the-fly custom Runnable that can receive data input into its thread
                class CloseWindowTask implements Runnable {
                    boolean isError;
                    public CloseWindowTask(boolean error) {
                        isError = error;
                    }

                    public void run() {
                        System.out.println("Close dialog window...");
                        closeWindow();

                        Alert alert = Utilities.showSimpleAlert((isError ? "An error occurred!" : "Completed Successfully!"));
                        alert.show();
                    }
                }
                Platform.runLater(new CloseWindowTask(isError));

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
