package eagleview.controllers;

import eagleview.App;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ConfigurationController {
    private static Stage stage;

    private static Label labelTest3;
    private static ListView<String> listVideo;
    private static Button buttonAddVideo;
    private static Button buttonRemoveVideo;

    public void render(Stage primary) throws Exception {
        stage = primary;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/vwConfiguration.fxml"));

        stage.setTitle ("Config - Eagle View");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    @FXML
    private void handleButtonAddVideoClicked(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("What video would you like to add?");
        dialog.setContentText("Please provide a YouTube URL to add to your video list.");
        dialog.initOwner(stage);

        dialog.showAndWait().ifPresent(x -> {
            System.out.println(x);
        });

        event.consume();
    }

    @FXML
    private void handleButtonCloseClicked(ActionEvent event) {
        App.exitApplication();
        event.consume();
    }
}
