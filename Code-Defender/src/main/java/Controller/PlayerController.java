package Controller;

import Module.Enemy;
import Module.MainModule;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import Module.Spawner;

import java.util.ArrayList;
import java.util.List;

public class PlayerController {
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;

    private boolean tempMovementW = true;
    private boolean tempMovementA = false;
    private boolean tempMovementS = true;
    private boolean tempMovementD = false;

    private ArrayList<Enemy> enemies = new ArrayList<>(); // ArrayList to hold Enemy objects

    private int movementVariable = 2; // Movement speed

    private MainModule mainModule;
    private Scene primaryScene;
    private double sceneWidth;
    private double sceneHeight;

    private Spawner spawner;

    // Constructor
    public PlayerController(Stage primaryStage, MainModule mainModule, Spawner spawner) {
        this.mainModule = mainModule;
        this.primaryScene = primaryStage.getScene();
        this.spawner = spawner;
        updateSceneSize();
        initialize();
    }

    // Setter method for Spawner
    public void setSpawner(Spawner spawner) {
        this.spawner = spawner;
    }
    public void initializeEnemy(ImageView enemyView, double enemyX, double enemyY) {
        // Create and initialize the enemy
        Enemy enemy = new Enemy(mainModule.enemyView.getImage(), mainModule.enemySpeed, mainModule.enemyDamage, mainModule.enemyHitpoints, mainModule.chosenEnemy);
        enemies.add(enemy);
        // Set the layout coordinates for the enemy's ImageView
        enemyView.setLayoutX(enemyX);
        enemyView.setLayoutY(enemyY);
    }



    public void start() {
        mainModule.playerView.setLayoutY(240);
        mainModule.playerView.setLayoutX(-3);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                movePlayer();
                moveEnemies(); // Move all enemies
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

    private void moveEnemies() {
        for (Enemy enemy : enemies) {
            ImageView enemyView = enemy.getImageView();
            double newX = enemyView.getLayoutX() - 1; // Adjust speed as needed
            double newY = enemyView.getLayoutY(); // Keep Y position unchanged

            // Update the layout coordinates for the enemy's ImageView
            enemyView.setLayoutX(newX);
            enemyView.setLayoutY(newY);

            // Check for collisions with player (assuming player is a rectangle)
            List<ImageView> enemyViews = spawner.getEnemyViews();
            for (ImageView enemyV : enemyViews) {
                if (mainModule.playerView.getBoundsInParent().intersects(enemyV.getBoundsInParent())) {
                    // Handle collision between player and current enemy
                    System.out.println("Player collided with an enemy!");
                }
            }

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
