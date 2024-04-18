package Module;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Laser {
    private List<ImageView> lasers = new ArrayList<>();
    private Image image;
    private Pane root;
    private double speed;

    public Laser(Image image, Pane root, double speed) {
        this.image = image;
        this.root = root;
        this.speed = speed;
    }

    public void shoot(double startX, double startY) {
        ImageView imageView = new ImageView(image);
        imageView.setX(startX);
        imageView.setY(startY - image.getHeight() / 2);
        root.getChildren().add(imageView);
        lasers.add(imageView);
    }

    public void moveLasers() {
        List<ImageView> toRemove = new ArrayList<>();
        for (ImageView laser : lasers) {
            double newX = laser.getX() + speed;
            laser.setX(newX);
            // Remove lasers that are out of bounds
            if (newX > root.getWidth()) {
                toRemove.add(laser);
            }
        }
        // Remove lasers that are out of bounds from the root and the list
        root.getChildren().removeAll(toRemove);
        lasers.removeAll(toRemove);
    }
}
