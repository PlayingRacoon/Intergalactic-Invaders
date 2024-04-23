package Controller;

import Module.Enemy;
import Module.MainModule;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import Module.Spawner;
import Module.PointCounter;
import Module.EnemyAttributes;
import Module.Laser;
import javafx.util.Duration;
import Module.SpawnWave;

import java.util.*;

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
    private String openNext = "shop";

    private MainModule mainModule;
    private Scene primaryScene;
    private double sceneWidth;
    private double sceneHeight;
    private Pane root;
    private Spawner spawner;
    private PointCounter pointCounter;
    private List<EnemyAttributes> enemyAttributesList;
    SpawnWave spawnWave = new SpawnWave();
    boolean kal = false;

    private int bossKilled = 0;
    private Map<ImageView, EnemyAttributes> enemyAttributesMap;

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
        this.enemyAttributesMap = spawner.getEnemyAttributesMap();

        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            ImageView enemyView = enemy.getImageView();
            double newX = enemyView.getLayoutX() - 1;
            double newY = enemyView.getLayoutY();

            List<ImageView> enemyViews = spawner.getEnemyViews();


            for (EnemyAttributes enemyAttributes : enemyAttributesList) {
                double enemySpeed = enemyAttributes.getSpeed();
                int enemyDamage = enemyAttributes.getDamage();
                int enemyHitpoints = enemyAttributes.getHitpoints();
                String enemyType = enemyAttributes.getType();
            }


            // Update the layout coordinates for the enemy's ImageView
            enemyView.setLayoutX(newX);
            enemyView.setLayoutY(newY);

            // Check for collisions
            collisionsCheck(enemyViews, enemyView, iterator);

            // Now you have access to all attributes of the current enemy, you can perform any additional actions here

            collisionsCheckLaser(enemyViews, enemyView, iterator);
        }
    }







    public void collisionsCheck(List<ImageView> enemyViews, ImageView enemyView, Iterator<Enemy> iterator) {
        // Check if the enemy is within the visible area of the screen

            for (ImageView enemyV : enemyViews) {

                    // Check for collision only if the player's bounds intersect with the enemy's bounds
                    if (mainModule.playerView.getBoundsInParent().intersects(enemyV.getBoundsInParent()) || kal) {

                        int ENNumber;
                        ENNumber = spawner.enemyAttributesMap.get(enemyV).getEnemyNumber();


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

    public void collisionsCheckLaser(List<ImageView> enemyViews, ImageView enemyView, Iterator<Enemy> iterator) {
        // Check if the enemy is within the visible area of the screen
        List<ImageView> laserViews=spawner.getLaserViews();
        for (ImageView laserV : laserViews) {
            // Check if the laser has already been removed
            if (!root.getChildren().contains(laserV)) {
                continue; // Skip this laser if it has already been removed
            }
            for (ImageView enemyV : enemyViews) {
                // Check for collision only if the laser's bounds intersect with the enemy's bounds
                if (laserV.getBoundsInParent().intersects(enemyV.getBoundsInParent()) || kal) {

                    int ENNumber;
                    ENNumber = spawner.enemyAttributesMap.get(enemyV).getEnemyNumber();

                    try {
                        kill(enemyViews, enemyView, iterator, enemyV, ENNumber);
                        lasers.remove(laserV);
                        root.getChildren().remove(laserV);
                    } catch (Exception e) {
                        System.out.println(" ");
                    }
                    System.out.println("Laser collided with an enemy!");
                    return; // Exit loop after handling collision with the current enemy
                }
            }
        }
    }



    public void addPointsPerDefeat(int ENumber){
        System.out.println("added points" + ENumber);
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
                //standard 400
                pointCounter.updateCounter();
                break;
                case 6: //speedster
                pointCounter.count += 300;
                pointCounter.updateCounter();
                break;

        }

        if (pointCounter.count >= pointCounter.tempSavePointNumber + 5000 && openNext != null)
        {
            if (openNext.equals("shop"))
            {
                killAllEnemies();
                spawner.keepSpawning = false;
                //chosenEnemy = 11; mach enemy 1mal zum 11er boss
                //spawnWave.wave = 2;
            } else if (openNext.equals("navigation")) {
                //chosenEnemy = 12; mach enemy 1mal zum 12er boss
                //spawnWave.wave = 1;
            }
            pointCounter.tempSavePointNumber = pointCounter.count;
        }

    }

    public void killAllEnemies() {
        kal = true;
    }


    public void kill(List<ImageView> enemyViews, ImageView enemyView, Iterator<Enemy> iterator, ImageView enemyV, int ENNumber) {

        // Play explosion sound
        mainModule.playSound("explosion");

        if (ENNumber == 11){

            //open shop instead
            openShop();

                kal = false;
                spawner.keepSpawning = true;
                openNext = "navigation";
                //get to spawnWave after

        } else if (ENNumber == 12) {

            kal = false;
            spawner.keepSpawning = true;
            openNext = "shop";

        }

        if (!kal) {
            // Add points based on enemy type
            addPointsPerDefeat(ENNumber);
        }

        // Load GIF file
        double gifX = enemyV.getLayoutX();
        double gifY = enemyV.getLayoutY();
        Image gifImage = new Image(getClass().getResource("/graphics/png/explosion.gif").toString());
        ImageView gifView = new ImageView(gifImage);
        gifView.setLayoutX(gifX);
        gifView.setLayoutY(gifY);
        root.getChildren().add(gifView);

        // Remove the GIF view after 5 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(e -> {
            root.getChildren().remove(gifView);
        });
        pause.play();

        // Remove enemy and its view from the scene
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




    private void openShop() {
        // Retrieve the width and height of the primaryStage
        double windowWidth = primaryScene.getWidth();
        double windowHeight = primaryScene.getHeight();

        // Load the shop image
        Image shopImage = new Image(getClass().getResourceAsStream("/graphics/png/shop.png"));

        // Create an ImageView for the shop image
        ImageView shopView = new ImageView(shopImage);

        // Set the size of the ImageView to match the size of the window
        shopView.setFitWidth(windowWidth);
        shopView.setFitHeight(windowHeight);

        // Add the ImageView to the root pane
        root.getChildren().add(shopView);

        // Handle mouse click to close the shop
        shopView.setOnMouseClicked(e -> root.getChildren().remove(shopView));
    }





    // Setup for movement, DO NOT CHANGE!
    private void movementSetup() {

        primaryScene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (e.getCode().toString().equals("SPACE")) {
                spawner.spawnLaser();
            }
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

    public static void delay(int milliseconds) {
        try {
            // Sleep the current thread for the specified duration
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // Handle interrupted exception if necessary
            e.printStackTrace();
        }
    }
}
