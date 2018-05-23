package eagleview.data;

import eagleview.controllers.ConfigurationController;
import eagleview.controllers.ScreensaverController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowManager {

    public void launchConfigWindow(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(ConfigurationController.class.getClassLoader().getResource("views/vwConfiguration.fxml"));

        stage.setTitle ("Config - Eagle View");
        stage.setScene(new Scene(root, 800, 600));

        //stage.setAlwaysOnTop(true);
        stage.show();
    }

    public void launchScreensaver(Stage stage) throws Exception {
        new ScreensaverController().render(stage);
    }
}
