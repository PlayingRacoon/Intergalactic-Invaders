package Module;

import Controller.PlayerController;

import java.util.Random;

public class SpawnWave {
    public int chosenEnemy;
    Random ran = new Random();

    public int bossChosen;

    public int wave = 1;

    public void spawnChoosenEnemy() {

        if (wave == 1)
        {
            if (bossChosen != 0){
            chosenEnemy = bossChosen;
            bossChosen = 0;
            System.out.println("sjbfbdsgdsgbfgfgfdjgkjfd "+chosenEnemy);
        }

            chosenEnemy = ran.nextInt(5) + 1;
            System.out.println("chosenEnemy "+ chosenEnemy);

        } else if (wave == 2) {
            chosenEnemy = ran.nextInt(10) + 5;
        }
    }
}
