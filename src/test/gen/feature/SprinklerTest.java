package test.gen.feature;

import jade.core.Actor;
import jade.core.World;
import jade.gen.Generator;
import jade.gen.feature.Sprinkler;
import jade.gen.feature.Sprinkler.SprinklerPart;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import test.core.CoreTest.ConcreteActor;
import test.core.CoreTest.ConcreteWorld;

public class SprinklerTest
{
    private SprinklerPart part;
    private Dice dice;
    private World world;
    private Sprinkler sprinkler;

    @Before
    public void init()
    {
        dice = Mockito.mock(Dice.class);
        part = Mockito.mock(SprinklerPart.class);
        Mockito.when(part.decide(Mockito.eq(dice), Mockito.anyInt(), Mockito.anyInt())).thenReturn(
                false);
        Answer<Actor> answer = new Answer<Actor>()
        {
            @Override
            public Actor answer(InvocationOnMock invocation) throws Throwable
            {
                return new ConcreteActor(ColoredChar.create('@'));
            }
        };
        Mockito.when(part.getActor(Mockito.eq(dice), Mockito.anyInt(), Mockito.anyInt()))
                .thenAnswer(answer);
        world = new ConcreteWorld(15, 15);

        sprinkler = new Sprinkler(Mockito.mock(Generator.class), part);
    }

    @Test
    public void decidePreventsPlacement()
    {
        sprinkler.generate(world, dice);

        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                Assert.assertNull(world.getActorAt(Actor.class, x, y));
    }

    @Test
    public void impassablePreventsPlacement()
    {
        Mockito.when(part.decide(Mockito.eq(dice), Mockito.anyInt(), Mockito.anyInt())).thenReturn(
                true);
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                world.setTile(ColoredChar.create('.'), false, x, y);
        sprinkler.generate(world, dice);

        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                Assert.assertNull(world.getActorAt(Actor.class, x, y));
    }

    @Test
    public void passableWithPartAllowsPlacement()
    {
        int allowedX = 4;
        int allowedY = 8;
        Mockito.when(part.decide(dice, allowedX, allowedY)).thenReturn(true);
        sprinkler.generate(world, dice);

        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                if(x != allowedX || y != allowedY)
                    Assert.assertNull(world.getActorAt(Actor.class, x, y));
                else
                    Assert.assertNotNull(world.getActorAt(Actor.class, x, y));
            }
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void nullPart()
    {
        new Sprinkler(Mockito.mock(Generator.class), null);
    }
}
