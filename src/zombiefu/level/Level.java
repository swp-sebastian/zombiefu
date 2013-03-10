package zombiefu.level;

import java.awt.Color;

import jade.core.Actor;
import jade.core.World;
import jade.gen.Generator;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import zombiefu.ZombiePanel;
import zombiefu.creature.DozentZombie;
import zombiefu.creature.Player;
import zombiefu.creature.Zombie;
import zombiefu.items.Teleporter;
import zombiefu.map.RoomBuilder;

public class Level extends World
{
    private final static Generator gen = getLevelGenerator();

    public Level(int width, int height, Player player)
    {
        super(width, height);
        gen.generate(this);
        addActor(player);
        Teleporter tele = new Teleporter(ColoredChar.create('\u25A0',Color.decode("0x8B4513")),"Testraum","TestraumZ",
    			new Coordinate(33,19),new Coordinate(33,1));
    	// Ein Teleporter kommt hinzu
    	addActor(tele, tele.aktuell());
    	// Der Zombie-Dozent kommt hinzu.
        addActor(new DozentZombie());
        // 6 normale Zombies kommen hinzu
        for(int i = 0; i <= 5; i++)
            addActor(new Zombie());
    }
    
    public void changeLevel(String input, Teleporter tele){
    	Player player = getActor(Player.class);					// Den Spieler finden.
    	for (Actor actor : getActors(Actor.class)){				// Alle anderen Actor werden gelöscht
    		if (!actor.equals(player) && !actor.getClass().equals(Teleporter.class))
    			actor.expire();									// außer der Spieler und die Teleporter
    		removeActor(actor);
    	}
    	((RoomBuilder) gen).changeFile(input);					// Neue Datei einlesen
    	gen.generate(this);										// Neue Welt erzeugen
    	addActor(player,tele.newPos(this));						// Den Spieler neu setzen
    	addActor(tele, tele.aktuell());							// Den Teleporter setzen
        addActor(new DozentZombie());
        for(int i = 0; i <= 5; i++)
            addActor(new Zombie());
    }

    private static Generator getLevelGenerator()
    {
    	RoomBuilder.setCharSet("src/sources/CharSet.txt");
    	return new RoomBuilder("src/sources/Testraum.txt");
        //return new Rooms();
    }
    
    public void refresh(ZombiePanel term){
    	term.clearBuffer();
        for(int x = 0; x < width(); x++) {
            for(int y = 0; y < height(); y++)
                term.bufferChar(x, y, look(x, y));
            term.bufferCameras();
            term.refreshScreen();
        }
        tick();
    }
}
