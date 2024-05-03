package Module;

public class EnemyAttributes {
    private double speed;
    private int damage;
    private int hitpoints;
    private String type;
    private int enemyNumber;
    private long timer=System.nanoTime();

    public EnemyAttributes(double speed, int damage, int hitpoints, String type, int enemyNumber) {
        this.speed = speed;
        this.damage = damage;
        this.hitpoints = hitpoints;
        this.type = type;
        this.enemyNumber = enemyNumber;
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

    public int getEnemyNumber() {
        return enemyNumber;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }
}

