package Module;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private List<ImageView> shopViews = new ArrayList<>();
    private List<ImageView> slotViews = new ArrayList<>();

    private ImageView shopView;

    private int layoutX;

    public Shop(int currentPoints, int layoutX, String img) {
        this.layoutX = layoutX;

        // Load the shop image
        Image shopImage = new Image(getClass().getResourceAsStream(img));
        shopView = new ImageView(shopImage);

        // Set size of the shop image
        shopView.setFitWidth(1562); //set x
        shopView.setFitHeight(800); //set y

        // Set position of the shop image
        shopView.setLayoutX(19); // Adjust position as needed
        shopView.setLayoutY(layoutX); // Adjust position as needed

        // Create a label to display the current points
        Label pointsLabel = new Label("Current Points: " + currentPoints);
        pointsLabel.setFont(Font.font(20));

        // Create a VBox to hold the points label
        VBox vbox = new VBox(pointsLabel);

        // Set position of points label
        VBox.setMargin(pointsLabel, new javafx.geometry.Insets(10, 0, 0, 0)); // Adjust margins as needed

        // Add the shop image and points label to the root StackPane
        StackPane root = new StackPane();
        root.getChildren().addAll(shopView, vbox);

        // Add shop view to the list
        shopViews.add(shopView);

    }
    public void addSlotView(ImageView slotView) {
        shopViews.add(slotView);
        slotViews.add(slotView);
    }

    // Method to retrieve the list of slot views
    public List<ImageView> getSlotViews() {
        return slotViews;
    }


    public List<ImageView> getShopViews() {
        return shopViews;
    }
    public ImageView getShopView() {
        return shopView;
    }
}


