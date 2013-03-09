package zombiefu.level;

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
    }
    
    public void create(){
    	super.addActor(new Teleporter(ColoredChar.create('#')), 33,20);
    	// Der Zombie-Dozent kommt hinzu.
        addActor(new DozentZombie());
        for(int i = 0; i <= 5; i++)
            addActor(new Zombie());
    }
    
    public void changeLevel(String input, Coordinate nach){
    	((RoomBuilder) gen).changeFile(input);
    	Player player = getActor(Player.class);
    	for (Actor actor : getActors(Actor.class)){
    		if (!actor.equals(player))
    			actor.expire();
    		removeActor(actor);
    	}
    	gen.generate(this);
    	addActor(player,nach);
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
