package zombiefu;

import zombiefu.creature.Player;
import zombiefu.level.Level;
import zombiefu.util.Screen;

public class ZombieFU {

    public static void main(String[] args) throws InterruptedException {

        ZombiePanel term = ZombiePanel.getFramedTerminal("The Final Exam - Die Anwesenheitspflicht schlägt zurück");
        // Der neue Spieler
        Player player = new Player(term);
        // Die neue Welt, in der sich nun der neue Spieler befindet
        Level world = new Level(68, 21,player);
        Screen.showImage(term,world,"src/sources/startscreen.txt");
        world.create();
        while(!player.expired())
        	world.refresh(term);
        Screen.showImage(term, world, "src/sources/endscreen.txt");
        term.getKey();
        System.exit(0);
    }
}
