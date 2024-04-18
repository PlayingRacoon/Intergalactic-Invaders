package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
        Image backgroundImage = new Image(getClass().getResourceAsStream("/graphics/png/space_1.gif"));
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

        // Load the laser image
        // Load the laser image
        Image laserImage = new Image(getClass().getResourceAsStream("/graphics/png/player/laser(player).gif"));

// Initialize Spawner with the laserImage
        Spawner spawner = new Spawner(mainStage, root, mainModule, laserImage);
        spawner.startSpawning(); // Start spawning enemies


        // Initialize PlayerController with Spawner instance
        PlayerController playerController = new PlayerController(mainStage, mainModule, spawner, root, laserImage);
        playerController.start();

        // Start spawning enemies


        // Initialize and display the point counter
        PointCounter pointCounter = new PointCounter(root);
        pointCounter.setDisplayCounter(true);

        // Pass the point counter to PlayerController
        playerController.setPointCounter(pointCounter);
    }

    public static void main(String[] args) {
        launch();
    }
}
