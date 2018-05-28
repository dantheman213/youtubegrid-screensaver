package eagleview.tasks;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProcessStreamReaderTask implements Runnable {
    private InputStreamReader inputStreamReader;
    private TextArea textLog; // optional widget to dump text data to

    private BufferedReader bufferedReader;

    public ProcessStreamReaderTask(InputStreamReader isr) {
        this.inputStreamReader = isr;
    }

    public ProcessStreamReaderTask(InputStreamReader isr, TextArea textField) {
        this.inputStreamReader = isr;
        this.textLog = textField;
    }

    @Override
    public void run() {

    }
}
