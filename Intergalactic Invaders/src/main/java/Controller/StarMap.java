package Controller;

import View.Screen;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Module.MainModule;
import javafx.util.Duration;

public class StarMap {
    private Pane root;
    private double screenWidth;
    private double screenHeight;
    private List<ImageView> planetViews = new ArrayList<>();
    private ImageView backgroundView;
    private HashMap<ImageView, Integer> planetNumberMap = new HashMap<>();
    private final double PLANET_SIZE_MULTIPLIER = 50; // Adjust this multiplier as needed
    Screen screen;

    public String currentPlanetAfterFTLjump = " ";


    private PlayerController playerController;

    public StarMap(Pane root, double screenWidth, double screenHeight, PlayerController playerController, Screen screen) {
        this.playerController = playerController;
        this.root = root;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        initializeBackground();
        initializePlanets();
        this.screen = screen;

    }

    private void initializeBackground() {
        Image backgroundImage = new Image(getClass().getResourceAsStream("/graphics/png/planets/backround.png"));
        backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(screenWidth);
        backgroundView.setFitHeight(screenHeight);
        root.getChildren().add(backgroundView);
    }

    private void initializePlanets() {
        // Define the positions and relative sizes for each planet
        double[][] planetData = {
                // {x, y, sizeMultiplier}
                {320, 300, 1}, // planet 1...
                {380, 470, 2}, //planet 2...
                {580, 150, 2.2}, //etc...
                {670, 450, 1.3},
                {850, 550, 1.3},
                {810, 240, 1.25},
                {1000, 180, 1.8},
                {1100, 520, 2.3},
                {220, 440, 1.1},
                {970, 370, 1.1},
        };

        for (int i = 0; i < planetData.length; i++) {
            double planetX = planetData[i][0];
            double planetY = planetData[i][1];
            double sizeMultiplier = planetData[i][2];
            createPlanet(i + 1, planetX, planetY, sizeMultiplier);
        }
    }

    private void createPlanet(int planetNumber, double planetX, double planetY, double sizeMultiplier) {
        String planetImagePath = "/graphics/png/planets/planet" + planetNumber + ".png";
        Image planetImage = new Image(getClass().getResourceAsStream(planetImagePath));
        ImageView planetView = new ImageView(planetImage);

        // Set the position of the planet
        planetView.setLayoutX(planetX);
        planetView.setLayoutY(planetY);

        // Calculate the size of the planet based on the multiplier
        double planetSize = PLANET_SIZE_MULTIPLIER * sizeMultiplier;

        // Get the original dimensions of the planet image
        double originalWidth = planetImage.getWidth();
        double originalHeight = planetImage.getHeight();

        // Calculate the ratio of the original dimensions
        double aspectRatio = originalWidth / originalHeight;

        // Calculate the new dimensions while maintaining the aspect ratio
        double newWidth = Math.sqrt(planetSize * planetSize * aspectRatio);
        double newHeight = newWidth / aspectRatio;

        // Set the dimensions of the planet view
        planetView.setFitWidth(newWidth);
        planetView.setFitHeight(newHeight);

        // Add event handler for clicking the planet
        planetView.setOnMouseClicked(event -> {

            System.out.println("Planet " + planetNumber + " clicked!");

            String playerImagePath = "/graphics/png/player/FTL.gif";
            playerController.changePlayerImage(playerImagePath);

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            String originImagePlayer = "/graphics/png/player/SpaceShip.gif";
            pause.setOnFinished(e -> {
                playerController.changePlayerImage(originImagePlayer);
                String tempBck = "/graphics/png/backrounds/" + playerController.currentPlanet + ".png";

                screen.setBackgroundImage(tempBck, playerController);
                playerController.PlayerTofront();
                playerController.pointCounter.counterLabel.toFront();
            });
            playerController.evaluateCurrentPlanet(planetNumber);

            pause.play();
            remove();
        });

        // Add the planet to the root and to the list of planet views
        planetViews.add(planetView);
        root.getChildren().add(planetView);

        // Add the planet and its number to the planet number map
        planetNumberMap.put(planetView, planetNumber);
    }

    public void remove() {
        root.getChildren().removeAll(backgroundView);
        root.getChildren().removeAll(planetViews);
    }


    public void changeBackgroundImage(String imagePath) {
        Image newBackgroundImage = new Image(getClass().getResourceAsStream(imagePath));
        backgroundView.setImage(newBackgroundImage);
    }


    public HashMap<ImageView, Integer> getPlanetNumberMap() {
        return planetNumberMap;
    }
}
