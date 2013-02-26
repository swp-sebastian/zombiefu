package test.story.prob;

import jade.story.prob.MixtureCategorical;
import jade.util.Dice;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import test.util.DiceTest;

public class MixtureCategoricalTest extends ProbabilityTest
{
    private MixtureCategorical<Character, Character> mixture;

    @Before
    public void init()
    {
        mixture = new MixtureCategorical<Character, Character>();
    }

    @Test
    public void countGetSetGiven()
    {
        for(char key = 'a'; key < 'z'; key++)
            for(char value = 'a'; value < 'z'; value++)
                mixture.setCount(key, value, (int)(key * value));
        for(char key = 'a'; key < 'z'; key++)
            for(char value = 'a'; value < 'z'; value++)
                Assert.assertEquals((int)(key * value), mixture.getCountGiven(key, value));
    }

    @Test
    public void countGetSet()
    {
        int totalMult = 0;
        for(char key = 'a'; key < 'z'; key++)
        {
            totalMult += key;
            for(char value = 'a'; value < 'z'; value++)
                mixture.setCount(key, value, (int)(key * value));
        }
        for(char value = 'a'; value < 'z'; value++)
            Assert.assertEquals((int)(totalMult * value), mixture.getCount(value));
    }

    @Test
    public void getCountDefault()
    {
        for(char key = 'a'; key < 'z'; key++)
            for(char value = 'a'; value < 'z'; value++)
                Assert.assertEquals(0, mixture.getCountGiven(key, value));
    }

    @Test
    public void getCountGivenDefault()
    {
        for(char value = 'a'; value < 'z'; value++)
            Assert.assertEquals(0, mixture.getCount(value));
    }

    @Test
    public void incrementSetsPlusOne()
    {
        int[] totals = new int[26];
        for(char key = 'a'; key < 'z'; key++)
        {
            for(char value = 'a'; value < 'z'; value++)
            {
                int base = key + value - 'a';
                mixture.setCount(key, value, base);
                totals[value - 'a'] += base;
                for(char j = 1; j <= 10; j++)
                {
                    mixture.incrementCount(key, value);
                    totals[value - 'a']++;
                    Assert.assertEquals(totals[value - 'a'], mixture.getCount(value));
                    Assert.assertEquals(base + j, mixture.getCountGiven(key, value));
                }
            }
        }
    }

    @Test
    public void setChangesSum()
    {
        Assert.assertEquals(0, mixture.sumCounts());
        mixture.setCount('a', 'a', 10);
        Assert.assertEquals(10, mixture.sumCounts());
        mixture.setCount('a', 'b', 5);
        Assert.assertEquals(15, mixture.sumCounts());
        mixture.setCount('a', 'a', 2);
        Assert.assertEquals(7, mixture.sumCounts());
        mixture.setCount('b', 'a', 3);
        Assert.assertEquals(10, mixture.sumCounts());
        mixture.setCount('b', 'a', 1);
        Assert.assertEquals(8, mixture.sumCounts());
        mixture.setCount('a', 'c', 2);
        Assert.assertEquals(10, mixture.sumCounts());
        mixture.setCount('c', 'd', 10);
        Assert.assertEquals(20, mixture.sumCounts());
    }

