package eagleview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class App extends Application
{
    @Override
    public void start (Stage primaryStage) throws Exception {
        primaryStage.setTitle ("Eagle View");

        WebView webView = new WebView();

        Scene scene = new Scene(webView, 1350, 800);
        primaryStage.setScene (scene);

        webView.getEngine().load("https://youtube.com/");

        primaryStage.show ();
    }

    public static void main( String[] args ) {
        Application.launch (args);
    }
}
