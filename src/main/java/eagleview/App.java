package eagleview;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import javafx.stage.Modality;
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

    public static void main( String[] args ) throws Exception {
        random = new Random();

        arguments = args;
        Application.launch (args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        if(arguments.length > 0) {
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
                        Alert alert = showSimpleAlert("Preview Window Handle Required!");
                        alert.showAndWait();
                        exitApplication();
                    }
                    break;
            }
        } else {
            Alert alert = showSimpleAlert("Install screensaver to use! Or read manual to activate manually.");
            alert.showAndWait();
            exitApplication();
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

        GridPane grid = new GridPane();

        Label labelTest = new Label("Hello, Configuration!");
        grid.add(labelTest,0,0);

        Label labelTest2 = new Label("Learn how to configure your screensaver here:");
        grid.add(labelTest2,0,1);

        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exitApplication();
                event.consume();
            }
        });
        grid.add(buttonCancel, 0, 2);

        Button buttonSave = new Button("Save");
        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exitApplication();
                event.consume();
            }
        });
        grid.add(buttonSave, 1, 2);



        Scene sceneMain = new Scene(grid, 800, 600);

        primaryStage.setScene(sceneMain);
        primaryStage.show();
    }

    private void startScreensaver(Stage primaryStage) throws Exception {
        boolean primaryStageUsed = false;

        for(Screen screen : Screen.getScreens()) {
            Stage currentStage;
            TilePane grid;

            if(!primaryStageUsed) {
                primaryStageUsed = true;
                currentStage = primaryStage;
            } else {
                currentStage = new Stage();
            }

            currentStage.setTitle ("Eagle View");
            currentStage.toFront();

            if (Screen.getPrimary().equals(screen)) {
                currentStage.setAlwaysOnTop(true);
            }

            //currentStage.initModality(Modality.APPLICATION_MODAL);

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


            currentStage.initStyle(StageStyle.UNDECORATED);
            currentStage.setFullScreen(true);
            currentStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable fullscreen exit message

            sceneMain.setCursor(Cursor.NONE);

            Rectangle2D bounds = screen.getBounds();
            currentStage.setX(bounds.getMinX());
            currentStage.setY(bounds.getMinY());
            currentStage.setWidth(bounds.getWidth());
            currentStage.setHeight(bounds.getHeight());

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


            currentStage.setScene(sceneMain);
            currentStage.show();

            if(!isFullscreenMode)
                break;
        }
    }

    private void exitApplication() {
        Platform.exit();
        System.exit(0);
    }

    private int generateRandomNumber(int min, int max) {
        return (random.nextInt(max + 1 - min) + min);
    }

    private Alert showSimpleAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING, contentText, ButtonType.OK);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        alert.initModality(Modality.APPLICATION_MODAL);

        return alert;
    }
}
