package Module;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Paths;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class MainModule {
    public ImageView playerView;
    public ImageView enemyView;

    public MainModule() {
        playerView = new ImageView(new Image(getClass().getResourceAsStream("/graphics/png/SpaceShip.gif")));
        loadEnemyAttributes();
        enemyView = new ImageView(new Image(getClass().getResourceAsStream(enemyImage)));
    }

    private String enemyImage;
    public double enemySpeed;
    public int enemyDamage;


    private void loadEnemyAttributes() {
        JSONParser parser = new JSONParser();
        try (InputStream inputStream = getClass().getResourceAsStream("/JSON/enemyTypes.json")) {
            if (inputStream != null) {
                StringBuilder jsonString = new StringBuilder();
                int character;
                while ((character = inputStream.read()) != -1) {
                    jsonString.append((char) character);
                }
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString.toString());
                enemyImage = (String) jsonObject.get("image");
                enemySpeed = Double.parseDouble(jsonObject.get("speed").toString());
                enemyDamage = Integer.parseInt(jsonObject.get("damage").toString());
            } else {
                System.err.println("Error: Unable to load enemyTypes.json file. Resource not found.");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}
