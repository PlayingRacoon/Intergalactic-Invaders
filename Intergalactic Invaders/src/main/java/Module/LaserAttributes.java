package Module;

public class LaserAttributes {
    private double speed;
    private int damage;
    private String type;

    public LaserAttributes(double speed, int damage, String type) {
        this.speed = speed;
        this.damage = damage;
        this.type = type;
    }

    // Getter methods
    public double getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public String getType() {
        return type;
    }
}

