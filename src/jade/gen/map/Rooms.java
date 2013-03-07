package jade.gen.map;

import jade.core.World;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.MutableCoordinate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Uses a binary space partitioning algorithm to generate rooms, and the connect them using the
 * binary space partition tree. Cycles are then added to make the maps more interesting. This
 * algorithm yields traditional roguelike maps with rectangular rooms connected by corridors.
 */
public class Rooms extends MapGenerator
{
    private ColoredChar wallTile;
    private ColoredChar floorTile;
    private int minSize;

    /**
     * Instantiates a BSP with default parameters. Room minSize is 4. Wall and floor tiles are '#'
     * and '.' respectively.
     */
    public Rooms()
    {
        this(ColoredChar.create('#'), ColoredChar.create('.'), 4);
    }

    /**
     * Instantiates a custom BSP with provided parameters.
     * @param wallTile the tile used for impassible walls
     * @param floorTile the tile used for passable floors
     * @param minSize the minimum dimension of a room
     */
    public Rooms(ColoredChar wallTile, ColoredChar floorTile, int minSize)
    {
        this.floorTile = floorTile;
        this.wallTile = wallTile;
        this.minSize = minSize;
    }

    @Override
    protected void generateStep(World world, Dice dice)
    {
        Set<Coordinate> cells = init(world);
        int wandObenY = (world.height()-1)*2/5;
        int wandUntenY = (world.height()-1)*3/5;
        
        for(int x = 0; x < world.width(); x++) {
                    world.setTile(wallTile, false, x, wandObenY);
                    world.setTile(wallTile, false, x, wandUntenY);            
        }
        
        int wandObenTrenner = dice.nextInt(2, world.width()-3);
        int wandUntenTrenner = dice.nextInt(2, world.width()-3);
        
        for(int y = 0; y < wandObenY; y++) { 
            world.setTile(wallTile, false, wandObenTrenner, y);
        }
        
        for(int y = wandUntenY; y < world.height()-1; y++) { 
            world.setTile(wallTile, false, wandUntenTrenner, y);
        }
        
        world.setTile(floorTile, true, dice.nextInt(1, wandObenTrenner-1), wandObenY);
        world.setTile(floorTile, true, dice.nextInt(wandObenTrenner+1, world.width()-2), wandObenY);
        world.setTile(floorTile, true, dice.nextInt(1, wandUntenTrenner-1), wandUntenY);
        world.setTile(floorTile, true, dice.nextInt(wandUntenTrenner+1, world.width()-2), wandUntenY);
    }

    private Set<Coordinate> init(World world) {
        Set<Coordinate> cells = new HashSet<>();
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                if( x == 0 || y == 0 || x == world.width() - 1 || y == world.height() - 1)
                    world.setTile(wallTile, false, x, y);
                else 
                    world.setTile(floorTile, true, x, y);
            }
        return cells;
    }

}
