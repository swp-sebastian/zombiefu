package rogue.level;

import jade.core.World;
import jade.gen.Generator;
import jade.gen.map.Cellular;
import jade.gen.map.Maze;
import jade.gen.map.Rooms;
import jade.gen.map.Terrain;
import jade.gen.map.Traditional;
import rogue.creature.Player;

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
        return new Rooms();
    }
}
