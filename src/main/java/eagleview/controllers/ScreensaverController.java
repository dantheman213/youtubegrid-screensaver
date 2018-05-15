package eagleview.controllers;

import eagleview.App;
import eagleview.data.Utilities;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;

public class ScreensaverController {
    private static Point2D mouseLocation = null;
    private static boolean isFirstRunMouse = true;

    public ScreensaverController(Stage primaryStage) throws Exception {
        start(primaryStage);
    }

    public void start(Stage primaryStage) throws Exception {
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
                        int currentSecondMark = Utilities.generateRandomNumber(0, Math.abs(totalSeconds-10));
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

                                App.exitApplication();
                            }
                        }
                    }
                }
            });

            sceneMain.setOnKeyPressed(event -> {
                App.exitApplication();
            });


            currentStage.setScene(sceneMain);
            currentStage.show();

            if(!App.isFullscreenMode)
                break;
        }
    }
}
