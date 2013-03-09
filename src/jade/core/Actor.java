package jade.core;

import jade.util.Guard;
import jade.util.Lambda;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import jade.util.datatype.MutableCoordinate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents anything on a Jade {@code World} that can perform an action (or be acted upon). This
 * obviously includes things like the player, NPCs, and monsters. Not as obviously, an {@code Actor}
 * can represent things like items, spells, doors and traps, or even abstract actions like timers
 * and events. An {@code Actor} can be attached to one another in order to create other interesting
 * effects. For example, an enchantment spell might be an effect {@code Actor} attached to an item
 * {@code Actor}. A timer {@code Actor} could be attached to the spell which simple counts an
 * appropriate amount of turns, then expires the spell {@code Actor}.
 */
public abstract class Actor extends Messenger
{
    private ColoredChar face;
    private World world;
    private MutableCoordinate pos;
    private boolean expired;
    private Actor holder;
    private Set<Actor> holds;

    /**
     * Constructs a new {@code Actor} with the given face.
     * @param face the face of the {@code Actor}
     */
    public Actor(ColoredChar face)
    {
        this.face = face;
        pos = new MutableCoordinate(0, 0);
        expired = false;
        holds = new HashSet<Actor>();
    }

    /**
     * Performs the actions of this {@code Actor}. Normally this would be called by the
     * {@code World} in the {@code tick()} method, which allows the {@code Actor} actions to be
     * scheduled properly based on act order and {@code Actor} speed.
     */
    public abstract void act();

    /**
     * Returns the face of the {@code Actor}.
     * @return the face of the {@code Actor}
     */
    public ColoredChar face()
    {
        return face;
    }

    /**
     * Returns the {@code World} the {@code Actor} is currently bound to, or null if the
     * {@code Actor} is unbound.
     * @return the {@code World} the {@code Actor} is currently bound to
     */
    public World world()
    {
        return world;
    }

    /**
     * Returns true if the {@code Actor} bound to a {@code World}. This is equivalent to calling
     * {@code actor.world() != null}.
     * @return true if the {@code Actor} bound to a {@code World}
     */
    public final boolean bound()
    {
        return world != null;
    }

    /**
     * Returns true if the {@code Actor} is bound to the given {@code World}. This is equivalent to
     * calling {@code actor.world() == world}.
     * @param world the {@code World} being tested
     * @return true if the {@code Actor} is bound to world
     */
    public final boolean bound(World world)
    {
        return this.world == world;
    }

    /**
     * Sets the position of the {@code Actor} on its current {@code World}. The {@code Actor} must
     * be bound and not held when this method is called.
     * @param x the new x position of the {@code Actor}
     * @param y the new y position of the {@code Actor}
     */
    public void setPos(int x, int y)
    {
        Guard.verifyState(bound());
        Guard.verifyState(!held());
        Guard.argumentsInsideBounds(x, y, world.width(), world.height());

        world.removeFromGrid(this);
        setXY(x, y);
        world.addToGrid(this);
    }

    /**
     * Sets the position of the {@code Actor} on its current {@code World}. The {@code Actor} must
     * be bound and not held when this method is called.
     * @param coord the new position of the {@code Actor}
     */
    public final void setPos(Coordinate coord)
    {
        Guard.argumentIsNotNull(coord);

        setPos(coord.x(), coord.y());
    }

    /**
     * Moves the {@code Actor} by the specified amounts
     * @param dx the change x
     * @param dy the change y
     */
    public final void move(int dx, int dy)
    {
        setPos(x() + dx, y() + dy);
    }

    /**
     * Moves the {@code Actor} by the specified amount
     * @param delta the change in x and y
     */
    public final void move(Coordinate delta)
    {
        Guard.argumentIsNotNull(delta);

        setPos(x() + delta.x(), y() + delta.y());
    }

    /**
     * Moves the {@code Actor} one tile in the specified direction
     * @param dir the change in x and y
     */
    public final void move(Direction dir)
    {
        Guard.argumentIsNotNull(dir);

        setPos(x() + dir.dx(), y() + dir.dy());
    }

