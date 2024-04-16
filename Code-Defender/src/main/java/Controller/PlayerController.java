package Controller;

import Module.Enemy;
import Module.MainModule;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import Module.Spawner;
import Module.PointCounter;

import java.util.ArrayList;
import java.util.Iterator;
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
private Pane root;
    private Spawner spawner;
    private PointCounter pointCounter;

    // Constructor
    public PlayerController(Stage primaryStage, MainModule mainModule, Spawner spawner, Pane root) {
        this.mainModule = mainModule;
        this.primaryScene = primaryStage.getScene();
        this.spawner = spawner;
        updateSceneSize();
        initialize();
        this.root = root;
        this.pointCounter = null;
    }

    // Setter method for PointCounter
    public void setPointCounter(PointCounter pointCounter) {
        this.pointCounter = pointCounter;
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
        mainModule.playerView.setLayoutX(2);

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
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            ImageView enemyView = enemy.getImageView();
            double newX = enemyView.getLayoutX() - 1;
            double newY = enemyView.getLayoutY();

            List<ImageView> enemyViews = spawner.getEnemyViews();

            // Update the layout coordinates for the enemy's ImageView
            enemyView.setLayoutX(newX);
            enemyView.setLayoutY(newY);

            if (newX + enemyView.getBoundsInParent().getWidth() <= 0) { //// doesnt work
                for (ImageView enemyV : enemyViews) {
                    if (enemyV == enemyView) {
                        delete(enemyViews, enemyView, iterator, enemyV);
                        System.out.println("behind 0");
                        break;
                    }
                }
            } else {
                collisionsCheck(enemyViews, enemyView, iterator);
            }
        }
    }






    public void collisionsCheck(List<ImageView> enemyViews, ImageView enemyView, Iterator<Enemy> iterator) {
        // Check if the enemy is within the visible area of the screen
        if (enemyView.getLayoutX() + enemyView.getBoundsInParent().getWidth() >= 0) {
            for (ImageView enemyV : enemyViews) {
                // Check for collision only if the player's bounds intersect with the enemy's bounds
                if (mainModule.playerView.getBoundsInParent().intersects(enemyV.getBoundsInParent())) {
                    // Handle collision between player and current enemy
                    delete(enemyViews, enemyView, iterator, enemyV);
                    try {
                        delete(enemyViews, enemyView, iterator, enemyV);
                    }
                    catch(Exception e) {
                        System.out.println(" ");
                    }

                    System.out.println("Player collided with an enemy!");
                    return; // Exit loop after handling collision with the current enemy
                }
            }
        }
    }


    public void delete(List<ImageView> enemyViews, ImageView enemyView, Iterator<Enemy> iterator, ImageView enemyV)
    {
        iterator.remove();

        root.getChildren().remove(enemyView);
        root.getChildren().remove(enemyV);

        enemyViews.remove(enemyView);
        enemyViews.remove(enemyV);

        spawner.getEnemyViews().remove(enemyV);
        spawner.getEnemyViews().remove(enemyView);

        enemies.remove(enemyV);
        enemies.remove(enemyView);
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
