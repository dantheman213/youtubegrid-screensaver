package eagleview.tasks;

import javafx.scene.control.TextArea;

public class UpdateWindowLogTask implements Runnable {
    private String logLine;
    private TextArea textLog;

    public UpdateWindowLogTask(String line, TextArea textField) {
        logLine = line;
        textLog = textField;
    }

    @Override
    public void run() {
        textLog.setText(textLog.getText() + logLine + "\n");
        textLog.setScrollTop(Double.MAX_VALUE);
    }
}
