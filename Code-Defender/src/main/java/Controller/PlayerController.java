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
import Module.EnemyAttributes;
import Module.Laser;

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
    private List<EnemyAttributes> enemyAttributesList;

    private ArrayList<Laser> lasers = new ArrayList<>(); // ArrayList to hold Enemy objects

    // Method to set the enemy attributes list
    public void setEnemyAttributesList(List<EnemyAttributes> enemyAttributesList) {
        this.enemyAttributesList = enemyAttributesList;
    }

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

            int enemyNumber = 0;
            for (EnemyAttributes enemyAttributes : enemyAttributesList) {
                double enemySpeed = enemyAttributes.getSpeed();
                int enemyDamage = enemyAttributes.getDamage();
                int enemyHitpoints = enemyAttributes.getHitpoints();
                String enemyType = enemyAttributes.getType();

                enemyNumber = Integer.parseInt(enemyType);
            }

            // Update the layout coordinates for the enemy's ImageView
            enemyView.setLayoutX(newX);
            enemyView.setLayoutY(newY);

            // Check for collisions
            collisionsCheck(enemyViews, enemyView, iterator, enemyNumber);

            // Now you have access to all attributes of the current enemy, you can perform any additional actions here
        }
    }







    public void collisionsCheck(List<ImageView> enemyViews, ImageView enemyView, Iterator<Enemy> iterator, int ENNumber) {
        // Check if the enemy is within the visible area of the screen

            for (ImageView enemyV : enemyViews) {

                    // Check for collision only if the player's bounds intersect with the enemy's bounds
                    if (mainModule.playerView.getBoundsInParent().intersects(enemyV.getBoundsInParent())) {
                        try {

                            kill(enemyViews, enemyView, iterator, enemyV, ENNumber);

                        } catch (Exception e) {
                            System.out.println(" ");
                        }


                        System.out.println("Player collided with an enemy!");
                        return; // Exit loop after handling collision with the current enemy
                    }



            }

    }


    public void addPointsPerDefeat(int ENumber){
        System.out.println(ENumber);

        switch (ENumber)
        {
            case 1: //melee
                pointCounter.count += 100;
                pointCounter.updateCounter();
                break;
            case 2: //ranged
                pointCounter.count += 125;
                pointCounter.updateCounter();
                break;
            case 3: //armored_ranged
                pointCounter.count += 200;
                pointCounter.updateCounter();
                break;
            case 4: //armored_melee
                pointCounter.count += 175;
                pointCounter.updateCounter();
                break;
            case 5: //ship
                pointCounter.count += 400;
                pointCounter.updateCounter();
                break;
                case 6: //speedster
                pointCounter.count += 300;
                pointCounter.updateCounter();
                break;
        }

    }


    public void kill(List<ImageView> enemyViews, ImageView enemyView, Iterator<Enemy> iterator, ImageView enemyV, int ENNumber) {
            addPointsPerDefeat(ENNumber);

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

    public void initializeLaser(ImageView laserView, double laserX, double laserY) {
        // Create and initialize the laser
        Laser laser = new Laser(mainModule.laserView.getImage(), mainModule.laserSpeed, mainModule.laserDamage, mainModule.chosenLaser);
        lasers.add(laser);

        // Set the layout coordinates for the laser's ImageView
        laserView.setLayoutX(laserX);
        laserView.setLayoutY(laserY);
    }
}
