package Module;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import Controller.PlayerController;
import Module.MainModule;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Spawner {
    private Pane root;
    private MainModule mainModule;
    private Stage primaryStage;
    private PlayerController playerController;

    public boolean keepSpawning = true;
    private int spawnTime = 5; // Initial spawn time in 5 seconds
    private long lastSpawnTime = 0;
    private long delay = 1_000_000_000;

    private List<EnemyAttributes> enemyAttributesList = new ArrayList<>();

    private ArrayList<ImageView> enemyViews = new ArrayList<>(); // Track enemy ImageViews
    private Rectangle deleteBoundary; // Delete boundary for enemies behind the screen

    private List<LaserAttributes> laserAttributesList = new ArrayList<>();

    private ArrayList<ImageView> laserViews = new ArrayList<>(); // Track laser ImageViews

    public Spawner(Stage primaryStage, Pane root, MainModule mainModule, PlayerController playerController) {
        this.root = root;
        this.mainModule = mainModule;
        this.primaryStage = primaryStage;
        this.playerController = playerController;
        playerController.setEnemyAttributesList(enemyAttributesList);

        // Initialize delete boundary
        deleteBoundary = new Rectangle(-200, 0, 100, primaryStage.getHeight()); // Adjust dimensions as needed x is first
        deleteBoundary.setFill(Color.TRANSPARENT); // Make it transparent
        root.getChildren().add(deleteBoundary);

        // Start the spawning loop
        startSpawning();
    }

    public void startSpawning() {
        AnimationTimer spawnerTimer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                if (timestamp - lastSpawnTime >= spawnTime * 1_000_000_000 + delay && keepSpawning) {
                    spawnEnemy();
                    lastSpawnTime = timestamp;

                    if (playerController.chosenEnemy == 3 || playerController.chosenEnemy == 12)
                    {
                        keepSpawning = false;
                    }

                    if (delay <= 250_000_000) {
                        delay -= 5_000_000; //delay until the next enemy spawns (spawn time gets faster)
                        //System.out.println(delay);
                    }
                }
            }
        };
        spawnerTimer.start();
    }

    private void spawnEnemy() {
            // Create a new ImageView for the enemy
        mainModule.loadEnemyAttributes(playerController.chosenEnemy);
            ImageView enemyView = new ImageView();

            // Load the enemy image from the file path
            Image enemyImage = new Image(getClass().getResourceAsStream(mainModule.enemyImage));

            // Set the loaded image to the enemy ImageView
            enemyView.setImage(enemyImage);
        double enemyX = primaryStage.getWidth(); // Spawn at the right edge of the screen
        double enemyY = Math.random() * (primaryStage.getHeight() - enemyView.getImage().getHeight());

        enemyY = Math.max(enemyY, 0); // Ensure enemy is not spawned above the screen
        enemyY = Math.min(enemyY, primaryStage.getHeight() - enemyView.getImage().getHeight()); // Ensure enemy is not spawned below the screen

        // Retrieve attributes from MainModule class
        double enemySpeed = mainModule.enemySpeed;
        int enemyDamage = mainModule.enemyDamage;
        int enemyHitpoints = mainModule.enemyHitpoints;
        String enemyType = String.valueOf(mainModule.chosenEnemy);

        EnemyAttributes enemyAttributes = new EnemyAttributes(enemySpeed, enemyDamage, enemyHitpoints, enemyType);
        enemyAttributesList.add(enemyAttributes);

        playerController.initializeEnemy(enemyView, enemyX, enemyY);
        root.getChildren().add(enemyView);

        // Add the enemy's ImageView to the list
        enemyViews.add(enemyView);

        AnimationTimer enemyMovement = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double newX = enemyView.getLayoutX() - 1; // Adjust speed as needed
                enemyView.setLayoutX(newX);

                // Check if the enemy touches or crosses the delete boundary
                if (enemyView.getBoundsInParent().intersects(deleteBoundary.getBoundsInParent())) {
                    delete(enemyViews, enemyView);
                }
            }
        };
        enemyMovement.start();
    }


    private void delete(List<ImageView> enemyViews, ImageView enemyView) {
        root.getChildren().remove(enemyView);
        enemyViews.remove(enemyView);
    }

    public List<ImageView> getEnemyViews() {
        return enemyViews;
    }

    // Method to set the PlayerController instance
    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    private long lastLaserFireTime = 0;

    public void spawnLaser() {
        long currentTime = System.nanoTime();
        if(currentTime - lastLaserFireTime >= 500_000_000){
            ImageView laserView = new ImageView(mainModule.laserView.getImage());
            double laserX = mainModule.playerView.getLayoutX() + 60; // Spawn laser on player
            double laserY = mainModule.playerView.getLayoutY() + 29;

            laserY = Math.max(laserY, 0); // Ensure laser is not spawned above the screen
            laserY = Math.min(laserY, primaryStage.getHeight() - laserView.getImage().getHeight()); // Ensure laser is not spawned below the screen

            // Retrieve attributes from MainModule class
            double laserSpeed = mainModule.laserSpeed;
            int laserDamage = mainModule.laserDamage;
            String laserType = String.valueOf(mainModule.chosenLaser);

            LaserAttributes laserAttributes = new LaserAttributes(laserSpeed, laserDamage, laserType);
            laserAttributesList.add(laserAttributes);

            playerController.initializeLaser(laserView, laserX, laserY);
            root.getChildren().add(laserView);

            // Add the laser's ImageView to the list
            laserViews.add(laserView);
            mainModule.playSound("laser");

            AnimationTimer laserMovement = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    double newX = laserView.getLayoutX() + 1; // Adjust speed as needed
                    laserView.setLayoutX(newX);

                    // Check if the laser touches or crosses the delete boundary
                    if (laserView.getBoundsInParent().intersects(deleteBoundary.getBoundsInParent())) {
                        delete(laserViews, laserView);
                    }
                }
            };
            laserMovement.start();

            lastLaserFireTime = currentTime;
        }
    }

    public List<ImageView> getLaserViews() {
        return laserViews;
    }
}
