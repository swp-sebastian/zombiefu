package zombiefu;

import jade.core.World;
import jade.ui.TiledTermPanel;
import zombiefu.creature.Dragon;
import zombiefu.creature.Player;
import zombiefu.creature.Zombie;
import zombiefu.level.Level;
import zombiefu.screen.KeyEdit;
import zombiefu.screen.Screen;

public class Rogue {

    public static void main(String[] args) throws InterruptedException {

        TiledTermPanel term = TiledTermPanel.getFramedTerminal("The Final Exam - Die Anwesenheitspflicht schlägt zurück");

        // Der neue Spieler
        Player player = new Player(term);
        // Die neue Welt, in der sich nun der neue Spieler befindet
        World world = new Level(68, 21, player);
        Screen.showImage(term,world,"src/sources/startscreen.txt");
     // An diese Stelle Abfrage für Tastenbelegung und world.tick aus screen.showImage entfernen.
        KeyEdit.getKeys(term); //Tastenbefehle einlesen
        // Der Drache kommt hinzu
        world.addActor(new Dragon());
        for(int i = 0; i <= 5; i++)
            world.addActor(new Zombie());

        

        while(!player.expired()) {
                term.rebuildFromWorld(world);
                world.tick();
        }
        
        Screen.showImage(term, world, "src/sources/endscreen.txt");
        term.getKey();
        System.exit(0);
    }
}
