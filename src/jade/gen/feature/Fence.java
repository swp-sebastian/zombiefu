package jade.gen.feature;

import jade.core.World;
import jade.gen.Generator;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;

/**
 * Places a fence around a map. This is almost always a good idea as it will prevent line of sight
 * calculations and {@code Actor} placement from happening outside the bounds of the {@code World}.
 */
public class Fence extends FeatureGenerator
{
    private static final ColoredChar face = ColoredChar.create('#');

    /**
     * Generates a new instance of Fence, the the specified chained {@code Generator}.
     * @param chained the chained {@code Generator}
     */
    public Fence(Generator chained)
    {
        super(chained);
    }

    @Override
    protected void generateStep(World world, Dice dice)
    {
        for(int x = 0; x < world.width(); x++)
        {
            world.setTile(face, false, x, 0);
            world.setTile(face, false, x, world.height() - 1);
        }
        for(int y = 1; y < world.height() - 1; y++)
        {
            world.setTile(face, false, 0, y);
            world.setTile(face, false, world.width() - 1, y);
        }
    }
}
