package test.core;

import jade.core.Actor;
import jade.core.World;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class WorldTest extends CoreTest
{
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    @Before
    @Override
    public void init()
    {
        super.init();
        x1 = 0;
        y1 = 0;
        x2 = world.width() - 1;
        y2 = world.height() - 1;
    }

    @Test
    public void constructorBounds()
    {
        world = new ConcreteWorld(10, 15);
        Assert.assertEquals(10, world.width());
        Assert.assertEquals(15, world.height());
        world = new ConcreteWorld(15, 30);
        Assert.assertEquals(15, world.width());
        Assert.assertEquals(30, world.height());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorZeroWidth()
    {
        world = new ConcreteWorld(0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorZeroHeight()
    {
        world = new ConcreteWorld(10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNegativeWidth()
    {
        world = new ConcreteWorld(-1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNegativeHeight()
    {
        world = new ConcreteWorld(10, -1);
    }

    @Test
    public void addActorBinds()
    {
        world.addActor(actor, 5, 5);
        Assert.assertSame(world, actor.world());
        Assert.assertTrue(actor.bound());
        Assert.assertTrue(actor.bound(world));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullActor()
    {
        world.addActor(null, 5, 5);
    }

    @Test
    public void addActorBounds()
    {
        testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                world.addActor(new ConcreteActor(ColoredChar.create('@')), x, y);
            }
        }, world.width(), world.height());
    }

    @Test
    public void addActorSetsPos()
    {
        world.addActor(actor, 4, 5);
        Assert.assertEquals(4, actor.x());
        Assert.assertEquals(5, actor.y());
    }

    @Test
    public void addActorCoord()
    {
        world = Mockito.spy(world);
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                Actor actor = new ConcreteActor(ColoredChar.create('@'));
                world.addActor(actor, new Coordinate(x, y));
                Mockito.verify(world).addActor(actor, x, y);
            }
    }

    @Test(expected = IllegalArgumentException.class)
    public void addActorNullCoord()
    {
        world.addActor(actor, (Coordinate)null);
    }

    @Test(expected = IllegalStateException.class)
    public void addBoundActor()
    {
        World other = new ConcreteWorld();
        other.addActor(actor, 5, 5);
        world.addActor(actor, 5, 5);
    }

    @Test
    public void addActorDice()
    {
        int x = 3;
        int y = 6;
        Dice dice = Mockito.mock(Dice.class);
        world = Mockito.spy(world);
        Mockito.doReturn(new Coordinate(x, y)).when(world).getOpenTile(dice);

        world.addActor(actor, dice);

        Assert.assertSame(actor, world.getActorAt(Actor.class, x, y));
        Mockito.verify(world).getOpenTile(dice);
        Mockito.verify(world).addActor(actor, x, y);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addActorNullDice()
    {
        world.addActor(actor, (Dice)null);
    }

    @Test
    public void addActorDefaultDice()
    {
        int x = 7;
        int y = 4;
        world = Mockito.spy(world);
        Mockito.doReturn(new Coordinate(x, y)).when(world).getOpenTile(Dice.global);

        world.addActor(actor);

        Assert.assertSame(actor, world.getActorAt(Actor.class, x, y));
        Mockito.verify(world).getOpenTile(Dice.global);
        Mockito.verify(world).addActor(actor, x, y);
    }

    @Test
    public void getActorAtOne()
    {
        world.addActor(actor, 4, 5);
        Assert.assertSame(actor, world.getActorAt(Actor.class, 4, 5));
    }

    @Test
    public void getActorAtNone()
    {
        Assert.assertNull(world.getActorAt(Actor.class, 5, 4));
        Assert.assertNull(world.getActorAt(Actor.class, 4, 5));
        Assert.assertNull(world.getActorAt(Actor.class, 2, 8));
        Assert.assertNull(world.getActorAt(Actor.class, 7, 4));
    }

    @Test
    public void getActorAtMulti()
    {
        Actor actor1 = new ConcreteActor(ColoredChar.create('@'));
        world.addActor(actor1, 5, 4);
        Assert.assertSame(actor1, world.getActorAt(Actor.class, 5, 4));
        Actor actor2 = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(actor2, 5, 4);
        Actor result = world.getActorAt(Actor.class, 5, 4);
        Assert.assertTrue(result == actor1 || result == actor2);
    }

    @Test
    public void getActorAtFilters()
    {
        Actor actorA = new ActorA();
        Actor actorB = new ActorB();
        world.addActor(actorA, 6, 3);
        world.addActor(actorB, 6, 3);
        Assert.assertSame(actorA, world.getActorAt(ActorA.class, 6, 3));
        Assert.assertSame(actorB, world.getActorAt(ActorB.class, 6, 3));
        Actor result = world.getActorAt(Actor.class, 6, 3);
        Assert.assertTrue(result == actorA || result == actorB);
    }

    @Test
    public void getActorAtCoord()
    {
        Coordinate pos = new Coordinate(4, 6);
        world = Mockito.spy(world);
        Mockito.doReturn(actor).when(world).getActorAt(Actor.class, pos.x(), pos.y());
        Assert.assertSame(actor, world.getActorAt(Actor.class, pos));
        Mockito.verify(world).getActorAt(Actor.class, pos.x(), pos.y());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getActorAtNullCoord()
    {
        world.getActorAt(Actor.class, null);
    }

    @Test
    public void getActorAtBounds()
    {
        testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                world.getActorAt(Actor.class, x, y);
            }
        }, world.width(), world.height());
    }

    @Test
    public void getActorsAtOne()
    {
        world.addActor(actor, 5, 4);
        Assert.assertTrue(world.getActorsAt(Actor.class, 5, 4).contains(actor));
        Assert.assertEquals(1, world.getActorsAt(Actor.class, 5, 4).size());
    }

    @Test
    public void getActorsAtNone()
    {
        Assert.assertEquals(0, world.getActorsAt(Actor.class, 5, 4).size());
        Assert.assertEquals(0, world.getActorsAt(Actor.class, 2, 7).size());
        Assert.assertEquals(0, world.getActorsAt(Actor.class, 6, 9).size());
        Assert.assertEquals(0, world.getActorsAt(Actor.class, 0, 3).size());
    }

    @Test
    public void getActorsAtMulti()
    {
        Actor actor1 = new ConcreteActor(ColoredChar.create('@'));
        world.addActor(actor1, 5, 4);
        Assert.assertTrue(world.getActorsAt(Actor.class, 5, 4).contains(actor1));
        Assert.assertEquals(1, world.getActorsAt(Actor.class, 5, 4).size());
        Actor actor2 = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(actor2, 5, 4);
        Assert.assertTrue(world.getActorsAt(Actor.class, 5, 4).contains(actor1));
        Assert.assertTrue(world.getActorsAt(Actor.class, 5, 4).contains(actor2));
        Assert.assertEquals(2, world.getActorsAt(Actor.class, 5, 4).size());
    }

    @Test
    public void getActorsAtFilters()
    {
        Actor actorA1 = new ActorA();
        Actor actorA2 = new ActorA();
        Actor actorB1 = new ActorB();
        Actor actorB2 = new ActorB();
        world.addActor(actorA1, 6, 3);
        world.addActor(actorB1, 6, 3);
        world.addActor(actorA2, 6, 3);
        world.addActor(actorB2, 6, 3);
        Assert.assertTrue(world.getActorsAt(ActorA.class, 6, 3).contains(actorA1));
        Assert.assertTrue(world.getActorsAt(ActorA.class, 6, 3).contains(actorA2));
        Assert.assertEquals(2, world.getActorsAt(ActorA.class, 6, 3).size());
        Assert.assertTrue(world.getActorsAt(ActorB.class, 6, 3).contains(actorB1));
        Assert.assertTrue(world.getActorsAt(ActorB.class, 6, 3).contains(actorB2));
        Assert.assertEquals(2, world.getActorsAt(ActorB.class, 6, 3).size());
        Assert.assertTrue(world.getActorsAt(Actor.class, 6, 3).contains(actorA1));
        Assert.assertTrue(world.getActorsAt(Actor.class, 6, 3).contains(actorA2));
        Assert.assertTrue(world.getActorsAt(Actor.class, 6, 3).contains(actorB1));
        Assert.assertTrue(world.getActorsAt(Actor.class, 6, 3).contains(actorB2));
        Assert.assertEquals(4, world.getActorsAt(Actor.class, 6, 3).size());
    }

    @Test
    public void getActorsAtCoord()
    {
        Collection<Actor> actors = new HashSet<Actor>();
        Coordinate pos = new Coordinate(4, 6);
        world = Mockito.spy(world);
        Mockito.doReturn(actors).when(world).getActorsAt(Actor.class, pos.x(), pos.y());
        Assert.assertSame(actors, world.getActorsAt(Actor.class, pos));
        Mockito.verify(world).getActorsAt(Actor.class, pos.x(), pos.y());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getActorsAtNullCoord()
    {
        world.getActorsAt(Actor.class, null);
    }

    @Test
    public void getActorsAtBounds()
    {
        testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                world.getActorsAt(Actor.class, x, y);
            }
        }, world.width(), world.height());
    }

    @Test
    public void getActorsOne()
    {
        world.addActor(actor, 5, 5);
        Assert.assertTrue(world.getActors(Actor.class).contains(actor));
        Assert.assertEquals(1, world.getActors(Actor.class).size());
    }

    @Test
    public void getActorsNone()
    {
        Assert.assertEquals(0, world.getActors(Actor.class).size());
    }

    @Test
    public void getActorsMulti()
    {
        Actor actor1 = new ConcreteActor(ColoredChar.create('@'));
        world.addActor(actor1, 5, 5);
        Assert.assertTrue(world.getActors(Actor.class).contains(actor1));
        Assert.assertEquals(1, world.getActors(Actor.class).size());
        Actor actor2 = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(actor2, 3, 7);
        Collection<Actor> result = world.getActors(Actor.class);
        Assert.assertTrue(result.contains(actor1));
        Assert.assertTrue(result.contains(actor2));
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void getActorsFilters()
    {
        Actor actorA1 = new ActorA();
        Actor actorA2 = new ActorA();
        Actor actorB1 = new ActorB();
        Actor actorB2 = new ActorB();
        world.addActor(actorA1, 2, 5);
        world.addActor(actorB1, 5, 8);
        world.addActor(actorA2, 7, 4);
        world.addActor(actorB2, 6, 6);
        Assert.assertTrue(world.getActors(ActorA.class).contains(actorA1));
        Assert.assertTrue(world.getActors(ActorA.class).contains(actorA2));
        Assert.assertEquals(2, world.getActors(ActorA.class).size());
        Assert.assertTrue(world.getActors(ActorB.class).contains(actorB1));
        Assert.assertTrue(world.getActors(ActorB.class).contains(actorB2));
        Assert.assertEquals(2, world.getActors(ActorB.class).size());
        Assert.assertTrue(world.getActors(Actor.class).contains(actorA1));
        Assert.assertTrue(world.getActors(Actor.class).contains(actorA2));
        Assert.assertTrue(world.getActors(Actor.class).contains(actorB1));
        Assert.assertTrue(world.getActors(Actor.class).contains(actorB2));
        Assert.assertEquals(4, world.getActors(Actor.class).size());
    }

    @Test
    public void getActorOne()
    {
        world.addActor(actor, 5, 5);
        Assert.assertSame(actor, world.getActor(Actor.class));
    }

    @Test
    public void getActorNone()
    {
        Assert.assertNull(world.getActor(Actor.class));
    }

    @Test
    public void getActorMulti()
    {
        Actor actor1 = new ConcreteActor(ColoredChar.create('@'));
        world.addActor(actor1, 5, 5);
        Assert.assertSame(actor1, world.getActor(Actor.class));
        Actor actor2 = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(actor2, 3, 7);
        Actor result = world.getActor(Actor.class);
        Assert.assertTrue(result == actor1 || result == actor2);
    }

    @Test
    public void getActorFilters()
    {
        Actor actorA = new ActorA();
        Actor actorB = new ActorB();
        world.addActor(actorA, 8, 3);
        world.addActor(actorB, 2, 9);
        Assert.assertSame(actorA, world.getActor(ActorA.class));
        Assert.assertSame(actorB, world.getActor(ActorB.class));
        Actor result = world.getActor(Actor.class);
        Assert.assertTrue(result == actorA || result == actorB);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullActor()
    {
        world.removeActor(null);
    }

    @Test(expected = IllegalStateException.class)
    public void removeUnboundActor()
    {
        world.removeActor(actor);
    }

    @Test(expected = IllegalStateException.class)
    public void removeBoundOtherActor()
    {
        World other = new ConcreteWorld();
        other.addActor(actor, 5, 5);
        world.removeActor(actor);
    }

    @Test
    public void removeUnbinds()
    {
        world.addActor(actor, 5, 5);
        world.removeActor(actor);
        Assert.assertNull(actor.world());
        Assert.assertFalse(actor.bound());
    }

    @Test
    public void removeExpiredUnbinds()
    {
        world.addActor(actor, 5, 5);
        actor.expire();
        world.removeActor(actor);
        
        Assert.assertNull(actor.world());
        Assert.assertFalse(actor.bound());
        Assert.assertNull(world.getActorAt(Actor.class, 5, 5));
        Assert.assertFalse(world.getActorsAt(Actor.class, 5, 5).contains(actor));
        Assert.assertTrue(actor.expired());
    }
    
    @Test
    public void expireEnablesHeldRemove()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(holder, 5, 5);
        actor.attach(holder);
        actor.expire();
        world.removeActor(actor);
        
        Assert.assertNull(actor.world());
        Assert.assertFalse(actor.bound());
        Assert.assertSame(holder, world.getActorAt(Actor.class, 5, 5));
        Assert.assertEquals(1, world.getActorsAt(Actor.class, 5, 5).size());
        Assert.assertFalse(world.getActorsAt(Actor.class, 5, 5).contains(actor));
        Assert.assertTrue(actor.expired());
    }

    @Test
    public void removeActorUnplaces()
    {
        world.addActor(actor, 5, 5);
        world.removeActor(actor);
        Assert.assertNull(world.getActorAt(Actor.class, 5, 5));
        Assert.assertFalse(world.getActorsAt(Actor.class, 5, 5).contains(actor));
    }

    @Test
    public void removeActorUnregisters()
    {
        world.addActor(actor, 5, 5);
        world.removeActor(actor);
        Assert.assertEquals(0, world.getActors(Actor.class).size());
        Assert.assertNull(world.getActor(Actor.class));
    }

    @Test
    public void RemoveSelectsActor()
    {
        Actor actor1 = new ConcreteActor(ColoredChar.create('@'));
        Actor actor2 = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(actor1, 5, 6);
        world.addActor(actor2, 5, 6);
        world.removeActor(actor2);

        Assert.assertEquals(1, world.getActorsAt(Actor.class, 5, 6).size());
        Assert.assertTrue(world.getActorsAt(Actor.class, 5, 6).contains(actor1));
        Assert.assertFalse(world.getActorsAt(Actor.class, 5, 6).contains(actor2));

        Assert.assertEquals(1, world.getActors(Actor.class).size());
        Assert.assertTrue(world.getActors(Actor.class).contains(actor1));
        Assert.assertFalse(world.getActors(Actor.class).contains(actor2));
    }

    @Test(expected = IllegalStateException.class)
    public void removeActorUnbindsPosX()
    {
        world.addActor(actor, 5, 5);
        world.removeActor(actor);
        actor.x();
    }

    @Test(expected = IllegalStateException.class)
    public void removeActorUnbindsPosY()
    {
        world.addActor(actor, 5, 5);
        world.removeActor(actor);
        actor.y();
    }

    @Test
    public void defaultTile()
    {
        ColoredChar defaultTile = ColoredChar.create('.');
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                Assert.assertEquals(defaultTile, world.tileAt(x, y));
    }

    @Test
    public void defaultPassable()
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                Assert.assertTrue(world.passableAt(x, y));
    }

    @Test
    public void tileBounds()
    {
        testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                world.tileAt(x, y);
            }
        }, world.width(), world.height());
    }

    @Test
    public void tileAtCoord()
    {
        ColoredChar expected = ColoredChar.create('%');
        world = Mockito.spy(world);
        Mockito.doReturn(expected).when(world).tileAt(Mockito.anyInt(), Mockito.anyInt());
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                Assert.assertEquals(expected, world.tileAt(new Coordinate(x, y)));
                Mockito.verify(world).tileAt(x, y);
            }
    }

    @Test(expected = IllegalArgumentException.class)
    public void tileAtNullCoord()
    {
        world.tileAt(null);
    }

    @Test
    public void passableBounds()
    {
        testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                world.passableAt(x, y);
            }
        }, world.width(), world.height());
    }

    @Test
    public void passableAtCoord()
    {
        boolean expected = false;
        world = Mockito.spy(world);
        Mockito.doReturn(expected).when(world).passableAt(Mockito.anyInt(), Mockito.anyInt());
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                Assert.assertEquals(expected, world.passableAt(new Coordinate(x, y)));
                Mockito.verify(world).passableAt(x, y);
            }
    }

    @Test(expected = IllegalArgumentException.class)
    public void passableAtNullCoord()
    {
        world.passableAt(null);
    }

    @Test
    public void setTilePassable()
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                world.setTile(ColoredChar.create('.'), x % 2 == 0, x, y);

        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                Assert.assertEquals(x % 2 == 0, world.passableAt(x, y));
    }

    @Test
    public void setTileFace()
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                ColoredChar face = ColoredChar.create((char)x, new Color(y));
                world.setTile(face, true, x, y);
            }

        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                ColoredChar face = ColoredChar.create((char)x, new Color(y));
                Assert.assertEquals(face, world.tileAt(x, y));
            }
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullFace()
    {
        world.setTile(null, true, 5, 5);
    }

    @Test
    public void setTileBounds()
    {
        testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                world.setTile(ColoredChar.create('.'), true, x, y);
            }
        }, world.width(), world.height());
    }

    @Test
    public void setTileCoord()
    {
        ColoredChar face = ColoredChar.create('.');
        boolean passable = false;
        world = Mockito.spy(world);
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                world.setTile(face, passable, new Coordinate(x, y));
                Mockito.verify(world).setTile(face, passable, x, y);
            }
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTileNullCoord()
    {
        world.setTile(ColoredChar.create('.'), false, null);
    }

    @Test
    public void getOpenTileSkipsClosed()
    {
        Dice dice = Mockito.mock(Dice.class);
        world.setTile(ColoredChar.create('#'), false, 1, 4);
        world.setTile(ColoredChar.create('#'), false, 2, 5);
        world.setTile(ColoredChar.create('.'), true, 3, 6);
        Assert.assertNotSame(world.width(), world.height());
        Mockito.when(dice.nextInt()).thenReturn(1, 4, 2, 5, 3, 6);
        Assert.assertEquals(new Coordinate(3, 6), world.getOpenTile(dice, 1, 1, 6, 6));
    }

    @Test
    public void getOpenTileSearchesIfDiceFails()
    {
        x2 = world.width() / 2;
        y2 = world.height() / 2;
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                if(x <= x2 && y <= y2)
                    world.setTile(ColoredChar.create('#'), false, x, y);
                else
                    world.setTile(ColoredChar.create('.'), true, x, y);
            }
        Coordinate expected = new Coordinate(x2, y2);
        world.setTile(ColoredChar.create('.'), true, expected);
        Dice dice = Mockito.mock(Dice.class);
        Mockito.when(dice.nextInt()).thenReturn(0);
        Assert.assertEquals(expected, world.getOpenTile(dice, 0, 0, x2, y2));
        Mockito.verify(dice, Mockito.times(200)).nextInt();
    }

    @Test
    public void getOpenTileClosedWorld()
    {
        x2 = world.width() / 2;
        y2 = world.height() / 2;
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                if(x <= x2 && y <= y2)
                    world.setTile(ColoredChar.create('#'), false, x, y);
                else
                    world.setTile(ColoredChar.create('.'), true, x, y);
            }
        Dice dice = Mockito.mock(Dice.class);
        Mockito.when(dice.nextInt()).thenReturn(0);
        Assert.assertNull(world.getOpenTile(dice, x1, y1, x2, y2));
        Mockito.verify(dice, Mockito.times(200)).nextInt();
    }

    @Test
    public void getOpenTileDefaults()
    {
        Coordinate expected = new Coordinate(-1, -1);
        world = Mockito.spy(world);
        Mockito.doReturn(expected).when(world).getOpenTile(Dice.global, x1, y1, x2, y2);
        Coordinate actual = world.getOpenTile();
        Assert.assertSame(expected, actual);
        Mockito.verify(world).getOpenTile(Dice.global, x1, y1, x2, y2);
    }

    @Test
    public void getOpenTileDiceCoords()
    {
        x2 = world.width() / 2;
        y2 = world.height() / 2;
        Coordinate upLeft = new Coordinate(x1, y1);
        Coordinate downRight = new Coordinate(x2, y2);
        Coordinate expected = new Coordinate(-1, -1);

        world = Mockito.spy(world);
        Mockito.doReturn(expected).when(world).getOpenTile(Dice.global, x1, y1, x2, y2);
        Coordinate actual = world.getOpenTile(Dice.global, upLeft, downRight);
        Assert.assertSame(expected, actual);
        Mockito.verify(world).getOpenTile(Dice.global, x1, y1, x2, y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOpenTileDiceNullLeft()
    {
        world.getOpenTile(Dice.global, null, new Coordinate(1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOpenTileDiceNullRight()
    {
        world.getOpenTile(Dice.global, new Coordinate(0, 0), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOpenTileNullDiceCoords()
    {
        world.getOpenTile(null, new Coordinate(0, 0), new Coordinate(1, 1));
    }

    @Test
    public void getOpenTileUnbounded()
    {
        Coordinate expected = new Coordinate(-1, -1);
        world = Mockito.spy(world);
        Mockito.doReturn(expected).when(world).getOpenTile(Dice.global, x1, y1, x2, y2);
        Assert.assertSame(expected, world.getOpenTile(Dice.global));
        Mockito.verify(world).getOpenTile(Dice.global, x1, y1, x2, y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOpenTileNullDice()
    {
        world.getOpenTile(null);
    }

    @Test
    public void getOpenTileIntDefaultDice()
    {
        x2 = x2 / 2;
        y2 = y2 / 2;

        Coordinate expected = new Coordinate(-1, -1);
        world = Mockito.spy(world);
        Mockito.doReturn(expected).when(world).getOpenTile(Dice.global, x1, y1, x2, y2);
        Assert.assertSame(expected, world.getOpenTile(x1, y1, x2, y2));
        Mockito.verify(world).getOpenTile(Dice.global, x1, y1, x2, y2);
    }

    @Test
    public void getOpenTileCoordDefaultDice()
    {
        x2 = x2 / 2;
        y2 = y2 / 2;
        Coordinate upLeft = new Coordinate(x1, y1);
        Coordinate downRight = new Coordinate(x2, y2);

        Coordinate expected = new Coordinate(-1, -1);
        world = Mockito.spy(world);
        Mockito.doReturn(expected).when(world).getOpenTile(Dice.global, x1, y1, x2, y2);
        Assert.assertSame(expected, world.getOpenTile(upLeft, downRight));
        Mockito.verify(world).getOpenTile(Dice.global, x1, y1, x2, y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOpenTileDefaultDiceNullLeft()
    {
        world.getOpenTile(null, new Coordinate(1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOpenTileDefaultDiceNullRight()
    {
        world.getOpenTile(new Coordinate(0, 0), null);
    }

    @Test
    public void getOpenTileUpLeftBounds()
    {
        testBounds(new BoundTest()
        {

            @Override
            public void action(int x, int y)
            {
                int x2 = Math.min(x + 1, world.width() - 1);
                int y2 = Math.min(y + 1, world.height() - 1);
                world.getOpenTile(x, y, x2, y2);
            }
        }, world.width(), world.height());
    }

    @Test
    public void getOpenTileDownRightBounds()
    {
        testBounds(new BoundTest()
        {

            @Override
            public void action(int x, int y)
            {
                int x2 = Math.max(x - 1, 0);
                int y2 = Math.max(y - 1, 0);
                world.getOpenTile(x2, y2, x, y);
            }
        }, world.width(), world.height());
    }

    @Test
    public void insideBoundsIntTrue()
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                Assert.assertTrue(world.insideBounds(x, y));
    }

    @Test
    public void insideBoundsCoordTrue()
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                Assert.assertTrue(world.insideBounds(new Coordinate(x, y)));
    }

    @Test
    public void insideBoundsIntFalse()
    {
        for(int x = 0; x < world.width(); x++)
        {
            Assert.assertFalse(world.insideBounds(x, -1));
            Assert.assertFalse(world.insideBounds(x, world.height()));
        }
        for(int y = 0; y < world.height(); y++)
        {
            Assert.assertFalse(world.insideBounds(-1, y));
            Assert.assertFalse(world.insideBounds(world.width(), y));
        }
    }

    @Test
    public void insideBoundsCoordFalse()
    {
        for(int x = 0; x < world.width(); x++)
        {
            Assert.assertFalse(world.insideBounds(new Coordinate(x, -1)));
            Assert.assertFalse(world.insideBounds(new Coordinate(x, world.height())));
        }
        for(int y = 0; y < world.height(); y++)
        {
            Assert.assertFalse(world.insideBounds(new Coordinate(-1, y)));
            Assert.assertFalse(world.insideBounds(new Coordinate(world.width(), y)));
        }
    }

    @Test
    public void drawOrderNotNull()
    {
        Assert.assertNotNull(world.getDrawOrder());
    }

    @Test
    public void drawOrderSame()
    {
        Assert.assertSame(world.getDrawOrder(), world.getDrawOrder());
    }

    @Test
    public void defaultDrawOrder()
    {
        Assert.assertEquals(1, world.getDrawOrder().size());
        Assert.assertEquals(Actor.class, world.getDrawOrder().get(0));
    }

    @Test
    public void drawOrderMutable()
    {
        world.getDrawOrder().clear();
        Assert.assertTrue(world.getDrawOrder().isEmpty());
        world.getDrawOrder().add(ActorA.class);
        world.getDrawOrder().add(ActorB.class);
        Assert.assertEquals(2, world.getDrawOrder().size());
        Assert.assertEquals(ActorA.class, world.getDrawOrder().get(0));
        Assert.assertEquals(ActorB.class, world.getDrawOrder().get(1));
    }

    @Test
    public void actOrderNotNull()
    {
        Assert.assertNotNull(world.getActOrder());
    }

    @Test
    public void actOrderSame()
    {
        Assert.assertSame(world.getActOrder(), world.getActOrder());
    }

    @Test
    public void defaultActOrder()
    {
        Assert.assertEquals(1, world.getActOrder().size());
        Assert.assertEquals(Actor.class, world.getActOrder().get(0));
    }

    @Test
    public void actOrderMutable()
    {
        world.getActOrder().clear();
        Assert.assertTrue(world.getActOrder().isEmpty());
        world.getActOrder().add(ActorA.class);
        world.getActOrder().add(ActorB.class);
        Assert.assertEquals(2, world.getActOrder().size());
        Assert.assertEquals(ActorA.class, world.getActOrder().get(0));
        Assert.assertEquals(ActorB.class, world.getActOrder().get(1));
    }

    @Test
    public void lookAllEmptyTile()
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                List<ColoredChar> look = world.lookAll(x, y);
                Assert.assertEquals(1, look.size());
                Assert.assertEquals(world.tileAt(x, y), look.get(0));
            }
    }

    @Test
    public void lookAllSingleActor()
    {
        ColoredChar face = ColoredChar.create('@');
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                world.addActor(new ConcreteActor(face), x, y);

        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                List<ColoredChar> look = world.lookAll(x, y);
                Assert.assertEquals(2, look.size());
                Assert.assertEquals(face, look.get(0));
                Assert.assertEquals(world.tileAt(x, y), look.get(1));
            }
    }

    @Test
    public void lookAllOrdering()
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                world.addActor(new ActorA(), x, y);
                world.addActor(new ActorB(), x, y);
            }

        world.getDrawOrder().clear();
        world.getDrawOrder().add(ActorA.class);
        world.getDrawOrder().add(ActorB.class);

        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                List<ColoredChar> look = world.lookAll(x, y);
                Assert.assertEquals(3, look.size());
                Assert.assertEquals(world.getActorAt(ActorA.class, x, y).face(), look.get(0));
                Assert.assertEquals(world.getActorAt(ActorB.class, x, y).face(), look.get(1));
                Assert.assertEquals(world.tileAt(x, y), look.get(2));
            }
    }

    @Test
    public void lookAllIgnoresNullFaces()
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                world.addActor(new ConcreteActor(null), x, y);

        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                List<ColoredChar> look = world.lookAll(x, y);
                Assert.assertEquals(1, look.size());
                Assert.assertEquals(world.tileAt(x, y), look.get(0));
            }
    }

    @Test
    public void lookAllBounds()
    {
        testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                world.lookAll(x, y);
            }
        }, world.width(), world.height());
    }

    @Test
    public void lookAllCoord()
    {
        List<ColoredChar> expected = new ArrayList<ColoredChar>();
        Coordinate pos = new Coordinate(3, 5);
        world = Mockito.spy(world);
        Mockito.doReturn(expected).when(world).lookAll(pos.x(), pos.y());
        Assert.assertSame(expected, world.lookAll(pos));
        Mockito.verify(world).lookAll(pos.x(), pos.y());
    }

    @Test(expected = IllegalArgumentException.class)
    public void lookAllNullCoord()
    {
        world.lookAll(null);
    }

    @Test
    public void lookCallsAll()
    {
        List<ColoredChar> expectedAll = new ArrayList<ColoredChar>();
        ColoredChar expectedChar = ColoredChar.create('@');
        expectedAll.add(expectedChar);
        expectedAll.add(ColoredChar.create('D'));

        int x = 3;
        int y = 5;
        world = Mockito.spy(world);
        Mockito.doReturn(expectedAll).when(world).lookAll(x, y);

        Assert.assertSame(expectedChar, world.look(x, y));
        Mockito.verify(world).lookAll(x, y);
    }

    @Test
    public void lookCoordCallsInt()
    {
        List<ColoredChar> expectedAll = new ArrayList<ColoredChar>();
        ColoredChar expectedChar = ColoredChar.create('@');
        expectedAll.add(expectedChar);
        expectedAll.add(ColoredChar.create('D'));

        Coordinate pos = new Coordinate(5, 3);
        world = Mockito.spy(world);
        Mockito.doReturn(expectedAll).when(world).lookAll(pos.x(), pos.y());

        Assert.assertSame(expectedChar, world.look(pos));
        Mockito.verify(world).lookAll(pos.x(), pos.y());
    }

    @Test(expected = IllegalArgumentException.class)
    public void lookNullCoord()
    {
        world.look(null);
    }

    @Test
    public void insideBoundsCoordCallInt()
    {
        World world = Mockito.mock(World.class);
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                Mockito.when(world.insideBounds(x, y)).thenReturn(x % 2 == 0);
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                Assert.assertEquals(x % 2 == 0, world.insideBounds(new Coordinate(x, y)));
                Mockito.verify(world).insideBounds(x, y);
            }
    }
}
