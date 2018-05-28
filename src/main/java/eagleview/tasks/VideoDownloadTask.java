package eagleview.tasks;

import eagleview.App;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class VideoDownloadTask extends Task<Void> {
    private TextArea textLog;
    private String url;

    public VideoDownloadTask(String url, TextArea textField) {
        this.url = url;
        this.textLog = textField;
    }

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
            Process proc = procBuilder.start();

            bufferedReaderStdOut = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            bufferedReaderStdErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            System.out.println("Checking for program response...");

            String stdOutLine = bufferedReaderStdOut.readLine();
            String stdErrLine = bufferedReaderStdErr.readLine();

            while(stdOutLine != null || stdErrLine != null) {
                String logLine = "";

                if(stdOutLine != null) {
                    System.out.println(stdOutLine);
                    logLine += stdOutLine + "\n";
                }

                if(stdErrLine != null) {
                    System.out.println(stdErrLine);
                    logLine += stdErrLine + "\n";
                    isError = true;
                }

                Platform.runLater(new UpdateWindowLogTask(logLine, textLog));

                stdOutLine = bufferedReaderStdOut.readLine();
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

        Platform.runLater(new ShowDialogCompleteTask(isError));

        return null;
    }
}
