package zombiefu.level;

import jade.core.World;
import jade.gen.Generator;
import zombiefu.creature.Player;
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

    private static Generator getLevelGenerator()
    {
    	RoomBuilder.setCharSet("src/sources/CharSet.txt");
    	return new RoomBuilder("src/sources/Testraum.txt");
        //return new Rooms();
    }
}
