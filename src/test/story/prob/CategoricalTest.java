package test.story.prob;

import jade.story.prob.Categorical;
import jade.util.Dice;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import test.util.DiceTest;

public class CategoricalTest extends ProbabilityTest
{
    private Categorical<Character> categorical;

    @Before
    public void init()
    {
        categorical = new Categorical<Character>();
    }

    @Test
    public void countGetSet()
    {
        for(char key = 'a'; key < 'z'; key++)
            categorical.setCount(key, (int)key);
        for(char key = 'a'; key < 'z'; key++)
            Assert.assertEquals((int)key, categorical.getCount(key));
    }

    @Test
    public void countGetDefault()
    {
        for(char key = 'a'; key < 'z'; key++)
            Assert.assertEquals(0, categorical.getCount(key));
    }

    @Test
    public void incrementSetsPlusOne()
    {
        for(char key = 'a'; key < 'z'; key++)
        {
            int base = key - 'a';
            categorical.setCount(key, base);
            for(char j = 1; j <= 10; j++)
            {
                categorical.incrementCount(key);
                Assert.assertEquals(base + j, categorical.getCount(key));
            }
        }
    }

    @Test
    public void setChangesSum()
    {
        Assert.assertEquals(0, categorical.sumCounts());
        categorical.setCount('a', 10);
        Assert.assertEquals(10, categorical.sumCounts());
        categorical.setCount('a', 5);
        Assert.assertEquals(5, categorical.sumCounts());
        categorical.setCount('b', 3);
        Assert.assertEquals(8, categorical.sumCounts());
        categorical.setCount('b', 5);
        Assert.assertEquals(10, categorical.sumCounts());
        categorical.setCount('c', 10);
        Assert.assertEquals(20, categorical.sumCounts());
    }

    @Test
    public void incrementChangesSum()
    {
        Assert.assertEquals(0, categorical.sumCounts());
        categorical.incrementCount('a');
        Assert.assertEquals(1, categorical.sumCounts());
        categorical.incrementCount('a');
        Assert.assertEquals(2, categorical.sumCounts());
        categorical.incrementCount('b');
        Assert.assertEquals(3, categorical.sumCounts());
        categorical.incrementCount('c');
        Assert.assertEquals(4, categorical.sumCounts());
        categorical.incrementCount('a');
        Assert.assertEquals(5, categorical.sumCounts());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullCount()
    {
        categorical.getCount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullCount()
    {
        categorical.setCount(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incrementNullCount()
    {
        categorical.incrementCount(null);
    }

    @Test
    public void setZero()
    {
        categorical.setCount('a', 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNegative()
    {
        categorical.setCount('a', -1);
    }

    @Test
    public void setAddsSupport()
    {
        Set<Character> expected = new HashSet<Character>();
        Assert.assertEquals(expected, categorical.support());
        categorical.setCount('a', 10);
        expected.add('a');
        Assert.assertEquals(expected, categorical.support());
        categorical.incrementCount('b');
        expected.add('b');
        Assert.assertEquals(expected, categorical.support());
        categorical.incrementCount('b');
        Assert.assertEquals(expected, categorical.support());
        categorical.setCount('a', 5);
        Assert.assertEquals(expected, categorical.support());
        categorical.setCount('c', 1);
        expected.add('c');
        Assert.assertEquals(expected, categorical.support());
    }

    @Test
    public void setZeroRemovesSupport()
    {
        Set<Character> expected = new HashSet<Character>();
        for(char key = 'a'; key <= 'z'; key++)
        {
            categorical.incrementCount(key);
            expected.add(key);
        }
        for(char key = 'a'; key <= 'z'; key++)
        {
            categorical.setCount(key, 0);
            expected.remove(key);
            Assert.assertEquals(expected, categorical.support());
        }
    }

    @Test
    public void probabilityRatios()
    {
        categorical.incrementCount('a');
        Assert.assertEquals(1.0, categorical.probability('a'));
        Assert.assertEquals(0.0, categorical.probability('b'));

        categorical.incrementCount('b');
        Assert.assertEquals(0.5, categorical.probability('a'));
        Assert.assertEquals(0.5, categorical.probability('b'));
        Assert.assertEquals(0.0, categorical.probability('c'));

        categorical.setCount('c', 2);
        Assert.assertEquals(0.25, categorical.probability('a'));
        Assert.assertEquals(0.25, categorical.probability('b'));
        Assert.assertEquals(0.5, categorical.probability('c'));
        Assert.assertEquals(0.0, categorical.probability('d'));

        categorical.setCount('d', 4);
        Assert.assertEquals(0.125, categorical.probability('a'));
        Assert.assertEquals(0.125, categorical.probability('b'));
        Assert.assertEquals(0.25, categorical.probability('c'));
        Assert.assertEquals(0.5, categorical.probability('d'));

        categorical.setCount('c', 1);
        categorical.setCount('d', 1);
        Assert.assertEquals(0.25, categorical.probability('a'));
        Assert.assertEquals(0.25, categorical.probability('b'));
        Assert.assertEquals(0.25, categorical.probability('c'));
        Assert.assertEquals(0.25, categorical.probability('d'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void probabilityNull()
    {
        categorical.incrementCount('a');
        categorical.probability(null);
    }

    @Test(expected = IllegalStateException.class)
    public void probabilityEmptySupport()
    {
        categorical.probability('a');
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
        categorical.incrementCount('a');
        categorical.sample(null);
    }

    @Test(expected = IllegalStateException.class)
    public void sampleNoSupport()
    {
        categorical.sample(Dice.global);
    }

    @Test
    public void sampleDefaultDice()
    {
        categorical = Mockito.spy(categorical);
        categorical.incrementCount('a');
        Mockito.doReturn('z').when(categorical).sample(Dice.global);

        Assert.assertEquals(Character.valueOf('z'), categorical.sample());
        Mockito.verify(categorical).sample(Dice.global);
    }

    public void testSample(int[] expected)
    {
        Dice dice = DiceTest.getMockedSequenceDice();

        Categorical<Integer> dist = new Categorical<Integer>();

        for(int i = 0; i < expected.length; i++)
            dist.setCount(i, expected[i]);

        int[] actual = new int[expected.length];
        int actualSum = dist.sumCounts() * expected.length;
        for(int i = 0; i < actualSum; i++)
            actual[dist.sample(dice)]++;

        for(int i = 0; i < expected.length; i++)
            Assert.assertEquals(expected[i] * actualSum, actual[i] * dist.sumCounts());
    }
}
