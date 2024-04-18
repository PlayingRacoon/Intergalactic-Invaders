package Module;

import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Spawner {
    private List<ImageView> enemyViews = new ArrayList<>();
    private List<ImageView> lasers = new ArrayList<>(); // Add a list to store lasers
    private Image enemyImage;
    private Image laserImage;
    private Pane root;

    public Spawner(Image enemyImage, Image laserImage, Pane root) {
        this.enemyImage = enemyImage;
        this.laserImage = laserImage;
        this.root = root;
    }

    public void spawnEnemy(double x, double y) {
        ImageView imageView = new ImageView(enemyImage);
        imageView.setX(x);
        imageView.setY(y);
        root.getChildren().add(imageView);
        enemyViews.add(imageView);
    }

    public void spawnLaser(double x, double y) {
        ImageView imageView = new ImageView(laserImage);
        imageView.setX(x);
        imageView.setY(y);
        root.getChildren().add(imageView);
        lasers.add(imageView);
    }

    public void moveEnemies(double speed) {
        for (ImageView enemy : enemyViews) {
            enemy.setX(enemy.getX() - speed);
        }
    }

    public void moveLasers() {
        List<ImageView> toRemove = new ArrayList<>();
        for (ImageView laser : lasers) {
            double newX = laser.getX() + 5; // Adjust the speed as needed
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

    public List<ImageView> getEnemyViews() {
        return enemyViews;
    }
}
