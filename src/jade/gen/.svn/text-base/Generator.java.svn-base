package jade.gen;

import jade.core.World;
import jade.util.Dice;
import jade.util.Guard;

/**
 * Represents a map generation algorithm. These generators can be chained together in decorator
 * fashion to create a new composite {@code Generator}.
 */
public abstract class Generator
{
    private Generator chained;

    /**
     * Creates a new {@code Generator} with no previous chained {@code Generator}
     */
    public Generator()
    {
        this(null);
    }

    /**
     * Creates a new {@code Generator} with the given {@code Generator} as the previous chained
     * {@code Generator}.
     * @param chained the chained {@code Generator}
     */
    public Generator(Generator chained)
    {
        this.chained = chained;
    }

    /**
     * Performs the generation step of the {@code Generator}.
     * @param world the {@code World} on which the generation algorithm is being performed on
     * @param dice the {@code Dice} to be used for random number generation during map generation
     */
    protected abstract void generateStep(World world, Dice dice);

    /**
     * Calls the generation step of the {@code Generator}, after the chained {@code Generator} has
     * been called.
     * @param world the {@code World} on which the generation algorithm is being performed on
     * @param dice the {@code Dice} to be used for random number generation during map generation
     */
    public final void generate(World world, Dice dice)
    {
        Guard.argumentsAreNotNull(world, dice);

        if(chained != null)
            chained.generate(world, dice);
        generateStep(world, dice);
    }

    /**
     * Calls the generation step of the {@code Generator}, after the chained {@code Generator} has
     * been called. The global instance of {@code Dice} is used as the default random number
     * generator.
     * @param world the {@code World} on which the generation algorithm is being performed on
     */
    public final void generate(World world)
    {
        generate(world, Dice.global);
    }
}
