package zombiefu;

import zombiefu.creature.Dragon;
import zombiefu.creature.Player;
import zombiefu.creature.Zombie;
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
        // Der Drache kommt hinzu
        world.addActor(new Dragon());
        for(int i = 0; i <= 5; i++)
            world.addActor(new Zombie());

        while(!player.expired()) {
                term.clearBuffer();
                for(int x = 0; x < world.width(); x++) {
                    for(int y = 0; y < world.height(); y++) {
                        term.bufferChar(x, y, world.look(x, y));
                    }
                    term.bufferCameras();
                    term.refreshScreen();
                }
                world.tick();
        }
        
        Screen.showImage(term, world, "src/sources/endscreen.txt");
        term.getKey();
        System.exit(0);
    }
}
