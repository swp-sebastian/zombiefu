package zombiefu;

import jade.ui.TermPanel;
import zombiefu.creature.Player;
import zombiefu.level.Level;
import zombiefu.util.Screen;
import zombiefu.util.ZombieTools;
import zombiefu.ui.ZombieFrame;

public class ZombieFU {

    public static void main(String[] args) throws InterruptedException {

        // Der Frame, der drei TermPanels erzeugt, frame.{top, main, bottom}Term();
        ZombieFrame frame = new ZombieFrame("The Final Exam - Die Anwesenheitspflicht schlägt zurück");
        TermPanel term = frame.mainTerm();

        // Der neue Spieler
        Player player = new Player(term);

        // Startscreen
        Screen.showImage(term, "src/sources/startscreen.txt");

        // Story initialisieren
        ZombieTools.createStoryForPlayer(player);

        // Spielen!
        while (!player.expired())
            ((Level) player.world()).refresh(term);
        Screen.showImage(term, "src/sources/endscreen.txt");
        System.exit(0);
    }
}
