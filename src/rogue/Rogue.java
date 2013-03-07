package rogue;

import jade.core.World;
import jade.ui.TiledTermPanel;
import jade.util.datatype.ColoredChar;
import java.awt.Color;
import java.util.Collection;
import rogue.creature.Dragon;

import rogue.creature.Monster;
import rogue.creature.Player;
import rogue.level.Level;
import rogue.screen.Screen;

public class Rogue {

    public static void main(String[] args) throws InterruptedException {

        TiledTermPanel term = TiledTermPanel.getFramedTerminal("The Final Exam - Die Anwesenheitspflicht schlägt zurück");

        // Der neue Spieler
        Player player = new Player(term);
        // Die neue Welt, in der sich nun der neue Spieler befindet
        World world = new Level(70, 30, player);
        // Der Drache kommt hinzu
        world.addActor(new Dragon());

        Screen.showImage(term,world,"src/rogue/screen/startscreen.txt");

        while(!player.expired()) {       
                // prüft ob der Drache mich berührt.
                Collection<Monster> monsters = world.getActorsAt(Monster.class, player.pos());
                if (!monsters.isEmpty()) {
                        player.expire();
                        continue;
                }
                term.rebuildFromWorld(world);
                world.tick();
        }
        
        Screen.showImage(term, world, "src/rogue/screen/endscreen.txt");
        term.getKey();
        System.exit(0);
    }
}
