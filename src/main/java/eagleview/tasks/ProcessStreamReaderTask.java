package eagleview.tasks;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessStreamReaderTask implements Runnable {
    private InputStreamReader inputStreamReader;
    private TextArea textLog; // optional widget to dump text data to

    private BufferedReader bufferedReader;

    public ProcessStreamReaderTask(InputStream is) {
        this.inputStreamReader = new InputStreamReader(is);
    }

    public ProcessStreamReaderTask(InputStream is, TextArea textField) {
        this.inputStreamReader = new InputStreamReader(is);
        this.textLog = textField;
    }

    @Override
    public void run() {
        try {
            bufferedReader = new BufferedReader(this.inputStreamReader);
            String stdOutput = bufferedReader.readLine();

            while(stdOutput != null) {
                System.out.println(stdOutput);
                if(this.textLog != null) {
                    Platform.runLater(new UpdateWindowLogTask(stdOutput, textLog));
                }

                stdOutput = bufferedReader.readLine();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Stream finished.. closing buffer!");
            try { bufferedReader.close();} catch(Exception ex) { ex.printStackTrace(); }
        }
    }
}
