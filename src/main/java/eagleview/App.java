package eagleview;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
import javafx.util.Duration;


import java.io.File;
import java.util.Random;

public class App extends Application
{
    private static boolean isFullscreenMode = false;
    private static boolean isWindowPreviewMode = false;
    private static boolean isConfigurationMode = false;
    private static boolean isDialogSelectorPreviewMode = false;
    private static String previewWindowHandle = null;
    private static boolean isFirstRunMouse = true;
    private static Point2D mouseLocation = null;
    private static Random random = null;
    private static String[] arguments = null;

    private TilePane grid;

    public static void main( String[] args ) throws Exception {
        random = new Random();

        arguments = args;
        Application.launch (args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        if(arguments.length > 0) {
//            String argFull = "";
//            for(String arg : arguments) {
//                argFull += String.format("ARG: %s", arg);
//            }
//
//            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
//            alert2.setContentText(argFull);
//            alert2.showAndWait();

            switch(arguments[0].toLowerCase().substring(0, 2)) {
                case "/s":
                    isFullscreenMode = true;
                    break;
                case "/c":
                    // Windows screensaver manager seems to pass /c:123456 (hwnd?) instead of just /c
                    isConfigurationMode = true;
                    break;
                case "/p":
                    if(arguments.length > 1) {
                        isDialogSelectorPreviewMode = true;
                        previewWindowHandle = arguments[1];
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Preview Window Handle Required!", ButtonType.OK);

                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.setAlwaysOnTop(true);
                        alert.show();
                    }
                    break;
            }
        }



        primaryStage.setOnCloseRequest(e -> {
            exitApplication();
        });

        if(isFullscreenMode) {
            startScreensaver(primaryStage);
        } else if(isConfigurationMode) {
            startConfigMode(primaryStage);
        } else if(isDialogSelectorPreviewMode) {
            // TBD do something
            // ...
            exitApplication();
        } else {
            isWindowPreviewMode = true;
            startScreensaver(primaryStage);
        }
    }

    private void startConfigMode(Stage primaryStage) throws Exception {
        primaryStage.setTitle ("Config - Eagle View");

        Label labelTest = new Label("Hello, Configuration!");
        Scene sceneMain = new Scene(labelTest, 800, 600);

        primaryStage.setScene(sceneMain);
        primaryStage.show();
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
            String filePath = String.format("C:\\videos\\%d.mp4", j+1);

            MediaPlayer player = new MediaPlayer( new Media(new File(filePath).toURI().toURL().toString()));
            MediaView mediaView = new MediaView(player);

            grid.getChildren().add(mediaView);

            mediaView.fitWidthProperty().bind(grid.prefTileWidthProperty());
            mediaView.fitHeightProperty().bind(grid.prefTileHeightProperty());
            mediaView.setPreserveRatio(false);

            player.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    player.seek(Duration.ZERO);
                }
            });

            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    int totalSeconds = (int)player.getTotalDuration().toSeconds();
                    int currentSecondMark = generateRandomNumber(0, Math.abs(totalSeconds-10));
                    player.seek(new Duration(currentSecondMark * 1000));
                }
            });

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

        if(isFullscreenMode) {
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable fullscreen exit message

            sceneMain.setCursor(Cursor.NONE);

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());

            sceneMain.addEventFilter(MouseEvent.MOUSE_MOVED , new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(isFirstRunMouse) {
                        isFirstRunMouse = false;
                        mouseLocation = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                    } else {
                        if(mouseLocation != null) {
                            if(mouseEvent.getX() >= mouseLocation.getX()+20 || mouseEvent.getX() >= mouseLocation.getX()-20
                                    || mouseEvent.getY() >= mouseLocation.getY()+20 || mouseEvent.getY() >= mouseLocation.getY()-20) {

                                exitApplication();
                            }
                        }
                    }
                }
            });

            sceneMain.setOnKeyPressed(event -> {
                exitApplication();
            });
        }

        primaryStage.setScene(sceneMain);
        primaryStage.show();
    }

    private void exitApplication() {
        Platform.exit();
        System.exit(0);
    }

    private int generateRandomNumber(int min, int max) {
        return (random.nextInt(max + 1 - min) + min);
    }
}
