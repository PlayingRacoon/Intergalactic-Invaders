package Module;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainModule {
    public ImageView playerView;
    public ImageView enemyView;

    public int chosenEnemy = 2; // Default chosen enemy number

    public MainModule() {
        playerView = new ImageView(new Image(getClass().getResourceAsStream("/graphics/png/SpaceShip.gif")));
        loadEnemyAttributes();
        if (enemyImage != null) {
            enemyView = new ImageView(new Image(getClass().getResourceAsStream(enemyImage)));
        } else {
            System.err.println("Error: enemyImage is null. Check if loadEnemyAttributes() is properly executed.");
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

    private void loadEnemyAttributes() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/JSON/" + ENEMY_FILE_PATH))) {
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
                enemyImage = "/graphics/png/" + chosenEnemyData.getOrDefault("/image", "");
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
}
