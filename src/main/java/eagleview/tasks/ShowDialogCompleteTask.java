package eagleview.tasks;

import eagleview.data.Utilities;
import javafx.scene.control.Alert;

public class ShowDialogCompleteTask implements Runnable {
    private boolean isError;

    public ShowDialogCompleteTask(boolean error) {
        isError = error;
    }

    @Override
    public void run() {
        Alert alert;

        if(isError) {
            alert = Utilities.showSimpleAlert("An error occurred!");
        } else {
            alert = Utilities.showSimpleAlert("Completed Successfully!");
        }

        alert.show();
    }
}

