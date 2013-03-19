package jade.story.prob;

import jade.util.Dice;
import jade.util.Guard;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a categorical distribution over {@code <T>}. This discrete distribution assigns a mass
 * to each possible outcome. This class represents these probabilities a set of frequencies. The
 * probability of any given event is equal to its count divided by the sum of all counts.
 * @param <T> the type of the support of the distribution
 */
public class Categorical<T>
{
    private Map<T, Integer> frequencies;
    private int sum;

    /**
     * Constructs a new categorical distribution with no observed counts.
     */
    public Categorical()
    {
        frequencies = new HashMap<T, Integer>();
        sum = 0;
    }

    /**
     * Returns the current count of the provided key.
     * @param key the key being queried
     * @return the current count of the key
     */
    public int getCount(T key)
    {
        Guard.argumentIsNotNull(key);

        return frequencies.containsKey(key) ? frequencies.get(key) : 0;
    }

    /**
     * Sets the count of the provided key
     * @param key the key being set
     * @param count the new count of the key
     */
    public void setCount(T key, int count)
    {
        Guard.argumentIsNotNull(key);
        Guard.argumentIsNonNegative(count);

        sum += count - getCount(key);
        if(count > 0)
            frequencies.put(key, count);
        else
            frequencies.remove(key);
    }

    /**
     * Increments the count of the provided key by one.
     * @param key the key being incremented
     */
    public final void incrementCount(T key)
    {
        frequencies.put(key, getCount(key) + 1);
        sum++;
    }

    /**
     * Gets the sum of all the counters.
     * @return the sum of all the counters
     */
    public int sumCounts()
    {
        return sum;
    }

    /**
     * Returns the support of the distribution, which is the smallest set whose compliment has a
     * cumulative probability of zero. In other words, it is the set of all elements for which there
     * is a non-zero count.
     * @return the support of the categorical distribution
     */
    public Set<T> support()
    {
        return frequencies.keySet();
    }

    /**
     * Returns the probability of the specified key, which is equal to its count divided by the sum.
     * @param key the key being queried.
     * @return the probability of the key
     */
    public double probability(T key)
    {
        Guard.verifyState(sum > 0);

        return (double)getCount(key) / sum;
    }

    /**
     * Returns a random sample from the categorical distribution using the provided {@code Dice} to
     * generate the sampled event.
     * @param dice the {@code Dice} used to generate the sampled event
     * @return a random sample from the distribution
     */
    public T sample(Dice dice)
    {
        Guard.argumentIsNotNull(dice);
        Guard.verifyState(sum > 0);

        int sample = dice.nextInt(sum);

        T event = null;

        for(T key : frequencies.keySet())
        {
            if(sample < frequencies.get(key))
            {
                event = key;
                break;
            }
            sample -= frequencies.get(key);
        }

        return event;
    }

    /**
     * Returns a random sample from the categorical distribution using the default {@code
     * Dice.global} to generate the sampled event.
     * @return a random sample from the categorical distribution
     */
    public final T sample()
    {
        return sample(Dice.global);
    }
}
