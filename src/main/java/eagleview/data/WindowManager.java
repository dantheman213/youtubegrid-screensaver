package eagleview.data;

import eagleview.controllers.ConfigurationController;
import eagleview.controllers.ScreensaverController;
import eagleview.controllers.VideoDownloadController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class WindowManager {

    public void launchConfigWindow(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(WindowManager.class.getClassLoader().getResource("views/vwConfiguration.fxml"));

        stage.setTitle ("Config - Eagle View");
        stage.setScene(new Scene(root, 800, 600));

        //stage.setAlwaysOnTop(true);
        stage.show();
    }

    public void launchVideoDownloadModal(Window parentWindow, String url) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(WindowManager.class.getClassLoader().getResource("views/modalVideoDownload.fxml"));
        Parent root = fxmlLoader.load();

        VideoDownloadController controller = fxmlLoader.<VideoDownloadController>getController();

        Stage dialogStage = new Stage();
        dialogStage.setTitle ("Downloading Video...");
        dialogStage.setScene(new Scene(root, 320, 240));
        dialogStage.setX(parentWindow.getX() + 300);
        dialogStage.setY(parentWindow.getY() + 300);
        dialogStage.initOwner(parentWindow);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        //dialogStage.setAlwaysOnTop(true);
        dialogStage.show();

        controller.downloadVideo(url);
    }

    public void launchScreensaver(Stage stage) throws Exception {
        new ScreensaverController().render(stage);
    }
}
