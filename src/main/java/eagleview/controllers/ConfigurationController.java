package eagleview.controllers;

import eagleview.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import org.apache.commons.lang.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigurationController implements Initializable {
    @FXML
    private ListView listVideo;

    @FXML
    private Button buttonVideoAdd;

    @FXML
    private Button buttonVideoRemove;

    // internal data
    private static List<String> importVideoQueue;

    public ConfigurationController() throws Exception {
        if(importVideoQueue == null) {
            importVideoQueue = new ArrayList<String>();
        }
    }

    @FXML
    private void handleButtonVideoAddClicked(ActionEvent event) throws Exception {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("What video would you like to add?");
        dialog.setContentText("Please provide a YouTube URL to add to your video list.");
        dialog.initOwner(buttonVideoAdd.getScene().getWindow());

        dialog.showAndWait().ifPresent(x -> {
            if(StringUtils.isNotBlank(x)) {
                System.out.println(x);
                importVideoQueue.add(x);

            }
        });

        event.consume();
    }

    @FXML
    private void handleButtonCloseClicked(ActionEvent event) {
        App.exitApplication();
        event.consume();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listVideo.getItems().add("YouTube Video 1");
        listVideo.getItems().add("YouTube Video 2");
        listVideo.getItems().add("YouTube Video 3");
        listVideo.getItems().add("YouTube Video 4");
        listVideo.getItems().add("YouTube Video 5");
        listVideo.getItems().add("YouTube Video 6");

        System.out.println("Config window initialized!");
    }
}
