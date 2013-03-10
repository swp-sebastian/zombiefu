package zombiefu;

import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.awt.Color;
import zombiefu.creature.Player;
import zombiefu.items.Teleporter;
import zombiefu.level.Level;
import zombiefu.util.Screen;
import zombiefu.ui.ZombiePanel;

public class ZombieFU {

    public static void main(String[] args) throws InterruptedException {

        ZombiePanel term = ZombiePanel.getFramedTerminal("The Final Exam - Die Anwesenheitspflicht schlägt zurück");
        
        // Der neue Spieler
        Player player = new Player(term);
        
        // Startscreen
        Screen.showImage(term, "src/sources/startscreen.txt");
        
        // Welten und Teleporter
        Level world1 = Level.levelFromFile("src/sources/Testraum.txt");
        Level world2 = Level.levelFromFile("src/sources/TestraumZ.txt");
        world1.addActor(new Teleporter(world2, new Coordinate(33, 1)), new Coordinate(33, 19));
        world2.addActor(new Teleporter(world1, new Coordinate(33, 19)), new Coordinate(33, 1));
        
        // Spieler auf erste Welt setzen
        world1.addActor(player);
        
        // Spielbetrieb
        while (!player.expired()) {
            ((Level) player.world()).refresh(term);
        }
        Screen.showImage(term, "src/sources/endscreen.txt");
        System.exit(0);
    }
}
