package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Controller.PlayerController;
import Module.MainModule;
import Module.Spawner;

public class Screen extends Application {
    AnchorPane root = new AnchorPane();

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setTitle("Code Defender");

        MainModule mainModule = new MainModule(mainStage.getWidth(), mainStage.getHeight()); // Pass stage dimensions
        root.getChildren().add(mainModule.playerView);
        root.getChildren().add(mainModule.enemyView); //eterzrthgrtdrtzgdrtzdtrhg

        mainModule.playerView.setFitHeight(60);
        mainModule.playerView.setFitWidth(60);

        Scene scene = new Scene(root, 1024, 576);
        mainStage.setScene(scene);

        // Set the stage to fullscreen mode
        mainStage.setFullScreen(true);

        mainStage.show();

        // Initialize PlayerController after scene creation
        PlayerController playerController = new PlayerController(mainStage, mainModule);
        playerController.start();

        // Initialize and start the Spawner after PlayerController initialization
        Spawner spawner = new Spawner(mainStage, root, mainModule, playerController);
        spawner.startSpawning();
    }

    public static void main(String[] args) {
        launch();
    }
}
