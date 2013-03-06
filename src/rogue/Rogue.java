package rogue;

import jade.core.World;
import jade.ui.TiledTermPanel;
import jade.util.datatype.ColoredChar;
import java.awt.Color;
import java.util.Collection;

import rogue.creature.Monster;
import rogue.creature.Player;
import rogue.level.Level;
import rogue.screen.Screen;

public class Rogue
{
    public static void main(String[] args) throws InterruptedException
    {
        TiledTermPanel term = TiledTermPanel.getFramedTerminal("The Final Exam - Die Anwesenheitspflicht schlägt zurück");
        /*
        term.registerTile("dungeon.png", 5, 59, ColoredChar.create('#'));
        term.registerTile("dungeon.png", 3, 60, ColoredChar.create('.'));
        term.registerTile("dungeon.png", 5, 20, ColoredChar.create('@'));
        term.registerTile("dungeon.png", 14, 30, ColoredChar.create('D', Color.red));		WOZU??*/
        
        Player player = new Player(term);									// 	Der neue Spieler
        World world = new Level(70, 30, player);			// Die neue Welt, in der sich nun der neue Spieler befindet
        world.addActor(new Monster(ColoredChar.create('D', Color.yellow)));	// 	Der Drache kommt hinzu
        //term.registerCamera(player, 5, 5); 									Zeigt den Ausschnitt des Spielers an
        
        Screen.showImage(term,world,"src/rogue/screen/startscreen.txt");
        
        
        while(!player.expired())
        {	
        	// prüft ob der Drache mich berührt.
        	Collection<Monster> monsters = world.getActorsAt(Monster.class, player.pos());
        	if (!monsters.isEmpty()){
        		Screen.showImage(term, world, "src/rogue/screen/endscreen.txt");
        		player.expire();
        	}
        	// nun wird der Screen neu gebildet
            term.clearBuffer();
            for(int x = 0; x < world.width(); x++)
                for(int y = 0; y < world.height(); y++)
                    term.bufferChar(x/* + 11*/, y, world.look(x, y));
            term.bufferCameras();
            term.refreshScreen();
            
            world.tick();
        }

        System.exit(0);
    }
}
