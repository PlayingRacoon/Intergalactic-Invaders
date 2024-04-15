package Module;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Controller.PlayerController;
import Module.MainModule;

import java.util.ArrayList;
import java.util.List;

public class Spawner {
    private Pane root;
    private MainModule mainModule;
    private Stage primaryStage;
    private PlayerController playerController;

    private int spawnTime = 5; // Initial spawn time in seconds
    private long lastSpawnTime = 0;

    private ArrayList<ImageView> enemyViews = new ArrayList<>(); // Track enemy ImageViews

    public Spawner(Stage primaryStage, Pane root, MainModule mainModule, PlayerController playerController) {
        this.root = root;
        this.mainModule = mainModule;
        this.primaryStage = primaryStage;
        this.playerController = playerController;

        // Start the spawning loop
        startSpawning();
    }

    public void startSpawning() {
        AnimationTimer spawnerTimer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                if (timestamp - lastSpawnTime >= spawnTime * 1_000_000_000) {
                    spawnEnemy();
                    lastSpawnTime = timestamp;
                }
            }
        };
        spawnerTimer.start();
    }

    private void spawnEnemy() {
        ImageView enemyView = new ImageView(mainModule.enemyView.getImage());
        double enemyX = primaryStage.getWidth(); // Spawn at the right edge of the screen
        double enemyY = Math.random() * (primaryStage.getHeight() - enemyView.getImage().getHeight());

        enemyY = Math.max(enemyY, 0); // Ensure enemy is not spawned above the screen
        enemyY = Math.min(enemyY, primaryStage.getHeight() - enemyView.getImage().getHeight()); // Ensure enemy is not spawned below the screen

        playerController.initializeEnemy(enemyView, enemyX, enemyY);
        root.getChildren().add(enemyView);

        // Add the enemy's ImageView to the list
        enemyViews.add(enemyView);

        AnimationTimer enemyMovement = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double newX = enemyView.getLayoutX() - 1; // Adjust speed as needed
                enemyView.setLayoutX(newX);

                if (newX + enemyView.getBoundsInParent().getWidth() < 0) {
                    root.getChildren().remove(enemyView);
                    this.stop(); // Stop the movement animation
                }
            }
        };
        enemyMovement.start();
    }

    // Method to get the list of enemy ImageViews
    public List<ImageView> getEnemyViews() {
        return enemyViews;
    }
}
