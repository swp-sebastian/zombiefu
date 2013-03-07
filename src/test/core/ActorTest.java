package test.core;

import jade.core.Actor;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.awt.Color;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ActorTest extends CoreTest
{
    @Test
    public void constructorSetsFace()
    {
        ColoredChar face1 = ColoredChar.create('@');
        actor = new ConcreteActor(face1);
        Assert.assertEquals(face1, actor.face());

        ColoredChar face2 = ColoredChar.create('D', Color.red);
        actor = new ConcreteActor(face2);
        Assert.assertEquals(face2, actor.face());
    }

    @Test(expected = IllegalStateException.class)
    public void unboundPosX()
    {
        actor.x();
    }

    @Test(expected = IllegalStateException.class)
    public void unboundPosY()
    {
        actor.y();
    }

    @Test
    public void setPosMovesActor()
    {
        world.addActor(actor, 5, 5);
        Assert.assertSame(actor, world.getActorAt(Actor.class, 5, 5));

        actor.setPos(4, 3);
        Assert.assertSame(actor, world.getActorAt(Actor.class, 4, 3));
        Assert.assertNull(world.getActorAt(Actor.class, 5, 5));

        actor.setPos(7, 4);
        Assert.assertSame(actor, world.getActorAt(Actor.class, 7, 4));
        Assert.assertNull(world.getActorAt(Actor.class, 4, 3));

        actor.setPos(5, 6);
        Assert.assertSame(actor, world.getActorAt(Actor.class, 5, 6));
        Assert.assertNull(world.getActorAt(Actor.class, 7, 4));
    }

    @Test
    public void setPosChangesXY()
    {
        world.addActor(actor, 5, 5);
        Assert.assertEquals(5, actor.x());
        Assert.assertEquals(5, actor.y());

        actor.setPos(4, 3);
        Assert.assertEquals(4, actor.x());
        Assert.assertEquals(3, actor.y());

        actor.setPos(7, 4);
        Assert.assertEquals(7, actor.x());
        Assert.assertEquals(4, actor.y());

        actor.setPos(5, 6);
        Assert.assertEquals(5, actor.x());
        Assert.assertEquals(6, actor.y());
    }

    @Test
    public void posMirrorsXY()
    {
        world.addActor(actor, 5, 5);
        Assert.assertEquals(new Coordinate(5, 5), actor.pos());

        actor.setPos(4, 3);
        Assert.assertEquals(new Coordinate(4, 3), actor.pos());

        actor.setPos(7, 4);
        Assert.assertEquals(new Coordinate(7, 4), actor.pos());

        actor.setPos(5, 6);
        Assert.assertEquals(new Coordinate(5, 6), actor.pos());
    }

    @Test
    public void posImmutableWithMove()
    {
        world.addActor(actor, 5, 5);
        Coordinate prev = actor.pos();

        actor.setPos(4, 3);
        Coordinate curr = actor.pos();
        Assert.assertNotSame(prev, curr);
        Assert.assertEquals(new Coordinate(5, 5), prev);
        Assert.assertEquals(new Coordinate(4, 3), curr);
    }

    @Test
    public void setPosBounds()
    {
        world.addActor(actor, 5, 5);
        testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                actor.setPos(x, y);
            }
        }, world.width(), world.height());
    }

    @Test(expected = IllegalStateException.class)
    public void setPosUnbound()
    {
        actor.setPos(5, 5);
    }

    @Test
    public void setPosCoord()
    {
        world.addActor(actor, 5, 5);
        actor = Mockito.spy(actor);
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                actor.setPos(new Coordinate(x, y));
                Mockito.verify(actor).setPos(x, y);
            }
    }

    @Test(expected = IllegalArgumentException.class)
    public void setPosNullCoord()
    {
        world.addActor(actor, 5, 5);
        actor.setPos(null);
    }

    @Test
    public void moveIntCallsSetPos()
    {
        world.addActor(actor, 5, 5);
        actor = Mockito.spy(actor);
        actor.move(1, 1);
        Mockito.verify(actor).setPos(6, 6);
        actor.move(2, 0);
        Mockito.verify(actor).setPos(8, 6);
        actor.move(1, -2);
        Mockito.verify(actor).setPos(9, 4);
    }

    @Test
    public void moveIntBounds()
    {
        testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                Actor actor = new ConcreteActor(ColoredChar.create('@'));
                world.addActor(actor, 5, 5);
                actor.move(x - 5, y - 5);
            }
        }, world.width(), world.height());
    }

    @Test
    public void moveCoordCallsSetPos()
    {
        world.addActor(actor, 5, 5);
        actor = Mockito.spy(actor);
        actor.move(new Coordinate(1, 1));
        Mockito.verify(actor).setPos(6, 6);
        actor.move(new Coordinate(2, 0));
        Mockito.verify(actor).setPos(8, 6);
        actor.move(new Coordinate(1, -2));
        Mockito.verify(actor).setPos(9, 4);
    }

    @Test
    public void moveCoordBounds()
    {
        testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                Actor actor = new ConcreteActor(ColoredChar.create('@'));
                world.addActor(actor, 5, 5);
                actor.move(new Coordinate(x - 5, y - 5));
            }
        }, world.width(), world.height());
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveNullCoord()
    {
        world.addActor(actor, 5, 5);
        actor.move((Coordinate)null);
    }

    @Test
    public void moveDirCallsSetPos()
    {
        world.addActor(actor, 5, 5);
        actor = Mockito.spy(actor);
        actor.move(Direction.NORTH);
        Mockito.verify(actor).setPos(5, 4);
        actor.move(Direction.SOUTHEAST);
        Mockito.verify(actor).setPos(6, 5);
        actor.move(Direction.SOUTH);
        Mockito.verify(actor).setPos(6, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveNullDir()
    {
        world.addActor(actor, 5, 5);
        actor.move((Direction)null);
    }

    @Test
    public void expiredDefault()
    {
        Assert.assertFalse(actor.expired());
    }

    @Test
    public void expireExpires()
    {
        actor.expire();
        Assert.assertTrue(actor.expired());
    }

    @Test
    public void expirePropagates()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        actor.attach(holder);
        holder.expire();
        Assert.assertTrue(actor.expired());
    }

    @Test
    public void attachSetsHolder()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        actor.attach(holder);
        Assert.assertSame(holder, actor.holder());
        Assert.assertTrue(actor.held(holder));
        Assert.assertTrue(actor.held());
    }

    @Test(expected = IllegalArgumentException.class)
    public void attachNullHolder()
    {
        actor.attach(null);
    }

    @Test
    public void attachSetsWorld()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(holder, 5, 5);
        actor.attach(holder);
        Assert.assertSame(world, actor.world());
        Assert.assertTrue(world.getActors(Actor.class).contains(actor));
    }

    @Test
    public void attachPropagatesWorld()
    {
        Actor held = new ConcreteActor(ColoredChar.create('*'));
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(holder, 4, 4);
        held.attach(actor);
        actor.attach(holder);

        Assert.assertEquals(3, world.getActors(Actor.class).size());
        Assert.assertTrue(world.getActors(Actor.class).contains(held));
        Assert.assertTrue(world.getActors(Actor.class).contains(actor));
        Assert.assertTrue(world.getActors(Actor.class).contains(holder));
        Assert.assertSame(world, held.world());
        Assert.assertSame(world, actor.world());
        Assert.assertSame(world, holder.world());
    }

    @Test(expected = IllegalStateException.class)
    public void attachDisablesSetPos()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(holder, 5, 5);
        actor.attach(holder);
        actor.setPos(5, 5);
    }

    @Test
    public void attachSetsPos()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(holder, 3, 4);
        actor.attach(holder);
        Assert.assertEquals(3, actor.x());
        Assert.assertEquals(4, actor.y());
        holder.setPos(5, 3);
        Assert.assertEquals(5, actor.x());
        Assert.assertEquals(3, actor.y());
        holder.setPos(6, 2);
        Assert.assertEquals(6, actor.x());
        Assert.assertEquals(2, actor.y());
        holder.setPos(7, 6);
        Assert.assertEquals(7, actor.x());
        Assert.assertEquals(6, actor.y());
    }

    @Test
    public void attachPosPropogates()
    {
        Actor held = new ConcreteActor(ColoredChar.create('*'));
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(holder, 3, 4);
        held.attach(actor);
        actor.attach(holder);
        Assert.assertEquals(3, held.x());
        Assert.assertEquals(4, held.y());
        holder.setPos(5, 3);
        Assert.assertEquals(5, held.x());
        Assert.assertEquals(3, held.y());
        holder.setPos(6, 2);
        Assert.assertEquals(6, held.x());
        Assert.assertEquals(2, held.y());
        holder.setPos(7, 6);
        Assert.assertEquals(7, held.x());
        Assert.assertEquals(6, held.y());
    }

    @Test
    public void attachDoesNotPlace()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(holder, 3, 6);
        actor.attach(holder);

        Assert.assertFalse(world.getActorsAt(Actor.class, 3, 6).contains(actor));
        Assert.assertTrue(world.getActors(Actor.class).contains(actor));
    }

    @Test(expected = IllegalStateException.class)
    public void attachDisablesAttach()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        Actor wannabe = new ConcreteActor(ColoredChar.create('.'));
        actor.attach(holder);
        actor.attach(wannabe);
    }

    @Test(expected = IllegalStateException.class)
    public void attachBound()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(actor, 5, 5);
        actor.attach(holder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void attachSelf()
    {
        actor.attach(actor);
    }

    @Test(expected = IllegalStateException.class)
    public void attachDisablesAddActor()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        actor.attach(holder);
        world.addActor(actor, 5, 5);
    }

    @Test(expected = IllegalStateException.class)
    public void attachDisablesRemoveActor()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(holder, 5, 5);
        actor.attach(holder);
        world.removeActor(actor);
    }
    
    @Test
    public void holdsOne()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        actor.attach(holder);

        Assert.assertEquals(1, holder.holds(Actor.class).size());
        Assert.assertTrue(holder.holds(Actor.class).contains(actor));
    }

    @Test
    public void holdsMulti()
    {
        Actor held1 = new ConcreteActor(ColoredChar.create('*'));
        Actor held2 = new ConcreteActor(ColoredChar.create('|'));
        held1.attach(actor);
        held2.attach(actor);

        Assert.assertEquals(2, actor.holds(Actor.class).size());
        Assert.assertTrue(actor.holds(Actor.class).contains(held1));
        Assert.assertTrue(actor.holds(Actor.class).contains(held2));
    }

    @Test
    public void holdsFilters()
    {
        Actor heldA = new ActorA();
        Actor heldB = new ActorB();
        heldA.attach(actor);
        heldB.attach(actor);

        Assert.assertEquals(2, actor.holds(Actor.class).size());
        Assert.assertTrue(actor.holds(Actor.class).contains(heldA));
        Assert.assertTrue(actor.holds(Actor.class).contains(heldB));

        Assert.assertEquals(1, actor.holds(ActorA.class).size());
        Assert.assertTrue(actor.holds(ActorA.class).contains(heldA));
        Assert.assertFalse(actor.holds(ActorA.class).contains(heldB));

        Assert.assertEquals(1, actor.holds(ActorB.class).size());
        Assert.assertFalse(actor.holds(ActorB.class).contains(heldA));
        Assert.assertTrue(actor.holds(ActorB.class).contains(heldB));
    }

    @Test(expected = IllegalArgumentException.class)
    public void holdsNullClass()
    {
        actor.holds(null);
    }

    @Test
    public void holdsDefaultClass()
    {
        actor = Mockito.spy(actor);

        Actor heldA = new ActorA();
        Actor heldB = new ActorB();
        heldA.attach(actor);
        heldB.attach(actor);

        Collection<Actor> results = actor.holds();
        Assert.assertEquals(2, results.size());
        Assert.assertTrue(results.contains(heldA));
        Assert.assertTrue(results.contains(heldB));

        Mockito.verify(actor).holds(Actor.class);
    }

    @Test
    public void detachUnsetsHolder()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        actor.attach(holder);
        actor.detach();

        Assert.assertNull(actor.holder());
        Assert.assertFalse(actor.held(holder));
        Assert.assertFalse(actor.held());
    }

    @Test
    public void detachReleaseActor()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        actor.attach(holder);
        actor.detach();

        Assert.assertFalse(holder.holds(Actor.class).contains(actor));
    }

    @Test(expected = IllegalStateException.class)
    public void detachNonHeld()
    {
        actor.detach();
    }

    @Test
    public void detachPlaces()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(holder, 3, 5);
        actor.attach(holder);
        actor.detach();

        Assert.assertEquals(3, actor.x());
        Assert.assertEquals(5, actor.y());

        Assert.assertEquals(2, world.getActorsAt(Actor.class, 3, 5).size());
        Assert.assertTrue(world.getActorsAt(Actor.class, 3, 5).contains(holder));
        Assert.assertTrue(world.getActorsAt(Actor.class, 3, 5).contains(actor));

        Assert.assertEquals(2, world.getActors(Actor.class).size());
        Assert.assertTrue(world.getActors(Actor.class).contains(holder));
        Assert.assertTrue(world.getActors(Actor.class).contains(actor));

        Assert.assertNull(actor.holder());
        Assert.assertFalse(holder.holds(Actor.class).contains(actor));
    }

    @Test
    public void detachPosIndependent()
    {
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(holder, 3, 5);
        actor.attach(holder);
        actor.detach();

        actor.setPos(6, 3);
        Assert.assertTrue(world.getActorsAt(Actor.class, 6, 3).contains(actor));
        Assert.assertFalse(world.getActorsAt(Actor.class, 6, 3).contains(holder));
        Assert.assertFalse(world.getActorsAt(Actor.class, 3, 5).contains(actor));
        Assert.assertTrue(world.getActorsAt(Actor.class, 3, 5).contains(holder));
        Assert.assertEquals(6, actor.x());
        Assert.assertEquals(3, actor.y());
        Assert.assertEquals(3, holder.x());
        Assert.assertEquals(5, holder.y());
    }

    @Test
    public void detachPosPropgates()
    {
        Actor held = new ConcreteActor(ColoredChar.create('*'));
        Actor holder = new ConcreteActor(ColoredChar.create('D'));
        world.addActor(holder, 3, 5);
        held.attach(actor);
        actor.attach(holder);
        actor.detach();

        actor.setPos(6, 3);
        Assert.assertTrue(world.getActorsAt(Actor.class, 6, 3).contains(actor));
        Assert.assertFalse(world.getActorsAt(Actor.class, 6, 3).contains(held));
        Assert.assertFalse(world.getActorsAt(Actor.class, 3, 5).contains(actor));
        Assert.assertFalse(world.getActorsAt(Actor.class, 3, 5).contains(held));
        Assert.assertEquals(6, actor.x());
        Assert.assertEquals(3, actor.y());
        Assert.assertEquals(6, held.x());
        Assert.assertEquals(3, held.y());
    }
}
