package jade.gen.feature;

import jade.core.Actor;
import jade.core.World;
import jade.gen.Generator;
import jade.util.Dice;
import jade.util.Guard;

/**
 * Randomly sprinkles {@code Actor} according to a specific {@code SprinklerPart}. This can be used
 * to populate entire maps with monsters, traps, or any other type of {@code Actor}.
 */
public class Sprinkler extends FeatureGenerator
{
    private SprinklerPart part;

    /**
     * Creates a new {@code Sprinkler} with the required chained {@code Generator} and the specified
     * {@code SprinklerPart}.
     * @param chained the chained {@code Generator}
     * @param part the {@code SprinklerPart} to be used in generation
     */
    public Sprinkler(Generator chained, SprinklerPart part)
    {
        super(chained);
        Guard.argumentIsNotNull(part);

        this.part = part;
    }

    @Override
    protected void generateStep(World world, Dice dice)
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                if(world.passableAt(x, y) && part.decide(dice, x, y))
                    world.addActor(part.getActor(dice, x, y), x, y);
    }

    /**
     * Used by {@code Sprinkler} to determine where {@code Actor}s should be placed and to generate
     * those {@code Actor}s.
     */
    public interface SprinklerPart
    {
        /**
         * Returns true if an {@code Actor} should be placed at the specified location
         * @param dice the {@code Dice} to be used in making the decision
         * @param x the x value of the location being queried
         * @param y the y value of the location being queried
         * @return true if an {@code Actor} should be placed at the specified location
         */
        public boolean decide(Dice dice, int x, int y);

        /**
         * Returns the {@code Actor} that should be placed at the specified location, provide that a
         * call to decided with the same parameters returned true.
         * @param dice the {@code Dice} to be used in generating the {@code Actor}
         * @param x the x value of the location being queried
         * @param y the y value of the location being queried
         * @return the {@code Actor} that should be placed at the specified location
         */
        public Actor getActor(Dice dice, int x, int y);
    }
}
