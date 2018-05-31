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
import java.util.List;

public class ScreensaverController {
    private static Point2D mouseLocation = null;
    private static boolean isFirstRunMouse = true;

    private static final int tileCount = 9;
    private static List<String> videoQueue;

    private MediaView generateVideoPlayerWithVideo() throws Exception {
        String filePath;

        if(videoQueue != null) {
            // unique vids
            int randomIndex = Utilities.generateRandomNumber(0, videoQueue.size()-1);
            filePath = videoQueue.get(randomIndex);
            videoQueue.remove(randomIndex);
        } else {
            // any random vid
            filePath = App.config.data.videoCollection.get(Utilities.generateRandomNumber(0, App.config.data.videoCollection.size()-1));
        }
        System.out.println(String.format("Loading video: %s", filePath));

        MediaPlayer player = new MediaPlayer( new Media(new File(filePath).toURI().toURL().toString()));
        MediaView mediaView = new MediaView(player);
        mediaView.setPreserveRatio(false);

        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                player.seek(Duration.ZERO);
                player.play();
            }
        });

        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                int totalSeconds = (int)player.getTotalDuration().toSeconds();
                int currentSecondMark = Utilities.generateRandomNumber(0, Math.abs(totalSeconds-10));
                player.seek(new Duration(currentSecondMark * 1000));

                player.play();
            }
        });

        player.setOnError(new Runnable() {
            @Override
            public void run() {
                System.out.println("UNABLE TO LOAD VIDEO..! Retrying..");
                player.seek(Duration.ZERO);
                player.play();
            }
        });

        player.setMute(true);

        return mediaView;
    }

    public void render(Stage primaryStage) throws Exception {
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

            int videoCount = App.config.data.videoCollection.size();
            int totalTileCount = tileCount * Screen.getScreens().size();
            System.out.println(String.format("Need to load %d video tiles.. have %d video files...", totalTileCount, videoCount));
            if(videoCount >= totalTileCount) {
                // Get unique videos since the video collection is larger than the anticipated tile count
                System.out.println("Will load unique videos..!");
                videoQueue = App.config.data.videoCollection;
            } else {
                System.out.println("Will load random videos that may repeat..! Add more videos if you want a unique video per tile.");
            }

            for(int j = 0; j < tileCount; j++) {
                MediaView mediaView = generateVideoPlayerWithVideo();
                mediaView.fitWidthProperty().bind(grid.prefTileWidthProperty());
                mediaView.fitHeightProperty().bind(grid.prefTileHeightProperty());

                grid.getChildren().add(mediaView);
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
        }
    }
}
