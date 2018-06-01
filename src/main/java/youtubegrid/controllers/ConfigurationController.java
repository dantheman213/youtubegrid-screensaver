package youtubegrid.controllers;

import youtubegrid.App;
import youtubegrid.data.Config;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.*;

public class ConfigurationController implements Initializable {
    @FXML
    private ListView listVideo;

    @FXML
    private Button buttonVideoAdd;

    @FXML
    private Button buttonVideoRemove;

    @FXML
    private void handleButtonVideoAddClicked(ActionEvent event) throws Exception {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("What video would you like to add?");
        dialog.setContentText("Please provide a YouTube URL to add to your video list.");

        Window parentWindow = buttonVideoAdd.getScene().getWindow();
        dialog.initOwner(parentWindow);

        dialog.showAndWait().ifPresent(query -> {
            if(StringUtils.isNotBlank(query)) {
                System.out.println(query);

                try {
                    Stage dialogStage = App.windowManager.launchVideoDownloadModal(parentWindow, query);
                    dialogStage.setOnCloseRequest(dialogEvent -> {
                        updateVideoList();
                    });

                } catch(Exception ex) {
                    // TBD
                    ex.printStackTrace();
                }
            }
        });

        event.consume();
    }

    @FXML
    private void handleButtonVideoRemoveClicked(ActionEvent event) throws Exception {
        if(!listVideo.getSelectionModel().isEmpty()) {
            String fileName = listVideo.getSelectionModel().getSelectedItem().toString();

            if(StringUtils.isNotBlank(fileName)) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Delete Video '" + fileName + "'?", ButtonType.YES, ButtonType.NO);
                alert.setTitle("Delete Dialog");

                alert.showAndWait().ifPresent(type -> {
                    if(type == ButtonType.YES) {
                        System.out.println("Deleting video " + fileName);
                        File file = new File(App.config.data.videoCollectionDir + File.separator + fileName);
                        file.delete();

                        updateVideoList();
                    }
                });
            }
        }
    }

    @FXML
    private void handleButtonCloseClicked(ActionEvent event) {
        App.exitApplication();
        event.consume();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateVideoList();
        System.out.println("Config window initialized!");
    }

    public void updateVideoList() {
        Config.data.updateVideoCollection();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listVideo.getItems().clear();

                for(String path : Config.data.videoCollection) {
                    String name = new File(path).getName();
                    listVideo.getItems().add(name);
                }
            }
        });
    }
}
