package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
    public Image backgroundImage;
    public ImageView backgroundView;

    public void initialize(AnchorPane root) {
        this.root = root;
        this.backgroundView = new ImageView();
        root.getChildren().add(backgroundView);

    }

    public void setBackgroundImage(String imagePath, PlayerController pc) {

            root.getChildren().remove(backgroundView);
            root.getChildren().remove(backgroundImage);

            backgroundView = null;
            backgroundImage = null;


        // Load the new background image
        Image newBackgroundImage = new Image(getClass().getResourceAsStream(imagePath));

        // Create a new ImageView with the new background image
        ImageView newBackgroundView = new ImageView(newBackgroundImage);

        // Set the size to match the root pane
        newBackgroundView.setFitWidth(root.getWidth());
        newBackgroundView.setFitHeight(root.getHeight());

        // Replace the old background with the new one
        root.getChildren().remove(backgroundView); // Remove the old background
        root.getChildren().add(newBackgroundView); // Add the new background

        backgroundView = newBackgroundView; // Update the reference to the background


        //backgroundView.toFront();
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setTitle("Intergalactic Invaders");

        // Set background image
        backgroundImage = new Image(getClass().getResourceAsStream("/graphics/png/backrounds/earth.png"));
        backgroundView = new ImageView(backgroundImage);

        initialize(root);


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
        PlayerController playerController = new PlayerController(mainStage, mainModule, null, root, this);
        playerController.start();

        // Initialize and start the Spawner after PlayerController initialization
        Spawner spawner = new Spawner(mainStage, root, mainModule, playerController);
        playerController.setSpawner(spawner); // Set the Spawner instance
        spawner.startSpawning();

        PointCounter pointCounter = new PointCounter(root);
        pointCounter.setDisplayCounter(true); // Set to true to display the counter

        // Pass the pointCounter to PlayerController
        playerController.setPointCounter(pointCounter);

        playerController.shopPointCounter(root);

    }

    public static void main(String[] args) {
        launch();
    }
}
