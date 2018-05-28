package eagleview.tasks;

import javafx.scene.control.TextArea;

public class UpdateWindowLogTask implements Runnable {
    private String logLine;
    private TextArea textLog;

    public UpdateWindowLogTask(String line, TextArea textField) {
        this.logLine = line;
        this.textLog = textField;
    }

    @Override
    public void run() {
        this.textLog.setText(this.textLog.getText() + this.logLine + "\n");
        this.textLog.setScrollTop(Double.MAX_VALUE);
    }
}
