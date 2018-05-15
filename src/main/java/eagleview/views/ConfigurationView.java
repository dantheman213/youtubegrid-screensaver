package eagleview.views;

import eagleview.App;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ConfigurationView {
    private static Stage primaryStage;

    private static Label labelTest3;
    private static ListView<String> listVideo;

    public ConfigurationView(Stage in) throws Exception {
        primaryStage = in;
        start();
    }

    public void start() throws Exception {
        primaryStage.setTitle ("Config - Eagle View");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));

        Label labelTest = new Label("Hello, ConfigurationView!");
        grid.add(labelTest,0,0);

        Label labelTest2 = new Label("Learn how to configure your screensaver here:");
        grid.add(labelTest2,0,1);

        listVideo = new ListView<String>();
        listVideo.setPrefSize(220, 500);
        listVideo.getItems().add("Item 1");
        listVideo.getItems().add("Item 2");
        listVideo.getItems().add("Item 3");
        listVideo.getItems().add("Item 4");

        listVideo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // ((Label)primaryStage.getScene().lookup("labelTest3")).setText(listVideo.getSelectionModel().getSelectedItem());
                labelTest3.setText(listVideo.getSelectionModel().getSelectedItem());
            }
        });

        grid.add(listVideo, 0, 2);

        labelTest3 = new Label("TBD");
        labelTest3.setId("labelTest3");
        grid.add(labelTest3,1,2);

        HBox paneActionButtons = new HBox();

        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                App.exitApplication();
                event.consume();
            }
        });
        paneActionButtons.getChildren().add(buttonCancel);

        Button buttonSave = new Button("Save");
        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                App.exitApplication();
                event.consume();
            }
        });
        paneActionButtons.getChildren().add(buttonSave);

        paneActionButtons.setSpacing(10);
        grid.add(paneActionButtons, 0, 3);


        Scene sceneMain = new Scene(grid, 800, 600);

        primaryStage.setScene(sceneMain);
        primaryStage.show();
    }
}
