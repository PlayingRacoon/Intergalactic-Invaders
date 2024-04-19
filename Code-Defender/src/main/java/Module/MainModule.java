package Module;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MainModule {
    public ImageView playerView;
    public ImageView enemyView;
    private Enemy enemy; // Assuming Enemy class exists

    public ImageView laserView;
    private Laser laser; // Assuming Enemy class exists



    public int chosenEnemy = 5; // number of the enemy that gets chosen from 1 to ....

    public int chosenLaser = 1; // number of the enemy that gets chosen from 1 to ....

    public MainModule(double stageWidth, double stageHeight) {
        playerView = new ImageView(new Image(getClass().getResourceAsStream("/graphics/png/player/SpaceShip.gif")));
        loadEnemyAttributes();
        if (enemyImage != null) {
            enemyView = new ImageView(new Image(getClass().getResourceAsStream(enemyImage)));
            initializeEnemy(stageWidth, stageHeight); // Initialize enemy only if its position is within reasonable bounds
        } else {
            System.err.println("Error: enemyImage is null. Check if loadEnemyAttributes() is properly executed.");
        }
        loadLaserAttributes();
        if (laserImage != null) {
            laserView = new ImageView(new Image(getClass().getResourceAsStream(laserImage)));
            initializeLaser(stageWidth, stageHeight); // Initialize laser only if its position is within reasonable bounds
        } else {
            System.err.println("Error: laserImage is null. Check if loadEnemyAttributes() is properly executed.");
        }
    }

    public Map<Integer, Map<String, String>> allEnemiesData = new HashMap<>();
    public String enemyImage;
    public String enemyType;
    public double enemySpeed;
    public int enemyDamage;
    public int enemyHitpoints;

    private static final String ENEMY_FILE_PATH = "enemies.txt";
    private static final String CHARACTER_SEPARATOR = "++++";

    public Map<Integer, Map<String, String>> allLasersData = new HashMap<>();
    public String laserImage;
    public String laserType;
    public double laserSpeed;
    public int laserDamage;

    private static final String LASER_FILE_PATH = "lasers.txt";

    private void loadEnemyAttributes() {
        try (InputStream inputStream = getClass().getResourceAsStream("/JSON/" + ENEMY_FILE_PATH);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            Map<String, String> currentEnemyData = new HashMap<>();
            boolean newCharacter = true;
            while ((line = br.readLine()) != null) {
                if (line.equals(CHARACTER_SEPARATOR)) {
                    // Separator detected, start a new character
                    if (!currentEnemyData.isEmpty()) {
                        int enemyNumber = Integer.parseInt(currentEnemyData.getOrDefault("/number", "-1"));
                        if (enemyNumber != -1) {
                            allEnemiesData.put(enemyNumber, new HashMap<>(currentEnemyData));
                        }
                        currentEnemyData.clear();
                    }
                    newCharacter = true;
                } else {
                    // Process attributes for the current character
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        currentEnemyData.put(parts[0].trim(), parts[1].trim());
                    } else {
                        System.err.println("Error: Invalid format in " + ENEMY_FILE_PATH);
                    }
                }
            }
            // Store the last character's data
            if (!currentEnemyData.isEmpty()) {
                int enemyNumber = Integer.parseInt(currentEnemyData.getOrDefault("/number", "-1"));
                if (enemyNumber != -1) {
                    allEnemiesData.put(enemyNumber, new HashMap<>(currentEnemyData));
                }
            }

            // Check if the chosen enemy exists in the loaded data
            if (allEnemiesData.containsKey(chosenEnemy)) {
                Map<String, String> chosenEnemyData = allEnemiesData.get(chosenEnemy);
                enemyImage = "/graphics/png/enemies/" + chosenEnemyData.getOrDefault("/image", "");
                enemyType = chosenEnemyData.getOrDefault("/type", "");
                enemySpeed = Double.parseDouble(chosenEnemyData.getOrDefault("/speed", "0.0"));
                enemyDamage = Integer.parseInt(chosenEnemyData.getOrDefault("/damage", "0"));
                enemyHitpoints = Integer.parseInt(chosenEnemyData.getOrDefault("/hitpoints", "0"));

            } else {
                System.err.println("Error: Chosen enemy number not found.");
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void initializeEnemy(double stageWidth, double stageHeight) {
        // Calculate the initial Y position of the enemy within reasonable bounds
        double enemyY = Math.max(0, Math.min(stageHeight - enemyView.getBoundsInParent().getHeight(), 240));

        // Set the initial X position of the enemy to the right edge of the screen
        double enemyX = stageWidth - enemyView.getBoundsInParent().getWidth();

        // Create and initialize the enemy
        enemy = new Enemy(enemyView.getImage(), enemySpeed, enemyDamage, enemyHitpoints, chosenEnemy);
        enemyView.setLayoutX(enemyX);
        enemyView.setLayoutY(enemyY);
    }

    private void loadLaserAttributes() {
        try (InputStream inputStream = getClass().getResourceAsStream("/JSON/" + LASER_FILE_PATH);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            Map<String, String> currentLaserData = new HashMap<>();
            boolean newCharacter = true;
            while ((line = br.readLine()) != null) {
                if (line.equals(CHARACTER_SEPARATOR)) {
                    // Separator detected, start a new character
                    if (!currentLaserData.isEmpty()) {
                        int laserNumber = Integer.parseInt(currentLaserData.getOrDefault("/number", "-1"));
                        if (laserNumber != -1) {
                            allLasersData.put(laserNumber, new HashMap<>(currentLaserData));
                        }
                        currentLaserData.clear();
                    }
                    newCharacter = true;
                } else {
                    // Process attributes for the current character
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        currentLaserData.put(parts[0].trim(), parts[1].trim());
                    } else {
                        System.err.println("Error: Invalid format in " + LASER_FILE_PATH);
                    }
                }
            }
            // Store the last character's data
            if (!currentLaserData.isEmpty()) {
                int laserNumber = Integer.parseInt(currentLaserData.getOrDefault("/number", "-1"));
                if (laserNumber != -1) {
                    allLasersData.put(laserNumber, new HashMap<>(currentLaserData));
                }
            }

            // Check if the chosen laser exists in the loaded data
            if (allLasersData.containsKey(chosenLaser)) {
                Map<String, String> chosenLaserData = allLasersData.get(chosenLaser);
                laserImage = "/graphics/png/player/" + chosenLaserData.getOrDefault("/image", "");
                laserType = chosenLaserData.getOrDefault("/type", "");
                laserSpeed = Double.parseDouble(chosenLaserData.getOrDefault("/speed", "0.0"));
                laserDamage = Integer.parseInt(chosenLaserData.getOrDefault("/damage", "0"));
            } else {
                System.err.println("Error: Chosen laser number not found.");
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void initializeLaser(double stageWidth, double stageHeight) {
        // Calculate the initial Y position of the laser within reasonable bounds
        double laserY = Math.max(0, Math.min(stageHeight - laserView.getBoundsInParent().getHeight(), 240));

        // Set the initial X position of the laser to the right edge of the screen
        double laserX = stageWidth - laserView.getBoundsInParent().getWidth();

        // Create and initialize the laser
        laser = new Laser(laserView.getImage(), laserSpeed, laserDamage, chosenLaser);
        laserView.setLayoutX(laserX);
        laserView.setLayoutY(laserY);
    }
}
