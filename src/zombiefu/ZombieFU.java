package zombiefu;

import zombiefu.creature.Player;
import zombiefu.level.Level;
import zombiefu.util.Screen;

public class ZombieFU {

    public static void main(String[] args) throws InterruptedException {

        ZombiePanel term = ZombiePanel.getFramedTerminal("The Final Exam - Die Anwesenheitspflicht schl�gt zur�ck");
        // Der neue Spieler
        Player player = new Player(term);
        // Die neue Welt, in der sich nun der neue Spieler befindet
        Screen.showImage(term,"src/sources/startscreen.txt");
        Level world = new Level(68, 21,player);
        while(!player.expired())
        	world.refresh(term);
        Screen.showImage(term, "src/sources/endscreen.txt");
        System.exit(0);
    }
}
