package eagleview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class App extends Application
{
    @Override
    public void start (Stage primaryStage) throws Exception {
        primaryStage.setTitle ("Eagle View");

        GridPane grid = new GridPane();

        for(int j = 0; j < 3; j++) {
            for(int k = 0; k < 3; k++) {
                WebView webView = new WebView();
                Scene scene = new Scene(webView, 320, 240);

                webView.getEngine().load("https://www.youtube.com/embed/mHswm4oyKQ4?vq=medium&autoplay=1");
                grid.add(webView, j, k);
            }
        }

        Scene sceneMain = new Scene(grid);
        primaryStage.setScene (sceneMain);
        primaryStage.show ();
    }

    public static void main( String[] args ) {
        Application.launch (args);
    }
}
