package jade.gen.map;

import jade.core.World;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates terrain by randomly placing tiles according to a specific mixture of two discrete
 * distributions.
 */
public class Terrain extends MapGenerator
{
    private int probImpass;
    private List<ColoredChar> pass;
    private List<ColoredChar> impass;

    /**
     * Initializes an instance of Terrain with default values.
     */
    public Terrain()
    {
        this(10);
    }

    /**
     * Initializes an instance of Terrain with a given probability of an impassible tile.
     * @param probImpass the probability of an impassible tile
     */
    public Terrain(int probImpass)
    {
        this(probImpass, defaultPass(), defaultImpass());
    }

    /**
     * Initializes an instance of Terrain with a given probability of an impassible tile, as well as
     * custom tiles. Passable and impassible tiles will be distributed according to the parameter
     * probImpass, chosen with uniform chance from the two lists of tile faces.
     * @param probImpass the probability of an impassible tile
     * @param pass the possible faces of the passable tiles
     * @param impass the possible faces of the impassible tiles
     */
    public Terrain(int probImpass, List<ColoredChar> pass, List<ColoredChar> impass)
    {
        this.probImpass = probImpass;
        this.pass = pass;
        this.impass = impass;
    }

    @Override
    protected void generateStep(World world, Dice dice)
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                if(dice.chance(probImpass))
                {
                    ColoredChar tile = dice.choose(impass);
                    world.setTile(tile, false, x, y);
                }
                else
                {
                    ColoredChar tile = dice.choose(pass);
                    world.setTile(tile, true, x, y);
                }
            }
    }

    private static List<ColoredChar> defaultPass()
    {
        ArrayList<ColoredChar> pass = new ArrayList<ColoredChar>();
        pass.add(ColoredChar.create('.', Color.white));
        pass.add(ColoredChar.create('.', Color.green));
        pass.add(ColoredChar.create('.', Color.yellow));
        return pass;
    }

    private static List<ColoredChar> defaultImpass()
    {
        ArrayList<ColoredChar> impass = new ArrayList<ColoredChar>();
        impass.add(ColoredChar.create('%', Color.green));
        impass.add(ColoredChar.create('%', Color.yellow));
        return impass;
    }
}
