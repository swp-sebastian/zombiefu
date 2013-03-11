package zombiefu;

import java.awt.Color;

import jade.ui.TermPanel;
import jade.util.datatype.ColoredChar;
import zombiefu.creature.Player;
import zombiefu.items.Waffe;
import zombiefu.level.Level;
import zombiefu.util.Screen;
import zombiefu.util.ZombieTools;
import zombiefu.ui.ZombieFrame;

public class ZombieFU {

    public static void main(String[] args) throws InterruptedException {

        // Der Frame, der drei TermPanels erzeugt
        ZombieFrame frame = new ZombieFrame("The Final Exam - Die Anwesenheitspflicht schlägt zurück");
        TermPanel term = frame.mainTerm();

        // Der neue Spieler
        Player player = new Player(frame,
                ColoredChar.create('\u263B', Color.decode("0x7D26CD")),
                "John Dorian", 100, 10, 10, 10,
                new Waffe("Faust", 1, ColoredChar.create('|')));
        
        // Superhäßlich, müssen wir nochmal drüber reden (tomas)
        ZombieTools.registerPlayer(player);

        // Startscreen
        Screen.showImage(term, "src/sources/startscreen.txt");

        // Storytext
        Screen.showImage(term, "src/sources/story.txt");

        // Story initialisieren
        ZombieTools.createStoryForPlayer(player);
        term.registerCamera(player, 40, 17);
        // Spielen!
        while (!player.expired()) {
            player.refreshWorld();
            player.refreshStats();
            player.world().tick();
        }

        Screen.showImage(term, "src/sources/endscreen.txt");
        System.exit(0);
    }
}
