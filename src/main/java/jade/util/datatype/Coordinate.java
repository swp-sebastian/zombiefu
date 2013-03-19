package jade.util.datatype;

import jade.util.Guard;

/**
 * An immutable 2-dimensional integer Cartesian coordinate.
 */
public class Coordinate
{
    /**
     * The x value of the {@code Coordinate}
     */
    protected int x;

    /**
     * The y value of the {@code Coordinate}
     */
    protected int y;

    /**
     * Constructs a new {@code Coordinate} at with the given (x, y) value.
     * @param x the x value
     * @param y the y value
     */
    public Coordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a copy of this {@code Coordinate}.
     * @return a copy of this {@code Coordinate}.
     */
    public Coordinate copy()
    {
        return new Coordinate(x, y);
    }

    /**
     * Returns a mutable copy of the {@code Coordinate}
     * @return a mutable copy of the {@code Coordinate}
     */
    public MutableCoordinate mutableCopy()
    {
        return new MutableCoordinate(x, y);
    }

    /**
     * returns the x value of the {@code Coordinate}
     * @return the x value of the {@code Coordinate}
     */
    public int x()
    {
        return x;
    }

    /**
     * returns the x value of the {@code Coordinate}
     * @return the x value of the {@code Coordinate}
     */
    public int y()
    {
        return y;
    }

    /**
     * Returns the Cartesian distance from this {@code Coordinate} to the provided {@code
     * Coordinate}.
     * @param coord the {@code Coordinate} to which distance is calculated
     * @return the Cartesian distance to the given {@code Coordinate}
     */
    public final double distance(Coordinate coord)
    {
        Guard.argumentIsNotNull(coord);

        return distance(coord.x, coord.y);
    }

    /**
     * Returns the Cartesian distance from this {@code Coordinate} to the provided location.
     * @param x the x value of the location to which distance is calculated
     * @param y the y value of the location to which distance is calculated
     * @return the Cartesian distance to the given (x, y) location
     */
    public double distance(int x, int y)
    {
        double a = this.x - x;
        double b = this.y - y;
        return Math.sqrt(a * a + b * b);
    }

    /**
     * Returns a copy of this {@code Coordinate} translated by the specified (dx, dy).
     * @param dx the change in x
     * @param dy the change in y
     * @return a copy translated by (dx, dy)
     */
    public Coordinate getTranslated(int dx, int dy)
    {
        return new Coordinate(x + dx, y + dy);
    }

    /**
     * Returns a copy of this {@code Coordinate} translated by the specified {@code Coordinate}
     * delta.
     * @param delta the change in x and y
     * @return a copy translated by delta
     */
    public final Coordinate getTranslated(Coordinate delta)
    {
        Guard.argumentIsNotNull(delta);

        return getTranslated(delta.x(), delta.y());
    }

    /**
     * Returns a copy of this {@code Coordinate} translated by the specified {@code Direction}.
     * @param dir the change in x and y
     * @return a copy translated by dir
     */
    public final Coordinate getTranslated(Direction dir)
    {
        Guard.argumentIsNotNull(dir);

        return getTranslated(dir.dx(), dir.dy());
    }

    /**
     * Returns the direction which is one step toward the given {@code Coordinate}.
     * @param goal the {@code Coordinate} towards which the result points
     * @return the direction which is one step toward the given {@code Coordinate}
     */
    public final Direction directionTo(Coordinate goal)
    {
        Guard.argumentIsNotNull(goal);

        return directionTo(goal.x, goal.y);
    }

    /**
     * Returns the direction which is one step toward the given (x, y)
     * @param x the x value of the {@code Coordinate} towards which the result points
     * @param y the y value of the {@code Coordinate} towards which the result points
     * @return the direction which is one step toward the given {@code Coordinate}
     */
    public Direction directionTo(int x, int y)
    {
        int dx = x - this.x;
        int dy = y - this.y;
        if(dx < 0)
        {
            if(dy < 0)
                return Direction.NORTHWEST;
            else if(dy > 0)
                return Direction.SOUTHWEST;
            else
                return Direction.WEST;
        }
        else if(dx > 0)
        {
            if(dy < 0)
                return Direction.NORTHEAST;
            else if(dy > 0)
                return Direction.SOUTHEAST;
            else
                return Direction.EAST;
        }
        else
        {
            if(dy < 0)
                return Direction.NORTH;
            else if(dy > 0)
                return Direction.SOUTH;
            else
                return Direction.ORIGIN;
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Coordinate)
        {
            Coordinate other = (Coordinate)obj;
            return x == other.x && y == other.y;
        }
        else
            return false;
    }

    @Override
    public int hashCode()
    {
        return (x << 16) | (y & 0xFFFF);
    }

    @Override
    public String toString()
    {
        return String.format("(%d, %d)", x, y);
    }
}
