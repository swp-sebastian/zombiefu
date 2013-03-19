package test.gen.feature;

import jade.core.World;
import jade.gen.Generator;
import jade.gen.feature.Fence;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import test.core.CoreTest.ConcreteWorld;

public class FenceTest
{
    private Fence fence;

    private World world;
    private int height;
    private int width;
    private Dice dice;

    private ColoredChar face;

    @Before
    public void init()
    {
        height = 15;
        width = 10;
        world = Mockito.spy(new ConcreteWorld(width, height));

        dice = Mockito.mock(Dice.class);

        fence = new Fence(Mockito.mock(Generator.class));

        face = ColoredChar.create('#');
    }

    @Test
    public void perimiterClosed()
    {
        fence.generate(world, dice);
        for(int x = 0; x < width; x++)
        {
            Assert.assertFalse(world.passableAt(x, 0));
            Assert.assertFalse(world.passableAt(x, height - 1));
        }
        for(int y = 0; y < height; y++)
        {
            Assert.assertFalse(world.passableAt(0, y));
            Assert.assertFalse(world.passableAt(width - 1, y));
        }
    }

    @Test
    public void perimiterFenced()
    {
        fence.generate(world, dice);
        for(int x = 0; x < width; x++)
        {
            Assert.assertEquals(face, world.tileAt(x, 0));
            Assert.assertEquals(face, world.tileAt(x, height - 1));
        }
        for(int y = 0; y < height; y++)
        {
            Assert.assertEquals(face, world.tileAt(0, y));
            Assert.assertEquals(face, world.tileAt(width - 1, y));
        }
    }

    @Test
    public void setTileUsed()
    {
        fence.generate(world, dice);
        for(int x = 0; x < width; x++)
        {
            Mockito.verify(world).setTile(face, false, x, 0);
            Mockito.verify(world).setTile(face, false, x, height - 1);
        }
        for(int y = 0; y < height; y++)
        {
            Mockito.verify(world).setTile(face, false, 0, y);
            Mockito.verify(world).setTile(face, false, width - 1, y);
        }
    }

    @Test
    public void diceUnused()
    {
        fence.generate(world, dice);
        Mockito.verify(dice, Mockito.never()).nextInt();
    }
}
