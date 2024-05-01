package Controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarMap {
    private Pane root;
    private double screenWidth;
    private double screenHeight;
    private List<ImageView> planetViews = new ArrayList<>();
    private ImageView backgroundView;

    public StarMap(Pane root, double screenWidth, double screenHeight) {
        this.root = root;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        initializeBackground();
        initializePlanets();
    }

    private void initializeBackground() {
        Image backgroundImage = new Image(getClass().getResourceAsStream("/graphics/png/shops/shop.png"));
        backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(screenWidth);
        backgroundView.setFitHeight(screenHeight);
        root.getChildren().add(backgroundView);
    }

    private void initializePlanets() {
        int planetCount = 10;
        double middleX = screenWidth / 2;
        double middleY = screenHeight / 2;

        double middleWidth = 960; // Width of the middle area
        double middleHeight = 540; // Height of the middle area

        Random random = new Random();
        for (int i = 1; i <= planetCount; i++) {
            String planetImagePath = "/graphics/png/planets/planet" + i + ".png";
            Image planetImage = new Image(getClass().getResourceAsStream(planetImagePath));
            ImageView planetView = new ImageView(planetImage);

            // Randomly position the planet within the middle area of the screen
            double planetSize = random.nextDouble() * 50 + 50; // Random size between 30 and 80
            double minX = middleX - middleWidth / 2 + planetSize / 2;
            double maxX = middleX + middleWidth / 2 - planetSize / 2;
            double minY = middleY - middleHeight / 2 + planetSize / 2;
            double maxY = middleY + middleHeight / 2 - planetSize / 2;
            double planetX = random.nextDouble() * (maxX - minX) + minX;
            double planetY = random.nextDouble() * (maxY - minY) + minY;

            planetView.setFitWidth(planetSize);
            planetView.setFitHeight(planetSize);
            planetView.setLayoutX(planetX);
            planetView.setLayoutY(planetY);

            planetViews.add(planetView);
            root.getChildren().add(planetView);
        }
    }

    public void remove() {
        root.getChildren().removeAll(backgroundView);
        root.getChildren().removeAll(planetViews);
    }
}
