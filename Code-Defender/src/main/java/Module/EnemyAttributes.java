package Module;

public class EnemyAttributes {
    private double speed;
    private int damage;
    private int hitpoints;
    private String type;

    public EnemyAttributes(double speed, int damage, int hitpoints, String type) {
        this.speed = speed;
        this.damage = damage;
        this.hitpoints = hitpoints;
        this.type = type;
    }

    // Getter methods
    public double getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public String getType() {
        return type;
    }
}

