package zombiefu;

import zombiefu.creature.Player;
import zombiefu.level.Level;
import zombiefu.util.Screen;
import zombiefu.ui.ZombiePanel;
import zombiefu.util.ZombieTools;

public class ZombieFU {

    public static void main(String[] args) throws InterruptedException {

        ZombiePanel term = ZombiePanel.getFramedTerminal("The Final Exam - Die Anwesenheitspflicht schlägt zurück");

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
