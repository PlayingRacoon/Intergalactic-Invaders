package Controller;

import Module.Enemy;
import Module.MainModule;
import View.Screen;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import Module.Spawner;
import Module.PointCounter;
import Module.EnemyAttributes;
import Module.Laser;
import javafx.util.Duration;
import Module.SpawnWave;
import Module.Shop;

import java.io.*;
import java.util.*;

public class PlayerController {
    public boolean playerLaser=false;

    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;

    private boolean tempMovementW = true;
    private boolean tempMovementA = false;
    private boolean tempMovementS = true;
    private boolean tempMovementD = false;

    public boolean dead =false;

    private ArrayList<Enemy> enemies = new ArrayList<>(); // ArrayList to hold Enemy objects

    private int movementVariable = 2; // Movement speed
    private String openNext = "shop";

    private MainModule mainModule;
    private Scene primaryScene;
    private double sceneWidth;
    private double sceneHeight;
    private Pane root;
    private Spawner spawner;
    public PointCounter pointCounter;
    private List<EnemyAttributes> enemyAttributesList;
    SpawnWave spawnWave = new SpawnWave();

    private Stage primaryStage;
    public boolean kal = false;
    public boolean kalp;

    private int bossKilled = 0;
    private Map<ImageView, EnemyAttributes> enemyAttributesMap;

    private ArrayList<Laser> lasers = new ArrayList<>(); // ArrayList to hold Enemy objects
    public String currentPlanet;
    public boolean shopOpen = false;
    Screen screen;

    public int hitpoints=3;

    // Method to set the enemy attributes list
    public void setEnemyAttributesList(List<EnemyAttributes> enemyAttributesList) {
        this.enemyAttributesList = enemyAttributesList;
    }

