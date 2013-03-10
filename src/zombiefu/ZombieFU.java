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
        Screen.showImage(term,"src/sources/startscreen.txt");
        Level world = new Level(68, 21, "src/sources/Testraum.txt");
        world.addActor(player);
        while(!player.expired())
        	((Level) player.world()).refresh(term);
        Screen.showImage(term, "src/sources/endscreen.txt");
        System.exit(0);
    }
}
