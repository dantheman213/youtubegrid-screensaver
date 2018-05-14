package eagleview.views;

import eagleview.App;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConfigurationView {

    public ConfigurationView(Stage primaryStage) throws Exception {
        start(primaryStage);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle ("Config - Eagle View");

        GridPane grid = new GridPane();

        Label labelTest = new Label("Hello, ConfigurationView!");
        grid.add(labelTest,0,0);

        Label labelTest2 = new Label("Learn how to configure your screensaver here:");
        grid.add(labelTest2,0,1);

        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                App.exitApplication();
                event.consume();
            }
        });
        grid.add(buttonCancel, 0, 2);

        Button buttonSave = new Button("Save");
        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                App.exitApplication();
                event.consume();
            }
        });
        grid.add(buttonSave, 1, 2);

        Scene sceneMain = new Scene(grid, 800, 600);

        primaryStage.setScene(sceneMain);
        primaryStage.show();
    }
}
