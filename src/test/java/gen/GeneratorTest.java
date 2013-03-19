package test.gen;

import jade.core.World;
import jade.gen.Generator;
import jade.gen.feature.FeatureGenerator;
import jade.gen.map.MapGenerator;
import jade.util.Dice;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import test.core.CoreTest.ConcreteWorld;

public class GeneratorTest
{
    private World world;
    private Dice dice;

    @Before
    public void init()
    {
        world = new ConcreteWorld();
        dice = new Dice();
    }

    @Test
    public void generateCallStep()
    {
        ConcreteGenerator gen = Mockito.mock(ConcreteGenerator.class);
        Mockito.doCallRealMethod().when(gen).generate(world, dice);
        gen.generate(world, dice);
        Mockito.verify(gen).generateStep(world, dice);
    }

    @Test
    public void generateCallStepDefaultDice()
    {
        ConcreteGenerator gen = Mockito.mock(ConcreteGenerator.class);
        Mockito.doCallRealMethod().when(gen).generate(world);
        Mockito.doCallRealMethod().when(gen).generate(world, Dice.global);
        gen.generate(world);
        Mockito.verify(gen).generate(world, Dice.global);
        Mockito.verify(gen).generateStep(world, Dice.global);
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateNullDice()
    {
        ConcreteMapGenerator gen = new ConcreteMapGenerator();
        gen.generate(world, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateNullWorld()
    {
        ConcreteMapGenerator gen = new ConcreteMapGenerator();
        gen.generate(null, dice);
    }

    @Test
    public void generateChainedStep()
    {
        ConcreteGenerator chained = Mockito.mock(ConcreteGenerator.class);
        ConcreteGenerator gen = new ConcreteGenerator(chained);
        gen.generate(world, dice);
        Mockito.verify(chained).generate(world, dice);
    }

    @Test
    public void chainedNullSafe()
    {
        ConcreteGenerator gen = new ConcreteGenerator(null);
        gen.generate(world, dice);
    }

    @Test
    public void chainedNullConstructorSafe()
    {
        ConcreteGenerator gen = new ConcreteGenerator();
        gen.generate(world, dice);
    }

    @Test(expected = IllegalArgumentException.class)
    public void featureGenRequiresChain()
    {
        ConcreteFeatureGenerator gen = new ConcreteFeatureGenerator(null);
        Assert.assertNull(gen);
    }

    @Test
    public void featureMapChain()
    {
        ConcreteMapGenerator map = Mockito.spy(new ConcreteMapGenerator());
        ConcreteFeatureGenerator feature = Mockito.spy(new ConcreteFeatureGenerator(map));
        feature.generate(world, dice);
        Mockito.verify(feature).generateStep(world, dice);
        Mockito.verify(map).generateStep(world, dice);
    }

    private static class ConcreteGenerator extends Generator
    {
        public ConcreteGenerator(Generator chained)
        {
            super(chained);
        }

        public ConcreteGenerator()
        {
            super();
        }

        @Override
        public void generateStep(World world, Dice dice)
        {}
    }

    private static class ConcreteFeatureGenerator extends FeatureGenerator
    {
        public ConcreteFeatureGenerator(Generator chained)
        {
            super(chained);
        }

        @Override
        protected void generateStep(World world, Dice dice)
        {}
    }

    private static class ConcreteMapGenerator extends MapGenerator
    {
        @Override
        protected void generateStep(World world, Dice dice)
        {}
    }
}
