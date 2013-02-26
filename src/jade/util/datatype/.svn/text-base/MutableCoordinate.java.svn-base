package jade.util.datatype;

import jade.util.Guard;

/**
 * A mutable version of {@code Coordinate}. One potential use is allowing multiple references to the
 * same {@code MutableCoordinate} to all be simultaneously updated.
 */
public class MutableCoordinate extends Coordinate
{
    /**
     * Creates a new instance of {@code MutableCoordinate} with the given (x, y) value.
     * @param x the x value
     * @param y the y value
     */
    public MutableCoordinate(int x, int y)
    {
        super(x, y);
    }

    /**
     * Sets the (x, y) value of the {@code Coordinate}
     * @param x the new x value of the {@code Coordinate}
     * @param y the new y value of the {@code Coordinate}
     */
    public void setXY(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the (x, y) value of the {@code Coordinate}
     * @param coord the new value of the {@code Coordinate}
     */
    public final void setXY(Coordinate coord)
    {
        Guard.argumentIsNotNull(coord);

        setXY(coord.x, coord.y);
    }

    /**
     * Changes the (x, y) value of the {@code Coordinate} by the specified amounts.
     * @param dx
     * @param dy
     */
    public void translate(int dx, int dy)
    {
        setXY(x + dx, y + dy);
    }

    /**
     * Changes the (x, y) value of the {@code Coordinate} by the specified amount.
     * @param delta the change in (x, y)
     */
    public final void translate(Coordinate delta)
    {
        Guard.argumentIsNotNull(delta);

        setXY(x + delta.x, y + delta.y);
    }

    /**
     * Changes the (x, y) value of the {@code Coordinate} one unit in specified direction.
     * @param direction the direction in which to change (x, y)
     */
    public final void translate(Direction direction)
    {
        Guard.argumentIsNotNull(direction);

        setXY(x + direction.dx(), y + direction.dy());
    }
}
