package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import Controller.PlayerController;
import Module.MainModule;
import Module.Spawner;
import Module.PointCounter;

public class Screen extends Application {
    AnchorPane root = new AnchorPane();

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setTitle("Code Defender");

        // Set background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/graphics/png/SPACE.gif"));
        ImageView backgroundView = new ImageView(backgroundImage);
        root.getChildren().add(backgroundView);

        MainModule mainModule = new MainModule(mainStage.getWidth(), mainStage.getHeight()); // Pass stage dimensions
        root.getChildren().add(mainModule.playerView);

        mainModule.playerView.setFitHeight(60);
        mainModule.playerView.setFitWidth(60);

        Scene scene = new Scene(root, 1024, 576);
        mainStage.setScene(scene);

        // Set the stage to fullscreen mode
        mainStage.setFullScreen(true);

        mainStage.show();

        // Initialize Spawner before PlayerController
        // Initialize PlayerController after scene creation
        PlayerController playerController = new PlayerController(mainStage, mainModule, null, root);
        playerController.start();

        // Initialize and start the Spawner after PlayerController initialization
        Spawner spawner = new Spawner(mainStage, root, mainModule, playerController);
        playerController.setSpawner(spawner); // Set the Spawner instance
        spawner.startSpawning();

        PointCounter pointCounter = new PointCounter(root);
        pointCounter.setDisplayCounter(true); // Set to true to display the counter

        // Pass the pointCounter to PlayerController
        playerController.setPointCounter(pointCounter);

        scene.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("SPACE")) {
                spawner.spawnLaser();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
