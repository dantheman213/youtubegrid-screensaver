package youtubegrid.tasks;

import youtubegrid.App;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoDownloadTask extends Task<Void> {
    private TextArea textLog;
    private String youtubeUrl;

    public VideoDownloadTask(String url, TextArea textField) {
        youtubeUrl = url;
        textLog = textField;
    }

    @Override
    protected Void call() {
        try {
            System.out.println("Starting video download thread...");

            List<String> cmdArgs = new ArrayList<String>();
            cmdArgs.add(App.config.data.youtubeDlBin);
            cmdArgs.add("--abort-on-error");
            cmdArgs.add("-o %(title)s.%(ext)s");
            cmdArgs.add("--no-playlist");
            cmdArgs.add("-f best[height<=480][ext=mp4]");
            cmdArgs.add(youtubeUrl);


            System.out.println("EXECUTING COMMAND: " + String.join(" ", cmdArgs));

            ProcessBuilder procBuilder = new ProcessBuilder(cmdArgs);
            procBuilder.directory(new File(App.config.data.videoCollectionDir));
            Process proc = procBuilder.start();

            Thread threadStdOut = new Thread(new ProcessStreamReaderTask(proc.getInputStream(), textLog));
            threadStdOut.setDaemon(true);
            threadStdOut.start();

            Thread threadStdErr = new Thread(new ProcessStreamReaderTask(proc.getErrorStream(), textLog));
            threadStdErr.setDaemon(true);
            threadStdErr.start();

            // wait for both threads to finish before continuing
            threadStdOut.join();
            threadStdErr.join();

            proc.waitFor();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
