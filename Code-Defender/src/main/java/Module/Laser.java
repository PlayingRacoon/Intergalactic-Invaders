package Module;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Laser {
    private ImageView imageView;
    private double speed;
    private int damage;
    private int laserNumber;
    private String type;

    public Laser(Image image, double speed, int damage, int chosenLaser) {
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

    public int getLaserNumber() {
        return laserNumber;
    }

    public String getType() {
        return type;
    }
}