    // Constructor
    public PlayerController(Stage primaryStage, MainModule mainModule, Spawner spawner, Pane root, Screen screen) {
        this.mainModule = mainModule;
        this.primaryScene = primaryStage.getScene();
        this.spawner = spawner;
        updateSceneSize();
        initialize();
        this.root = root;
        this.pointCounter = null;
        this.screen = screen;

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

        screen.setBackgroundImage("/graphics/png/backrounds/earth.png", this);
        PlayerTofront();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                collisionsCheckEnemyLaser();
                movePlayer();
                moveEnemies(); // Move all enemies
                if (hitpoints < 1) {
                    killplayer(); // Call your method here
                }
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
            double newX = enemyView.getLayoutX() - mainModule.enemySpeed;
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

            collisionsCheckLaser(enemyViews, enemyView, iterator);
        }
    }


    public void collisionsCheck(List<ImageView> enemyViews, ImageView enemyView, Iterator<Enemy> iterator) {
        // Check if the enemy is within the visible area of the screen

        for (ImageView enemyV : enemyViews) {

            // Check for collision only if the player's bounds intersect with the enemy's bounds
            if (mainModule.playerView.getBoundsInParent().intersects(enemyV.getBoundsInParent()) || kalp || kal ){

                int ENNumber;
                ENNumber = spawner.enemyAttributesMap.get(enemyV).getEnemyNumber();

                if (kalp) {
                    hitpoints = hitpoints - 5;
                    System.out.println(hitpoints);
                }

                try {
                    kill(enemyViews, enemyView, iterator, enemyV, ENNumber);

                } catch (Exception e) {
                    System.out.println(" ");
                }


                if (!kal) {
                    System.out.println("Player collided with an enemy!");
                }
                return; // Exit loop after handling collision with the current enemy
            }
        }

    }

    public void collisionsCheckLaser(List<ImageView> enemyViews, ImageView enemyView, Iterator<Enemy> iterator) {
        // Check if the enemy is within the visible area of the screen
        List<ImageView> laserViews = spawner.getLaserViews();
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
                    return; // Exit loop after handling collision with the current enemy
                }
            }
        }
    }
    public void collisionsCheckEnemyLaser() {
        List<ImageView> enemylasers = spawner.getEnemyLaserViews();
        Iterator<ImageView> iterator = enemylasers.iterator();
        while (iterator.hasNext()) {
            ImageView laserV = iterator.next();
            if (laserV.getBoundsInParent().intersects(mainModule.playerView.getBoundsInParent())) {
                System.out.println("player collided with a laser!");
                hitpoints--;
                System.out.println(hitpoints);
                iterator.remove();
                root.getChildren().remove(laserV);
            }
        }
    }



    public void addPointsPerDefeat(int ENumber) {

        switch (ENumber) {
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

        //DEV TEST OPTIONS

        //killAllEnemies();
        //spawner.keepSpawning = false;
        //currentPlanet = "earth";
        //mainModule.curPlanet = currentPlanet;

        //openShopFromTextFile(currentPlanet);
        //shopCounter.setDisplayCounter(true);
        //openStarMap();

//5000 pointsInfo
        if (pointCounter.count >= pointCounter.tempSavePointNumber + 10 && openNext != null) {
            if (openNext.equals("shop")) {
                killAllEnemies();
                currentPlanet = "earth";
                mainModule.curPlanet = currentPlanet;
                spawner.bossChosenFürWave = 11;



                //spawner.keepSpawning = false;
                //spawnWave.wave = 2;
            } else if (openNext.equals("navigation")) {
                //spawnWave.bossChosen  = 12; mach enemy 1mal zum 12er boss
                //spawnWave.wave = 1;
                openStarMap();
            }
            pointCounter.tempSavePointNumber = pointCounter.count;
        }

    }

    private void openStarMap() {
        StarMap starMap = new StarMap(root, sceneWidth, sceneHeight, this, screen); // i love that, THIS!
        // You might want to keep a reference to the StarMap object if you need to remove it later
        // e.g., this.starMap = starMap;
    }

    public void changePlayerImage(String imagePath) {
        screen.backgroundImage = null;
        screen.backgroundView = null;
        Image newPlayerImage = new Image(getClass().getResourceAsStream(imagePath));
        mainModule.playerView.setImage(newPlayerImage);

    }

    public void evaluateCurrentPlanet(int PlanetNumber)
    {
        String temp = " ";

        switch (PlanetNumber)
        {
            case 1:
                temp = "earth";
                break;

            case 2:
                temp = "birmingham";
            break;

            case 3:
                temp = "insulam-118";
                break;

            case 4:
                temp = "kepler-22b";
                break;

            case 5:
                temp = "kepler-89c";
                break;

            case 6:
                temp = "mons-25";
                break;

            case 7:
                temp = "palus-445";
                break;

            case 8:
                temp = "proxima_centauri_b";
                break;

            case 9:
                temp = "sargasso";
                break;

            case 10:
                temp = "silva-114";
                break;
        }

        currentPlanet = temp;
        mainModule.curPlanet = temp;
    }

    public void PlayerTofront()
    {
        mainModule.playerView.toFront();
    }




    public void killAllEnemies() {
        kal = true;

    }


    public void kill(List<ImageView> enemyViews, ImageView enemyView, Iterator<Enemy> iterator, ImageView enemyV, int ENNumber) {

        // Play explosion sound
        mainModule.playSound("explosion");

        if (ENNumber == 11) {

            //open shop instead
            openShopFromTextFile(currentPlanet);
            shopCounter.setDisplayCounter(true);


            kal = false;
            dead=false;
            kalp=false;
            spawner.bossChosenFürWave = 0;
            openNext = "navigation";
            //get to spawnWave after

        } else if (ENNumber == 12) {

            kal = false;
            dead=false;
            kalp=false;
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

private Button shopButton;
    private ImageView openShop() {
        // Retrieve the width and height of the primaryStage
        double windowWidth = primaryScene.getWidth();
        double windowHeight = primaryScene.getHeight();


        // Load the shop image
        Image shopImage = new Image(getClass().getResourceAsStream("/graphics/png/shops/shop_" + currentPlanet + ".png"));
        ImageView shopView = new ImageView(shopImage);

        // Set the size of the ImageView to match the size of the window
        shopView.setFitWidth(windowWidth);
        shopView.setFitHeight(windowHeight);

        // Add the ImageView to the root pane
        root.getChildren().add(shopView);

        // Create a Button for the shop
        Button shopButton = new Button();

        // Load the button image
        Image buttonImage = new Image(getClass().getResourceAsStream("/graphics/png/shops/shopButton.gif"));
        ImageView buttonImageView = new ImageView(buttonImage);

        // Set the graphic of the button
        shopButton.setGraphic(buttonImageView);

        // Set the size of the button (adjust as needed)


        // Position the button in the top-right corner of the shop image
        double buttonX = sceneWidth / 2 + 490;
        double buttonY = sceneHeight / 2 + 40; // Adjust X position as needed
        shopButton.setLayoutX(buttonX);
        shopButton.setLayoutY(buttonY);

        ImageView outerShopView = shopView;
        // Add event handling to the button
        shopButton.setOnAction(e -> {
            System.out.println("Button clicked!");
            root.getChildren().remove(shopButton);

            kal=false;
            kalp=false;
            dead=false;
            spawner.keepSpawning=true;

            root.getChildren().remove(outerShopView);
            shopCounter.setDisplayCounter(false);
            delView();
        });



        // Add the button to the root pane
        root.getChildren().add(shopButton);

        return shopView;
    }

    public PointCounter shopCounter;
    public void shopPointCounter(AnchorPane root){
        shopCounter = new PointCounter(root);
        shopCounter.setDisplayCounter(false);
        double labelX = root.getWidth() - 260; // Adjust X position as needed
        double labelY = root.getHeight() / 3 + 34; // Adjust Y position as needed
        AnchorPane.setLeftAnchor(shopCounter.counterLabel, labelX);
        AnchorPane.setTopAnchor(shopCounter.counterLabel, labelY);

        shopCounter.count = pointCounter.count;
        shopCounter.updateCounter();
        shopCounter.counterLabel.toFront();
    }

    int layoutX = -100;

    Random random = new Random();
    private int slotsCreated = 0;
    private ArrayList<Integer> existingSlots = new ArrayList<Integer>();

    private void openShopFromTextFile(String currentPlanet) {

        try {
            ImageView outerShopImage = null;

            InputStream inputStream = getClass().getResourceAsStream("/JSON/planets.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            int slotCount = 0;
            boolean planetFound = false;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                // Check if the line contains the current planet
                if (line.startsWith("/planet:") && line.contains(currentPlanet)) {
                    planetFound = true;

                    outerShopImage = openShop();

                    continue; // Skip to the next line after finding the current planet
                }

                if (planetFound) {
                    // Check if the line contains the next planet delimiter
                    if (line.equals("++++")) {

                        break; // Stop reading after encountering the next planet delimiter
                    }

                        // Check if the line contains the slot count
                        if (line.startsWith("/slots:")) {
                            slotCount = Integer.parseInt(line.substring("/slots:".length()).trim());

                            // Call the callSlot method with the slot count
                            checkPaidSlots(currentPlanet, slotCount);
                            callSlot(slotCount, outerShopImage);
                            break; // Stop reading after obtaining the slot count
                        }

                    // Check if the line contains a slot information
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void callSlot(int slotCount, ImageView outerShopImage)
    {
        int tempSlot = 0;
        Random ran = new Random();

        boolean bool = true;
        while (bool) {

            while (true) {

                if (existingSlots.size() > 3 || existingSlots.size() == slotCount)
                {
                    bool = false;
                    break;
                }
                tempSlot = ran.nextInt(slotCount) + 1;

                if (existingSlots.contains(tempSlot)) {
                    break;
                } else {
                    System.out.println("slotount; " + tempSlot);

                    existingSlots.add(tempSlot);
                }


                System.out.println(tempSlot);
                String img = "/graphics/png/shops/slots/" + currentPlanet + "/upgrade" + tempSlot + ".gif";


                ArrayList temp = new ArrayList();
                temp = unpaidSlots.get(currentPlanet);
                if (!slotAlreadyCreated(layoutX) && !temp.contains(tempSlot)) {
                    openClassShop(pointCounter.count, layoutX, img, tempSlot, outerShopImage);
                    layoutX += 95;
                }

            }
        }
    }

    public HashMap<String, ArrayList> unpaidSlots = new HashMap<String, ArrayList>();
    public ArrayList<String> paidCheckDONEforPlanet = new ArrayList<String>();

    private void checkPaidSlots(String curPla, int allSLOTS)
    {
        if (!paidCheckDONEforPlanet.contains(curPla)) {
            unpaidFOR(allSLOTS, curPla);
        }
    }

    private void unpaidFOR(int allSLOTS, String curPla)
    {
        int anzahlDERbezahlenden = random.nextInt(5) + 1;
        ArrayList<Integer> temp = new ArrayList<Integer>();

        for (int i = 0; i < anzahlDERbezahlenden; i++) {
            int everythingNeededTOpay = random.nextInt(allSLOTS-1) + 1;

            temp.add(everythingNeededTOpay);
        }

        unpaidSlots.put(curPla, temp);

    }
    private boolean slotAlreadyCreated(int layoutX) {
        for (Node node : root.getChildren()) {
            if (node instanceof ImageView && node.getLayoutX() == layoutX && node.getLayoutY() == 50) {
                return true;
            }
        }
        return false;
    }


    // Setup for movement, DO NOT CHANGE!
    private void movementSetup() {

        primaryScene.setOnKeyPressed(a -> {
            KeyCode code = a.getCode();
            if (a.getCode().toString().equals("SPACE")&&!dead) {
                playerLaser=true;
                spawner.spawnLaser();
                a.consume();
                playerLaser=false;
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


    public String identifyShopItem(int slotNumber) {
        String temp = " ";

        switch (currentPlanet) {
            case "earth":
            switch (slotNumber) {
                case 1:
                    temp = "hull_upgrade";
                    hitpoints=hitpoints+1;
                    break;

                case 2:
                    temp = "thruster_upgrade";
                    tempMovementW=true;
                    tempMovementA=true;
                    tempMovementS=true;
                    tempMovementD=true;
                    break;

                case 3:
                    temp = "laser_upgrade";
            }
            break;

            case "birmingham":
            break;
        }
        return temp;
    }


    ArrayList<ImageView> test = new ArrayList<ImageView>();
    public void addView(ImageView img)
    {
        test.add(img);
        root.getChildren().add(img);
        System.out.println("img"+img);
    }

    public void delView()
    {
        for (ImageView img : test) {
            root.getChildren().remove(img);
        }
    }
    int count = 0;
    private void openClassShop(int currentPoints, int layoutX, String img, int slotNumber, ImageView outerShopView) {
        Shop shop = new Shop(currentPoints, layoutX, img);

        // Create and add slot view to the shop
        ImageView slotView = new ImageView(new Image(getClass().getResourceAsStream(img)));
        slotView.setLayoutX(-19);
        slotView.setLayoutY(layoutX); // Adjust Y position as needed


        slotView.setFitWidth(1562);
        slotView.setFitHeight(800);

        shop.addSlotView(slotView);

        // Add event handling to the slot ImageView
        slotView.setOnMouseClicked(e -> {

            System.out.println("slotnumber"+identifyShopItem(slotNumber));

            //calls clicked shop to delete it
            root.getChildren().removeAll(shop.getSlotViews());

        });

        count += 1;

        //because of addAll the arraylist always has size 1

        for (ImageView sl : shop.getSlotViews()) {
            addView(sl);
        }
        System.out.println("size"+shop.getSlotViews().size());
    }

    public int getPoints()
    {
        return pointCounter.count;
    }

    private void killplayer() {
        System.out.println("player is kil");
        dead=true;
        endGame();
        kalp=true;
        //call method to put player in main menu
    }

    // Deletes player and sends them to the main menu
    public void endGame() {
        root.getChildren().remove(mainModule.playerView);
        // Add code to transition to the main menu
    }
}

