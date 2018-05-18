package eagleview.controllers;

import com.google.gson.Gson;
import eagleview.App;
import eagleview.models.SettingsModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationController {
    private static Stage stage;

    // widgets
    private static Label labelTest3;
    private static ListView<String> listVideo;
    private static Button buttonAddVideo;
    private static Button buttonRemoveVideo;

    // internal data
    private static List<String> importVideoQueue;

    public ConfigurationController() throws Exception {
        if(importVideoQueue == null) {
            importVideoQueue = new ArrayList<String>();
        }
    }

    public void render(Stage primary) throws Exception {
        stage = primary;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/vwConfiguration.fxml"));

        stage.setTitle ("Config - Eagle View");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    @FXML
    private void handleButtonAddVideoClicked(ActionEvent event) throws Exception {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("What video would you like to add?");
        dialog.setContentText("Please provide a YouTube URL to add to your video list.");
        dialog.initOwner(stage);

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
}