    /**
     * Returns the x location of the {@code Actor}.
     * @return the x location of the {@code Actor}
     */
    public int x()
    {
        Guard.verifyState(bound());

        return pos.x();
    }

    /**
     * Returns the y location of the {@code Actor}.
     * @return the y location of the {@code Actor}
     */
    public int y()
    {
        Guard.verifyState(bound());

        return pos.y();
    }

    /**
     * Returns a copy of the current position of the {@code Actor}.
     * @return a copy of the current position of the {@code Actor}.
     */
    public final Coordinate pos()
    {
        return pos.copy();
    }

    /**
     * Returns true if the {@code Actor} is expired, or in other words, marked for removal.
     * @return true if the {@code Actor} is expired
     */
    public boolean expired()
    {
        return expired;
    }

    /**
     * Expires the {@code Actor}, or in other words, marks it for removal.
     */
    public void expire()
    {
        expired = true;
        for(Actor held : holds)
            held.expire();
    }

    /**
     * Attaches this {@code Actor} to another. Note that the {@code Actor} must not be currently
     * held or bound to a {@code World} when calling this method. The position of this {@code Actor}
     * will follow the holder, and the {@code Actor} will be bound to the holder's {@code World}.
     * However, the {@code Actor} will not be placed on the {@code World} grid and will therefore
     * not be accessible through {@code world.getActorAt()} or {@code world.getActorsAt()}.
     * @param holder the new holder of the {@code Actor}
     */
    public void attach(Actor holder)
    {
        Guard.verifyState(!held());
        Guard.verifyState(!bound());
        Guard.argumentIsNotNull(holder);
        Guard.validateArgument(holder != this);

        this.holder = holder;
        propagatePos(holder.pos);
        if(holder.bound())
        {
            setWorld(holder.world);
            world.registerActor(this);
        }
        holder.holds.add(this);
    }

    /**
     * Detaches this {@code Actor} from its current holder. Note that the {@code Actor} must
     * currently be held when calling this method. If the {@code Actor} is bound, it will be placed
     * on the {@code World} at the current position of the holder.
     */
    public void detach()
    {
        Guard.verifyState(held());

        if(bound())
            world.addToGrid(this);
        propagatePos(holder.pos.mutableCopy());
        holder.holds.remove(this);
        holder = null;
    }

    /**
     * Returns the current holder of the {@code Actor}, or null if it is not held.
     * @return the current holder of the {@code Actor}
     */
    public Actor holder()
    {
        return holder;
    }

    /**
     * Returns true if the {@code Actor} is currently held. This is equivalent to
     * {@code actor.holder() != null}.
     * @return true if the {@code Actor} is currently held
     */
    public final boolean held()
    {
        return holder != null;
    }

    /**
     * Returns true if the {@code Actor} is held by the specified {@code Actor}. This is equivalent
     * to {@code actor.holder() == holder}.
     * @param holder the holder being tested
     * @return true if the {@code Actor} is held by the specified holder
     */
    public final boolean held(Actor holder)
    {
        return this.holder == holder;
    }

    /**
     * Returns all the {@code Actor} the specified class currently being held by this one.
     * @param <T> the generic type of the class being queried
     * @param cls the class being queried
     * @return all the {@code Actor} currently being held by this one
     */
    public <T extends Actor> Collection<T> holds(Class<T> cls)
    {
        return Lambda.toSet(Lambda.filterType(holds, cls));
    }

    /**
     * Returns all the {@code Actor} currently being held by this one.
     * @return all the {@code Actor} currently being held by this one
     */
    public final Collection<Actor> holds()
    {
        return holds(Actor.class);
    }

    void setWorld(World world)
    {
        this.world = world;
        for(Actor held : holds)
            held.setWorld(world);
    }

    void setXY(int x, int y)
    {
        pos.setXY(x, y);
    }

    private void propagatePos(MutableCoordinate pos)
    {
        this.pos = pos;
        for(Actor held : holds)
            held.propagatePos(pos);
    }
}
