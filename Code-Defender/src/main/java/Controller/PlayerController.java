package Controller;

import Module.MainModule;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerController {
    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();

    private BooleanBinding keyPressed = wPressed.or(sPressed);

    private int movementVariable = 2;

    private Stage primaryStage;
    private MainModule mainModule;

    public PlayerController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.mainModule = new MainModule();
        initialize(null, null);
    }

    public void start() {
        mainModule.playerView.setLayoutY(500);
        mainModule.playerView.setLayoutX(200);
    }

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long timestamp) {
            if(wPressed.get()) {
                mainModule.playerView.setLayoutY(mainModule.playerView.getLayoutY() - movementVariable);
            }

            if(sPressed.get()){
                mainModule.playerView.setLayoutY(mainModule.playerView.getLayoutY() + movementVariable);
            }
        }
    };

    public void initialize(URL url, ResourceBundle resourceBundle) {
        movementSetup();

        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if(!aBoolean){
                timer.start();
            } else {
                timer.stop();
            }
        }));
    }

    public void movementSetup() {
        Scene primaryScene = primaryStage.getScene();
        primaryScene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.W) {
                wPressed.set(true);
            }

            if(e.getCode() == KeyCode.S) {
                sPressed.set(true);
            }

        });

        primaryScene.setOnKeyReleased(e ->{
            if(e.getCode() == KeyCode.W) {
                wPressed.set(false);
            }

            if(e.getCode() == KeyCode.S) {
                sPressed.set(false);
            }
        });
    }
}
