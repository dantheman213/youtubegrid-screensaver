package eagleview.tasks;

import eagleview.App;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import java.io.File;

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

            String[] cmdArgs = {
                    App.config.data.youtubeDlBin,
                    "-o %(title)s.%(ext)s",
                    "-f worstvideo[ext=mp4]",
                    youtubeUrl
            };

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