package Controller;

import Module.MainModule;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class PlayerController {
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;

    private int movementVariable = 4; //movement speed

    private MainModule mainModule;
    private Scene primaryScene;

    private boolean tempMovementW = true;
    private boolean tempMovementS = true;
    private boolean tempMovementA = true;
    private boolean tempMovementD = true;


    public PlayerController(Stage primaryStage, MainModule mainModule) {
        this.mainModule = mainModule;
        this.primaryScene = primaryStage.getScene();
        initialize();
    }

    public void start() {
        mainModule.playerView.setLayoutY(240);
        mainModule.playerView.setLayoutX(0);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {

                //Movement allowance moving only possible if set to true
                tempMovementW = true;
                tempMovementS = true;
                tempMovementA = false;
                tempMovementD = false;


                if (wPressed && tempMovementW) {
                    mainModule.playerView.setLayoutY(mainModule.playerView.getLayoutY() - movementVariable);
                }
                if (sPressed && tempMovementS) {
                    mainModule.playerView.setLayoutY(mainModule.playerView.getLayoutY() + movementVariable);
                }
                if (aPressed && tempMovementA) {
                    mainModule.playerView.setLayoutX(mainModule.playerView.getLayoutX() - movementVariable);
                }
                if (dPressed && tempMovementD) {
                    mainModule.playerView.setLayoutX(mainModule.playerView.getLayoutX() + movementVariable);
                }
            }
        };
        timer.start();
    }

    private void initialize() {
        movementSetup();
    }

    // Set temporary movement flag
    private void movementSetup() {
        primaryScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) {
                wPressed = true;
                tempMovementW = true;
            }
            if (e.getCode() == KeyCode.S) {
                sPressed = true;
                tempMovementS = true;
            }
            if (e.getCode() == KeyCode.A) {
                aPressed = true;
                tempMovementA = true;
            }
            if (e.getCode() == KeyCode.D) {
                dPressed = true;
                tempMovementD = true;
            }
        });

        primaryScene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.W) {
                wPressed = false;
                tempMovementW = false;
            }
            if (e.getCode() == KeyCode.S) {
                sPressed = false;
                tempMovementS = false;
            }
            if (e.getCode() == KeyCode.A) {
                aPressed = false;
                tempMovementA = false;
            }
            if (e.getCode() == KeyCode.D) {
                dPressed = false;
                tempMovementD = false;
            }
        });
    }
}
