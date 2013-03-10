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

    public Level(int width, int height, Generator gen) {
        super(width, height);
        gen.generate(this);
        fillWithEnemies();
    }

    public Level(int width, int height, String level) {
        this(width, height, new RoomBuilder(level, "src/sources/CharSet.txt"));
    }
    
    public void fillWithEnemies() {
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
