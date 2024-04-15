package Controller;

import Module.MainModule;
import Module.Enemy;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;

public class PlayerController {
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;

    // Enemy
    private Enemy enemy;
    private double enemyX;
    private double enemyY;

    // Movement allowance
    // (Assuming tempMovementW, tempMovementS, tempMovementA, tempMovementD
    // control player movement only)
    private boolean tempMovementW = true; // Up
    private boolean tempMovementS = true; // Down
    private boolean tempMovementA = false; // Left
    private boolean tempMovementD = false; // Right

    private int movementVariable = 6; // Movement speed

    private MainModule mainModule;
    private Scene primaryScene;

    private double sceneWidth;
    private double sceneHeight;

    public PlayerController(Stage primaryStage, MainModule mainModule) {
        this.mainModule = mainModule;
        this.primaryScene = primaryStage.getScene();
        updateSceneSize();
        initialize();
        initializeEnemy();
    }

    public void start() {
        mainModule.playerView.setLayoutY(240);
        mainModule.playerView.setLayoutX(-3);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                movePlayer();
                moveEnemy();
            }
        };
        timer.start();
    }

    private void initialize() {
        movementSetup();
        // Add listeners to scene width and height properties to update scene size
        primaryScene.widthProperty().addListener((obs, oldVal, newVal) -> updateSceneSize());
        primaryScene.heightProperty().addListener((obs, oldVal, newVal) -> updateSceneSize());
    }

    private void initializeEnemy() {
        //maybe use arrayList to create and edit or clear multiple enemies
        enemy = new Enemy(mainModule.enemyView.getImage(), mainModule.enemySpeed, mainModule.enemyDamage, mainModule.enemyHitpoints,mainModule.chosenEnemy);
        enemyX = sceneWidth; // Start enemy on the right side
        enemyY = 240; // Adjust as needed
        mainModule.enemyView.setLayoutX(enemyX);
        mainModule.enemyView.setLayoutY(enemyY);
    }

    private void updateSceneSize() {
        sceneWidth = primaryScene.getWidth();
        sceneHeight = primaryScene.getHeight();
    }

    private void movePlayer() {
        double newX = mainModule.playerView.getLayoutX();
        double newY = mainModule.playerView.getLayoutY();

        if (wPressed && tempMovementW && newY > 0) {
            newY -= movementVariable;
        }
        if (sPressed && tempMovementS && newY < sceneHeight - mainModule.playerView.getBoundsInParent().getHeight()) {
            newY += movementVariable;
        }
        if (aPressed && tempMovementA && newX > 0) {
            newX -= movementVariable;
        }
        if (dPressed && tempMovementD && newX < sceneWidth - mainModule.playerView.getBoundsInParent().getWidth()) {
            newX += movementVariable;
        }

        mainModule.playerView.setLayoutX(newX);
        mainModule.playerView.setLayoutY(newY);
    }

    private void moveEnemy() {
        double newX = enemyX;
        double newY = enemyY;

        // Move enemy towards left
        newX -= enemy.getSpeed();
        mainModule.enemyView.setLayoutX(newX);
        enemyX = newX;

        // Check for collisions with player (assuming player is a rectangle)
        if (mainModule.playerView.getBoundsInParent().intersects(mainModule.enemyView.getBoundsInParent())) {
            // Handle collision
            System.out.println("Enemy collided with player!");
        }
    }

    // Setup for movement, DO NOT CHANGE!
    private void movementSetup() {
        primaryScene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.W) wPressed = true;
            else if (code == KeyCode.S) sPressed = true;
            else if (code == KeyCode.A) aPressed = true;
            else if (code == KeyCode.D) dPressed = true;
        });

        primaryScene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.W) wPressed = false;
            else if (code == KeyCode.S) sPressed = false;
            else if (code == KeyCode.A) aPressed = false;
            else if (code == KeyCode.D) dPressed = false;
        });
    }
}
