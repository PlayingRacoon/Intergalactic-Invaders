package Module;

import Controller.PlayerController;

import java.util.Random;

public class SpawnWave {
    public int chosenEnemy;
    Random ran = new Random();

    public int bossChosen;
    public int wave = 1;

    public void spawnChoosenEnemy(int bossEnemy, Spawner spawner, PlayerController playerController) {


        if (wave == 1)
        {
            System.out.println("boss "+ bossEnemy);

            chosenEnemy = ran.nextInt(5) + 1;
            System.out.println("chosenEnemy "+ chosenEnemy);

            if(bossEnemy == 11){
                chosenEnemy = bossEnemy;
                bossChosen = 0;
                playerController.kal = false;
                spawner.keepSpawning = false;
            }
        } else if (wave == 2) {
            chosenEnemy = ran.nextInt(10) + 5;
        }
    }
}
