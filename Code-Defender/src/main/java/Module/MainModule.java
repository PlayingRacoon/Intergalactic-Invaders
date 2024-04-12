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

    public MainModule() {
        playerView = new ImageView(new Image(getClass().getResourceAsStream("/graphics/png/SpaceShip.gif")));
        loadEnemyAttributes();
        if (enemyImage != null) {
            enemyView = new ImageView(new Image(getClass().getResourceAsStream(enemyImage)));
        } else {
            System.err.println("Error: enemyImage is null. Check if loadEnemyAttributes() is properly executed.");
        }
    }


    private String enemyImage;
    public String enemyType;
    public double enemySpeed;
    public int enemyDamage;
    public int enemyHitpoints;

    private static final String ENEMY_FILE_PATH = "enemies.txt";

    private void loadEnemyAttributes() {
        Map<String, String> enemyData = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/JSON/" + ENEMY_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    enemyData.put(parts[0].trim(), parts[1].trim());
                } else {
                    System.err.println("Error: Invalid format in " + ENEMY_FILE_PATH);
                }
            }
            String enemyImageName = enemyData.getOrDefault("/image", "");
            enemyImage = "/graphics/png/" + enemyImageName;
            enemyType = enemyData.getOrDefault("/type", "");
            enemySpeed = Double.parseDouble(enemyData.getOrDefault("/speed", "0.0"));
            enemyDamage = Integer.parseInt(enemyData.getOrDefault("/damage", "0"));
            enemyHitpoints = Integer.parseInt(enemyData.getOrDefault("/hitpoints","0"));
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
