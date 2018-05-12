package eagleview;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.File;

public class App extends Application
{
    private static boolean isFullscreenMode = false;
    private static boolean isWindowPreviewMode = false;
    private static boolean isConfigurationMode = false;
    private static boolean isDialogSelectorPreviewMode = false;
    private static String previewWindowHandle = null;

    private TilePane grid;

    @Override
    public void start (Stage primaryStage) throws Exception {
       if(isFullscreenMode) {
           startScreensaver(primaryStage);
       } else if(isConfigurationMode) {
           startConfigMode(primaryStage);
       } else {
           isWindowPreviewMode = true;
           startScreensaver(primaryStage);
       }
    }

    public static void main( String[] args ) throws Exception {
        for(int i = 0; i <  args.length; i++) {
            String arg = args[i];

            if(arg.equals("/s")) {
                isFullscreenMode = true;
            } else if(arg.equals("/c")) {
                isConfigurationMode = true;
            } else if(arg.equals("/p")) {
                isDialogSelectorPreviewMode = true;

                if(args.length >= i+1) {
                    previewWindowHandle = args[i+1];
                }
            }
        }

        Application.launch (args);
    }

    private void startConfigMode(Stage primaryStage) throws Exception {

    }

    private void startScreensaver(Stage primaryStage) throws Exception {
        primaryStage.setTitle ("Eagle View");

        grid = new TilePane();
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setPrefRows(3);
        grid.setPrefColumns(3);
        grid.setPrefTileWidth((1920/3));
        grid.setPrefTileHeight((1080/3));

        grid.setPrefSize(1920, 1080); // Default width and height
        grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        for(int j = 0; j < 9; j++) {
            MediaPlayer player = new MediaPlayer( new Media(new File("C:\\Users\\Dan\\Desktop\\new.mp4").toURI().toURL().toString()));
            MediaView mediaView = new MediaView(player);

            grid.getChildren().add(mediaView);

            mediaView.fitWidthProperty().bind(grid.prefTileWidthProperty());
            mediaView.fitHeightProperty().bind(grid.prefTileHeightProperty());

            mediaView.setPreserveRatio(false);

            player.setMute(true);
            player.play();
        }

        Scene sceneMain = new Scene(grid);

        sceneMain.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                //System.out.println("Width: " + newSceneWidth);
                grid.setPrefTileWidth((newSceneWidth.intValue()/3));
            }
        });

        sceneMain.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                grid.setPrefTileHeight((newSceneHeight.intValue()/3));
            }
        });

        // TBD: REMOVE BEFORE GETTING TO PRODUCTION (ONLY USE IN DEV MODE)
        if(!isFullscreenMode) {
            sceneMain.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent t) {
                    if(t.getCode()==KeyCode.ESCAPE)
                    {
                        primaryStage.close();
                    }
                }
            });
        }

        if(isFullscreenMode) {
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setFullScreen(true);

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
        }

        primaryStage.setScene(sceneMain);
        primaryStage.show();
    }
}
