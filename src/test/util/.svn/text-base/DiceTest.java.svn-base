package test.util;

import jade.util.Dice;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class DiceTest
{
    private Dice dice;
    private static Random seeder;

    @BeforeClass
    public static void initSeeder()
    {
        seeder = new Random();
    }

    @Before
    public void initDice()
    {
        dice = new Dice();
    }

    @Test
    public void reseededSequences()
    {
        testSequenceMatches(new ReseedAction()
        {
            @Override
            public void reseed(int seed)
            {
                dice.reseed(seed);
            }
        });
    }

    @Test
    public void constructorReseedSequences()
    {
        ReseedAction construct = new ReseedAction()
        {
            @Override
            public void reseed(int seed)
            {
                dice = new Dice(seed);
            }
        };

        ReseedAction reseed = new ReseedAction()
        {
            @Override
            public void reseed(int seed)
            {
                dice.reseed(seed);
            }
        };

        testSequenceMatches(construct, reseed);
    }

    @Test
    public void seedCurrentTime()
    {
        dice = Mockito.mock(Dice.class, Mockito.CALLS_REAL_METHODS);
        Mockito.doAnswer(new Answer<Object>()
        {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                int curr = (int)System.currentTimeMillis();
                int seed = (Integer)invocation.getArguments()[0];
                int diff = Math.abs(seed - curr);
                Assert.assertTrue(diff < 15);
                return null;
            }
        }).when(dice).reseed(Mockito.anyInt());

        dice.reseed();
    }

    @Test
    public void globalInstance()
    {
        Assert.assertNotNull(Dice.global);
    }

    @Test
    public void nextIntMaxBounds()
    {
        for(int i = 1; i < 1000; i++)
        {
            int value = dice.nextInt(i);
            Assert.assertTrue(value < i);
            Assert.assertTrue(value >= 0);
        }
    }

    @Test
    public void nextIntMaxUniform()
    {
        dice = Mockito.mock(Dice.class, Mockito.CALLS_REAL_METHODS);
        Mockito.doAnswer(new Answer<Integer>()
        {
            int i = 0;

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable
            {
                return i++;
            }
        }).when(dice).nextInt();

        int numBins = 10;
        int[] bins = new int[numBins];
        int expectedBinSize = 10;
        for(int i = 0; i < expectedBinSize * numBins; i++)
            bins[dice.nextInt(numBins)]++;
        for(int i = 0; i < numBins; i++)
            Assert.assertEquals(expectedBinSize, bins[i]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextIntZeroMax()
    {
        dice.nextInt(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextIntNegMax()
    {
        dice.nextInt(-1);
    }

    @Test
    public void nextIntNonNegative()
    {
        for(int i = 0; i < 10000; i++)
            Assert.assertTrue(dice.nextInt() >= 0);
    }

    @Test
    public void nextIntRangeBounded()
    {
        for(int i = 1; i < 1000; i++)
        {
            int value = dice.nextInt(10, 20);
            Assert.assertTrue(value <= 20);
            Assert.assertTrue(value >= 10);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void nextIntNegRange()
    {
        dice.nextInt(10, 5);
    }

    @Test
    public void nextIntRangeUniform()
    {
        dice = Mockito.mock(Dice.class, Mockito.CALLS_REAL_METHODS);
        Mockito.doAnswer(new Answer<Integer>()
        {
            int i = 0;

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable
            {
                return i++;
            }
        }).when(dice).nextInt();

        int range = 10;
        int min = 15;
        int max = min + range - 1;
        int bins[] = new int[range];
        int expected = 10;
        for(int i = 0; i < expected * range; i++)
            bins[dice.nextInt(min, max) - min]++;
        for(int i = 0; i < range; i++)
            Assert.assertEquals(expected, bins[i]);
    }

    @Test
    public void chanceResultMatchesPercent()
    {
        int odds = 40;
        int total = 50;
        Dice dice = getMockedSequenceDice();

        int hits = 0;
        int tries = 400;
        for(int i = 0; i < tries; i++)
            if(dice.chance(odds, total))
                hits++;

        Assert.assertEquals(tries * odds / total, hits);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeOdds()
    {
        dice.chance(-1, 100);
    }

    @Test
    public void zeroOdds()
    {
        for(int i = 1; i <= 100; i++)
            Assert.assertFalse(dice.chance(0, i));
    }

    @Test
    public void perfectOdds()
    {
        for(int i = 1; i <= 100; i++)
            Assert.assertTrue(dice.chance(i, i));
    }

    @Test(expected = IllegalArgumentException.class)
    public void oddsTooHigh()
    {
        dice.chance(100, 99);
    }

    @Test
    public void defaultChanceTotal()
    {
        int percent = 40;
        Dice dice = getMockedSequenceDice();

        int hits = 0;
        int tries = 400;
        for(int i = 0; i < tries; i++)
            if(dice.chance(percent))
                hits++;

        Assert.assertEquals(tries * percent / 100, hits);
    }

    @Test
    public void defaultChanceOdds()
    {
        Dice dice = getMockedSequenceDice();

        int hits = 0;
        int tries = 400;
        for(int i = 0; i < tries; i++)
            if(dice.chance())
                hits++;

        Assert.assertEquals(tries / 2, hits);
    }

    @Test
    public void chooseUniformDistribution()
    {
        dice = getMockedSequenceDice();

        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < 10; i++)
            list.add(i);

        int[] bins = new int[list.size()];
        int tries = 200;
        for(int i = 0; i < tries; i++)
            bins[dice.choose(list)]++;

        for(int i = 0; i < bins.length; i++)
            Assert.assertEquals(tries / bins.length, bins[i]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void chooseNullList()
    {
        dice.choose(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void chooseEmptyList()
    {
        dice.choose(new ArrayList<String>());
    }

    public static Dice getMockedSequenceDice()
    {
        Dice dice = Mockito.mock(Dice.class, Mockito.CALLS_REAL_METHODS);
        Mockito.doAnswer(new Answer<Integer>()
        {
            private int curr = 0;

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable
            {
                return curr++;
            }
        }).when(dice).nextInt();
        return dice;
    }

    private void testSequenceMatches(ReseedAction reseed)
    {
        testSequenceMatches(reseed, reseed);
    }

    private void testSequenceMatches(ReseedAction reseed1, ReseedAction reseed2)
    {
        testSequenceMatches(reseed1, reseed2, 1000, 100);
        testSequenceMatches(reseed1, reseed2, 100, 1000);
        testSequenceMatches(reseed1, reseed2, 10, 10000);
        testSequenceMatches(reseed1, reseed2, 1, 100000);
    }

    private void testSequenceMatches(ReseedAction reseed1, ReseedAction reseed2, int seeds,
            int length)
    {
        int[] sample = new int[length];
        for(int i = 0; i < seeds; i++)
        {
            int seed = seeder.nextInt();

            reseed1.reseed(seed);
            for(int j = 0; j < length; j++)
                sample[j] = dice.nextInt();

            reseed2.reseed(seed);
            for(int j = 0; j < length; j++)
                Assert.assertEquals(sample[j], dice.nextInt());
        }
    }

    private static interface ReseedAction
    {
        public void reseed(int seed);
    }
}
