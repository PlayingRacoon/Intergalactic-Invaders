package Module;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Enemy {
    private ImageView imageView;
    private double speed;
    private int damage;
    private int enemyNumber;
    private int hitpoints;
    private String type;

    public Enemy(Image image, double speed, int damage, int enemyHitpoints, int chosenEnemy) {
        imageView = new ImageView(image);
        this.speed = speed;
        this.damage = damage;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public int getEnemyNumber() {
        return enemyNumber;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public String getType() {
        return type;
    }
}
