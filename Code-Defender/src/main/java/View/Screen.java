package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Controller.PlayerController;
import Module.MainModule;

public class Screen extends Application {
    AnchorPane root = new AnchorPane();

    @Override
    public void start(Stage MainStage) throws Exception {
        MainStage.setTitle("Code Defender");
        MainStage.setScene(new Scene(root, 1024, 576));
        MainStage.show();
        PlayerController playerController = new PlayerController(MainStage);
        playerController.start();
        MainModule mainModule=new MainModule();
        root.getChildren().add(mainModule.playerView);
        mainModule.playerView.setFitHeight(60);
        mainModule.playerView.setFitWidth(60);
    }

    public static void main(String[] args) {
        launch();
    }
}


