package Module;

import Controller.PlayerController;

import java.util.Random;

public class SpawnWave {
    public int chosenEnemy;
    Random ran = new Random();

    public int wave = 1;

    public void spawnChoosenEnemy() {
        if (wave == 1){
            chosenEnemy = ran.nextInt(5) + 1;
            System.out.println(chosenEnemy);
        } else if (wave == 2) {
            chosenEnemy = ran.nextInt(10) + 5;
        }
    }
}
