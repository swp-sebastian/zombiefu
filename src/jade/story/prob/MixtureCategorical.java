package jade.story.prob;

import jade.util.Dice;
import jade.util.Guard;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a mixture of categorical distributions over {@code <V>}. A mixture of distributions is
 * a distribution which can be interpreted as being derived from an underlying set of distributions.
 * In this case, each of these distributions is a {@code Categorical}. Each of the underlying
 * distributions has a key so that conditional probabilities are easily calculator. The respective
 * weights of each of the underlying distributions is represented as the ratio of the counts of the
 * particular {@code Categorical} with respect to the sum of all the {@code Categorical} frequency
 * sums.
 * @param <K> the type of the keys to the individual categorical distributions in the mixture
 * @param <V> the type of the support of the categorical distributions in the mixture
 */
public class MixtureCategorical<K, V>
{
    private Map<K, Categorical<V>> frequencies;
    private Categorical<V> totals;

    /**
     * Constructs a new {@code MixtureCategorical}, with empty counts and sums.
     */
    public MixtureCategorical()
    {
        frequencies = new HashMap<K, Categorical<V>>();
        totals = new Categorical<V>();
    }

    /**
     * Sets the count for a particular value in the specified {@code Categorical} distribution.
     * @param key the key of the {@code Categorical} being modified
     * @param value the value whose count is being set
     * @param count the new count of the value
     */
    public void setCount(K key, V value, int count)
    {
        Guard.argumentsAreNotNull(key, value);
        Guard.argumentIsNonNegative(count);

        int change = count - getCountGiven(key, value);
        totals.setCount(value, totals.getCount(value) + change);
        getCategorical(key).setCount(value, count);
    }

    /**
     * Returns the count of a value given a particular {@code Categorical}.
     * @param key the key to the {@code Categorical} distribution being queried
     * @param value the value whose count is being queried
     * @return the count of value given the key
     */
    public int getCountGiven(K key, V value)
    {
        Guard.argumentsAreNotNull(key, value);

        return frequencies.containsKey(key) ? frequencies.get(key).getCount(value) : 0;
    }

    /**
     * Gets the count of a value across all {@code Categorical} in the mixture.
     * @param value the value whose count is being queried
     * @return the count of value
     */
    public int getCount(V value)
    {
        return totals.getCount(value);
    }

    /**
     * Increments the count of a value given a key.
     * @param key the key of the {@code Categorical} being modified
     * @param value the value to increment
     */
    public void incrementCount(K key, V value)
    {
        Guard.argumentsAreNotNull(key, value);

        totals.incrementCount(value);
        getCategorical(key).incrementCount(value);
    }

    /**
     * Returns the sum of all values across all {@code Categorical}.
     * @return the sum of all counts in the {@code MixtureCategorical}
     */
    public int sumCounts()
    {
        return totals.sumCounts();
    }

    /**
     * Returns the sum of all values across a particular {@code Categorical}.
     * @param key the key of the {@code Categorical} being queried
     * @return the sum of all values in the specified {@code Categorical}
     */
    public int sumCountGiven(K key)
    {
        Guard.argumentIsNotNull(key);

        return getCategorical(key).sumCounts();
    }

    /**
     * Returns the support of the distribution, which is the smallest set whose compliment has a
     * cumulative probability of zero. In other words, it is the set of all values for which there
     * is a non-zero count.
     * @return the support of the mixture of categorical distribution
     */
    public Set<V> support()
    {
        return totals.support();
    }

    /**
     * Returns the probability of the specified value, which is equal to its total count divided by
     * the total sum.
     * @param value the value being queried.
     * @return the probability of the value
     */
    public double probability(V value)
    {
        return totals.probability(value);
    }

    /**
     * Returns the probability of the specified value given a particular {@code Categorical}.
     * @param key the key of the {@code Categorical} being queried
     * @param value the value being queried
     * @return the probability of value given the key
     */
    public double probabilityGiven(K key, V value)
    {
        Guard.verifyState(totals.sumCounts() > 0);

        return sumCountGiven(key) > 0 ? getCategorical(key).probability(value) : 0;
    }

    /**
     * Returns a random sample from the whole mixture of categorical distribution using the provided
     * {@code Dice} to generate the sampled event.
     * @param dice the {@code Dice} used to generate the sampled event
     * @return a random sample from the distribution
     */
    public V sample(Dice dice)
    {
        return totals.sample(dice);
    }

    /**
     * Returns a random sample from the whole mixture of categorical distribution using the global
     * instance of {@code Dice} to generate the sampled event.
     * @return a random sample from the distribution
     */

    public final V sample()
    {
        return sample(Dice.global);
    }

    /**
     * Returns a random sample from the whole mixture of categorical distribution given a particular
     * key, using the provided {@code Dice} to generate the sampled event. This is the same as
     * sampling from the individual categorical distribution referenced by the key.
     * @param dice the {@code Dice} used to generate the sampled event
     * @return a random sample from the distribution
     */
    public V sampleGiven(K key, Dice dice)
    {
        return getCategorical(key).sample(dice);
    }

    /**
     * Returns a random sample from the whole mixture of categorical distribution given a particular
     * key, using the global instance of {@code Dice} to generate the sampled event. This is the
     * same as sampling from the individual categorical distribution referenced by the key.
     * @return a random sample from the distribution
     */
    public V sampleGiven(K key)
    {
        return sampleGiven(key, Dice.global);
    }

    private Categorical<V> getCategorical(K key)
    {
        if(!frequencies.containsKey(key))
            frequencies.put(key, new Categorical<V>());
        return frequencies.get(key);
    }
}
