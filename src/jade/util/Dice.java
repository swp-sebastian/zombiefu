package jade.util;

import java.util.List;
import java.util.Random;

/**
 * A pseudo-random number generator.
 */
public class Dice
{
    /**
     * The global instance of {@code Dice}. This instance is to be used when a unique instance is
     * not needed, and as a default value for any call requiring {@code Dice}. It is recommended,
     * but not enforced, that reseed NOT be called on this instance, as this might reduce randomness
     * in other parts of a Jade system.
     */
    public static final Dice global = new Dice();

    private Random random;

    /**
     * Constructs a new {@code Dice} and initializes it with the given seed.
     * @param seed the seed of the pseudo-random number generator
     */
    public Dice(int seed)
    {
        random = new Random(seed);
    }

    /**
     * Constructs a new {@code Dice} and initializes it with a seed based on the current clock.
     */
    public Dice()
    {
        this((int)System.currentTimeMillis());
    }

    /**
     * Reseeds the pseudo-random number generator with the given seed. The sequence of numbers
     * generated after being given any particular seed is unique. In other words, after each call to
     * {@code reseed} with the same seed, the results of the generator will be identical.
     * @param seed the new seed of the pseudo-random number generator
     */
    public void reseed(int seed)
    {
        random.setSeed(seed);
    }

    /**
     * Reseeds the pseudo-random number generator with a seed chosen based on the current clock
     * time.
     */
    public final void reseed()
    {
        reseed((int)System.currentTimeMillis());
    }

    /**
     * Returns a random non-negative integer.
     * @return a random non-negative integer
     */
    public int nextInt()
    {
        return Math.abs(random.nextInt());
    }

    /**
     * Returns a random non-negative integer strictly less than the max. Note that max must be a
     * non-negative integer.
     * @param max the upper bound (exclusive) of the generated integer
     * @return a random non-negative integer
     */
    public final int nextInt(int max)
    {
        Guard.argumentIsPositive(max);

        return nextInt() % max;
    }

    /**
     * Returns a random non-negative integer between min and max (inclusive). Note that min must be
     * less than max.
     * @param min the lower bound (inclusive) of the generated integer
     * @param max the upper bound (inclusive) of the generated integer
     * @return a random non-negative integer
     */
    public final int nextInt(int min, int max)
    {
        return nextInt(max - min + 1) + min;
    }

    /**
     * Returns true with a probability of odds / total. Odds must be non-negative and less than or
     * equal to total.
     * @param odds the probability of true
     * @param total the total amount of probability
     * @return true with probability odds / total
     */
    public final boolean chance(int odds, int total)
    {
        Guard.argumentIsNonNegative(odds);
        Guard.validateArgument(odds <= total);

        return nextInt(total) < odds;
    }

    /**
     * Returns true with a probability of percent / 100.
     * @param percent the probability of true
     * @return true with probability percent / 100
     */
    public final boolean chance(int percent)
    {
        return chance(percent, 100);
    }

    /**
     * Returns true with a probability of 1/2.
     * @return true with probability 1/2
     */
    public final boolean chance()
    {
        return chance(50);
    }

    /**
     * Returns a random element of a list.
     * @param <T> the type of the elements of the list
     * @param possible the list from which an element is chosen
     * @return a random element of a list
     */
    public <T> T choose(List<T> possible)
    {
        Guard.argumentIsNotNull(possible);
        Guard.validateArgument(!possible.isEmpty());

        int index = nextInt(possible.size());
        return possible.get(index);
    }
}
