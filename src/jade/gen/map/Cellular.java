package jade.gen.map;

import jade.core.World;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.util.HashSet;
import java.util.Stack;

/**
 * Uses cellular automaton to generate interesting cave-like maps. This implementation always tries
 * to make a connected map. For large maps, this is usually done by removing small unconnected
 * portions of the map. For small maps, the whole map is usually scrapped and a new map generated.
 * Since the map is small, this turns out to be fairly inexpensive. However, after 100 tries, the
 * algorithm quits, most likely leaving a completely impassible {@code World}. This all but
 * guaranteed on exceptionally small maps.
 */
public class Cellular extends MapGenerator
{
    private int wallChance = 45;
    private int minCount1 = 5;
    private int maxCount2 = 3;
    private ColoredChar floor;
    private ColoredChar wall;

    /**
     * Creates a new instance of {@code Cellular} with the default open tile of '.' and a default
     * closed tile of '#'.
     */
    public Cellular()
    {
        this(ColoredChar.create('.'), ColoredChar.create('#'));
    }

    /**
     * Creates a new instance of {@code Cellular} with the specified open and closed tiles.
     * @param floor the face of the open tiles
     * @param wall the face of the closed tiles
     */
    public Cellular(ColoredChar floor, ColoredChar wall)
    {
        this(floor, wall, 45, 5, 3);
    }

    /**
     * Creates a new instance of Cellular with the specified generation parameters. Great care
     * should be taken when customizing these parameters, as certain configurations may make it
     * impossible to generate good levels.
     * @param floor the face of the open tiles
     * @param wall the face of the closed tiles
     * @param wallChance the chance out of 100 of seeding the map with a wall, as well as the
     *        required percent of wall tiles for the map to be accepted
     * @param minCount the minimum number of walls at distance 1 to generate a wall
     * @param maxCount the maximum number of walls at distance 2 to generate a wall
     */
    public Cellular(ColoredChar floor, ColoredChar wall, int wallChance, int minCount, int maxCount)
    {
        this.wallChance = wallChance;
        this.minCount1 = minCount;
        this.maxCount2 = maxCount;
        this.floor = floor;
        this.wall = wall;
    }

    @Override
    protected void generateStep(World world, Dice dice)
    {
        int tries = 0;
        do
        {
            Automata automata = new Automata(world, dice);
            for(int i = 0; i < 4; i++)
                automata.applyRule();
            automata.growCaves();
        }
        while(!connected(world, dice) && tries++ < 100);
    }

    private boolean connected(World world, Dice dice)
    {
        HashSet<Coordinate> fill = getFill(world, dice);
        if(fill.size() * 100 / (world.width() * world.height()) < wallChance)
            return false;
        else
        {
            deleteExtra(world, fill);
            return true;
        }
    }

    private HashSet<Coordinate> getFill(World world, Dice dice)
    {
        HashSet<Coordinate> fill = new HashSet<Coordinate>();
        Stack<Coordinate> stack = new Stack<Coordinate>();
        stack.push(world.getOpenTile(dice));

        while(!stack.isEmpty())
        {
            Coordinate coord = stack.pop();
            if(fill.contains(coord) || !world.passableAt(coord))
                continue;

            fill.add(coord);

            stack.push(coord.getTranslated(1, 0));
            stack.push(coord.getTranslated(-1, 0));
            stack.push(coord.getTranslated(0, 1));
            stack.push(coord.getTranslated(0, -1));
        }
        return fill;
    }

    private void deleteExtra(World world, HashSet<Coordinate> fill)
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                if(!fill.contains(new Coordinate(x, y)))
                    world.setTile(wall, false, x, y);
    }

    private class Automata
    {
        private World world;

        private boolean[][] cells;
        private boolean[][] temp;

        private int width()
        {
            return world.width();
        }

        private int height()
        {
            return world.height();
        }

        public Automata(World world, Dice dice)
        {
            this.world = world;
            cells = initBuffer(dice);
            temp = createBuffer();
        }

        public void applyRule()
        {
            for(int x = 1; x < width() - 1; x++)
                for(int y = 1; y < height() - 1; y++)
                    temp[x][y] = wallCount(x, y, 1) < minCount1 && wallCount(x, y, 2) > maxCount2;

            boolean[][] placeHolder = cells;
            cells = temp;
            temp = placeHolder;
        }

        public void growCaves()
        {
            for(int x = 0; x < width(); x++)
                for(int y = 0; y < height(); y++)
                {
                    if(cells[x][y])
                        world.setTile(floor, true, x, y);
                    else
                        world.setTile(wall, false, x, y);
                }
        }

        private int wallCount(int posX, int posY, int range)
        {
            int count = 0;
            for(int x = posX - range; x <= posX + range; x++)
                for(int y = posY - range; y <= posY + range; y++)
                    if(!outsideBuffer(x, y) && !cells[x][y])
                        count++;
            return count;
        }

        private boolean outsideBuffer(int x, int y)
        {
            return x < 0 || y < 0 || x >= width() || y >= height();
        }

        private boolean[][] initBuffer(Dice dice)
        {
            boolean[][] buffer = createBuffer();

            for(int x = 1; x < width() - 1; x++)
                for(int y = 1; y < height() - 1; y++)
                    buffer[x][y] = !dice.chance(wallChance);

            return buffer;
        }

        private boolean[][] createBuffer()
        {
            boolean[][] buffer = new boolean[width()][height()];

            for(int x = 0; x < width(); x++)
            {
                buffer[x][0] = false;
                buffer[x][height() - 1] = false;
            }
            for(int y = 0; y < height(); y++)
            {
                buffer[0][y] = false;
                buffer[width() - 1][y] = false;
            }

            return buffer;
        }
    }
}
