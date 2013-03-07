package test.core;

import jade.core.Actor;
import jade.core.World;
import jade.util.datatype.ColoredChar;
import org.junit.Assert;
import org.junit.Before;

public abstract class CoreTest
{
    protected World world;
    protected Actor actor;

    @Before
    public void init()
    {
        world = new ConcreteWorld();
        actor = new ConcreteActor(ColoredChar.create('@'));
    }

    public static void testBounds(BoundTest test, int width, int height)
    {
        int insideWidth = width / 2;
        int insideHeight = height / 2;

        test.action(width - 1, height - 1);
        test.action(insideWidth, insideHeight);
        test.action(0, 0);

        expectedBoundException(test, -1, insideHeight);
        expectedBoundException(test, width, insideHeight);
        expectedBoundException(test, insideWidth, -1);
        expectedBoundException(test, insideWidth, height);
    }

    private static void expectedBoundException(BoundTest test, int x, int y)
    {
        boolean expectedCaught = false;
        try
        {
            test.action(x, y);
        }
        catch(IndexOutOfBoundsException exception)
        {
            expectedCaught = true;
        }
        if(!expectedCaught)
            Assert.fail();
    }

    public static interface BoundTest
    {
        public void action(int x, int y);
    }

    public static class ConcreteWorld extends World
    {
        public ConcreteWorld()
        {
            this(10, 15);
        }

        public ConcreteWorld(int width, int height)
        {
            super(width, height);
        }
    }

    public static class ConcreteActor extends Actor
    {
        public ConcreteActor(ColoredChar face)
        {
            super(face);
        }

        @Override
        public void act()
        {}
    }

    public static class ActorA extends ConcreteActor
    {
        public ActorA()
        {
            super(ColoredChar.create('@'));
        }
    }

    public static class ActorB extends ConcreteActor
    {
        public ActorB()
        {
            super(ColoredChar.create('D'));
        }
    }
}