    @Test
    public void setChangesSumGiven()
    {
        Assert.assertEquals(0, mixture.sumCountGiven('a'));
        Assert.assertEquals(0, mixture.sumCountGiven('b'));
        mixture.setCount('a', 'a', 10);
        Assert.assertEquals(10, mixture.sumCountGiven('a'));
        Assert.assertEquals(0, mixture.sumCountGiven('b'));
        mixture.setCount('a', 'b', 5);
        Assert.assertEquals(15, mixture.sumCountGiven('a'));
        Assert.assertEquals(0, mixture.sumCountGiven('b'));
        mixture.setCount('a', 'a', 2);
        Assert.assertEquals(7, mixture.sumCountGiven('a'));
        Assert.assertEquals(0, mixture.sumCountGiven('b'));
        mixture.setCount('b', 'a', 3);
        Assert.assertEquals(7, mixture.sumCountGiven('a'));
        Assert.assertEquals(3, mixture.sumCountGiven('b'));
        mixture.setCount('b', 'a', 1);
        Assert.assertEquals(7, mixture.sumCountGiven('a'));
        Assert.assertEquals(1, mixture.sumCountGiven('b'));
        mixture.setCount('b', 'b', 4);
        Assert.assertEquals(7, mixture.sumCountGiven('a'));
        Assert.assertEquals(5, mixture.sumCountGiven('b'));
        mixture.setCount('c', 'b', 4);
        Assert.assertEquals(7, mixture.sumCountGiven('a'));
        Assert.assertEquals(5, mixture.sumCountGiven('b'));
        Assert.assertEquals(4, mixture.sumCountGiven('c'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void sumCountGivenNullKey()
    {
        mixture.incrementCount('a', 'a');
        mixture.sumCountGiven(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCountGivenNullKey()
    {
        mixture.incrementCount('a', 'a');
        mixture.getCountGiven(null, 'a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCountGivenNullValue()
    {
        mixture.incrementCount('a', 'a');
        mixture.getCountGiven('a', null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCountNullValue()
    {
        mixture.incrementCount('a', 'a');
        mixture.getCount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCountNullKey()
    {
        mixture.setCount(null, 'a', 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCountNullValue()
    {
        mixture.setCount('a', null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incrementCountNullKey()
    {
        mixture.incrementCount(null, 'a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void incrementCountNullValue()
    {
        mixture.incrementCount('a', null);
    }

    @Test
    public void setZero()
    {
        mixture.setCount('a', 'a', 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNegative()
    {
        mixture.setCount('a', 'a', -1);
    }

    @Test
    public void setAddsSupport()
    {
        Set<Character> expected = new HashSet<Character>();
        Assert.assertEquals(expected, mixture.support());
        mixture.setCount('a', 'b', 10);
        expected.add('b');
        Assert.assertEquals(expected, mixture.support());
        mixture.incrementCount('a', 'b');
        Assert.assertEquals(expected, mixture.support());
        mixture.incrementCount('b', 'b');
        Assert.assertEquals(expected, mixture.support());
        mixture.setCount('a', 'c', 5);
        expected.add('c');
        Assert.assertEquals(expected, mixture.support());
        mixture.setCount('a', 'c', 1);
        Assert.assertEquals(expected, mixture.support());
    }

    @Test
    public void setZeroRemovesSupport()
    {
        Set<Character> expected = new HashSet<Character>();
        for(char value = 'a'; value <= 'z'; value++)
            for(char key = 'a'; key <= 'z'; key++)
            {
                mixture.incrementCount(key, value);
                expected.add(value);
            }
        for(char value = 'a'; value <= 'z'; value++)
        {
            for(char key = 'a'; key <= 'z'; key++)
            {
                Assert.assertEquals(expected, mixture.support());
                mixture.setCount(key, value, 0);
            }
            expected.remove(value);
            Assert.assertEquals(expected, mixture.support());
        }
    }

    @Test
    public void probabilityRatiosGiven()
    {
        for(char key = 'a'; key < 'd'; key++)
        {
            mixture.incrementCount(key, 'a');
            Assert.assertEquals(1.0, mixture.probabilityGiven(key, 'a'));
            Assert.assertEquals(0.0, mixture.probabilityGiven(key, 'b'));

            mixture.incrementCount(key, 'b');
            Assert.assertEquals(0.5, mixture.probabilityGiven(key, 'a'));
            Assert.assertEquals(0.5, mixture.probabilityGiven(key, 'b'));
            Assert.assertEquals(0.0, mixture.probabilityGiven(key, 'c'));

            mixture.setCount(key, 'c', 2);
            Assert.assertEquals(0.25, mixture.probabilityGiven(key, 'a'));
            Assert.assertEquals(0.25, mixture.probabilityGiven(key, 'b'));
            Assert.assertEquals(0.5, mixture.probabilityGiven(key, 'c'));
            Assert.assertEquals(0.0, mixture.probabilityGiven(key, 'd'));

            mixture.setCount(key, 'd', 4);
            Assert.assertEquals(0.125, mixture.probabilityGiven(key, 'a'));
            Assert.assertEquals(0.125, mixture.probabilityGiven(key, 'b'));
            Assert.assertEquals(0.25, mixture.probabilityGiven(key, 'c'));
            Assert.assertEquals(0.5, mixture.probabilityGiven(key, 'd'));

            mixture.setCount(key, 'c', 1);
            mixture.setCount(key, 'd', 1);
            Assert.assertEquals(0.25, mixture.probabilityGiven(key, 'a'));
            Assert.assertEquals(0.25, mixture.probabilityGiven(key, 'b'));
            Assert.assertEquals(0.25, mixture.probabilityGiven(key, 'c'));
            Assert.assertEquals(0.25, mixture.probabilityGiven(key, 'd'));
        }
    }

    @Test
    public void probabilityRatios()
    {
        mixture.incrementCount('a', 'a');
        Assert.assertEquals(1.0, mixture.probability('a'));
        Assert.assertEquals(0.0, mixture.probability('b'));

        mixture.incrementCount('a', 'b');
        Assert.assertEquals(0.5, mixture.probability('a'));
        Assert.assertEquals(0.5, mixture.probability('b'));
        Assert.assertEquals(0.0, mixture.probability('c'));

        mixture.incrementCount('a', 'c');
        mixture.incrementCount('b', 'c');
        Assert.assertEquals(0.25, mixture.probability('a'));
        Assert.assertEquals(0.25, mixture.probability('b'));
        Assert.assertEquals(0.5, mixture.probability('c'));
        Assert.assertEquals(0.0, mixture.probability('d'));

        mixture.setCount('c', 'd', 4);
        Assert.assertEquals(0.125, mixture.probability('a'));
        Assert.assertEquals(0.125, mixture.probability('b'));
        Assert.assertEquals(0.25, mixture.probability('c'));
        Assert.assertEquals(0.5, mixture.probability('d'));

        mixture.setCount('a', 'c', 0);
        mixture.setCount('c', 'd', 1);
        Assert.assertEquals(0.25, mixture.probability('a'));
        Assert.assertEquals(0.25, mixture.probability('b'));
        Assert.assertEquals(0.25, mixture.probability('c'));
        Assert.assertEquals(0.25, mixture.probability('d'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void probabilityGivenNullKey()
    {
        mixture.incrementCount('a', 'a');
        mixture.probabilityGiven(null, 'a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void probabilityGivenNullValue()
    {
        mixture.incrementCount('a', 'a');
        mixture.probabilityGiven('a', null);
    }

    @Test
    public void probabilityGivenZeroKey()
    {
        mixture.incrementCount('a', 'a');
        Assert.assertEquals(0.0, mixture.probabilityGiven('b', 'a'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void probabilityNullValue()
    {
        mixture.incrementCount('a', 'a');
        mixture.probability(null);
    }

    @Test(expected = IllegalStateException.class)
    public void probabilityGivenEmptySupport()
    {
        mixture.probabilityGiven('a', 'a');
    }

    @Test(expected = IllegalStateException.class)
    public void probabilityEmptySupport()
    {
        mixture.probability('a');
    }

    @Test
    public void sampleUniform()
    {
        testSample(uniform());
    }

    @Test
    public void sampleStair()
    {
        testSample(stair());
    }

    @Test
    public void samplePower()
    {
        testSample(power());
    }

    @Test(expected = IllegalArgumentException.class)
    public void sampleNullDice()
    {
        mixture.setCount('a', 'a', 1);
        mixture.sample(null);
    }

    @Test(expected = IllegalStateException.class)
    public void sampleNoSupport()
    {
        mixture.sample(Dice.global);
    }

    @Test
    public void sampleDefaultDice()
    {
        mixture = Mockito.spy(mixture);
        mixture.incrementCount('a', 'a');
        Mockito.doReturn('z').when(mixture).sample(Dice.global);

        Assert.assertEquals(Character.valueOf('z'), mixture.sample());
        Mockito.verify(mixture).sample(Dice.global);
    }

    @Test
    public void sampleGivenUniform()
    {
        testSampleGiven(uniform());
    }

    @Test
    public void sampleGivenStair()
    {
        testSampleGiven(stair());
    }

    @Test
    public void sampleGivenPower()
    {
        testSampleGiven(power());
    }

    @Test(expected = IllegalArgumentException.class)
    public void sampleGivenNullDice()
    {
        mixture.setCount('a', 'a', 1);
        mixture.sampleGiven('a', null);
    }

    @Test(expected = IllegalStateException.class)
    public void sampleGivenNoSuppoort()
    {
        mixture.sampleGiven('a', Dice.global);
    }

    @Test(expected = IllegalStateException.class)
    public void sampleGivenNoGivenSupport()
    {
        mixture.setCount('b', 'a', 1);
        mixture.sampleGiven('a', Dice.global);
    }
    
    @Test
    public void sampleGivenDefaultDice()
    {
        mixture = Mockito.spy(mixture);
        mixture.incrementCount('a', 'a');
        Mockito.doReturn('z').when(mixture).sampleGiven('a', Dice.global);

        Assert.assertEquals(Character.valueOf('z'), mixture.sampleGiven('a'));
        Mockito.verify(mixture).sampleGiven('a', Dice.global);
    }

    private void testSample(int[] expected)
    {
        for(int i = 0; i < expected.length; i++)
            mixture.setCount((char)('a' + i), (char)i, expected[i]);

        Dice dice = DiceTest.getMockedSequenceDice();
        int[] actual = new int[expected.length];
        int actualSum = mixture.sumCounts() * expected.length;
        for(int i = 0; i < actualSum; i++)
            actual[mixture.sample(dice)]++;

        for(int i = 0; i < expected.length; i++)
            Assert.assertEquals(expected[i] * actualSum, actual[i] * mixture.sumCounts());
    }

    private void testSampleGiven(int[] expected)
    {
        char key = 'a';

        for(int i = 0; i < expected.length; i++)
        {
            mixture.setCount(key, (char)i, expected[i]);
            mixture.setCount((char)(key + i + 1), (char)i, i);
        }

        Dice dice = DiceTest.getMockedSequenceDice();
        int[] actual = new int[expected.length];
        int actualSum = mixture.sumCountGiven(key) * expected.length;
        for(int i = 0; i < actualSum; i++)
            actual[mixture.sampleGiven(key, dice)]++;

        for(int i = 0; i < expected.length; i++)
            Assert.assertEquals(expected[i] * actualSum, actual[i] * mixture.sumCountGiven(key));
    }
}
