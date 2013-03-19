package jade.fov;

import jade.core.World;
import jade.util.Guard;
import jade.util.datatype.Coordinate;
import java.util.Collection;

/**
 * Represents an algorithm for determining field of view on a {@code World}.
 */
public abstract class ViewField
{
    /**
     * Calculates the field of view on the given {@code World} from the given (x, y) coordinates
     * with the specified radius.
     * @param world the {@code World} on which field of view is being calculated
     * @param x the x value of the coordinate being queried
     * @param y the y value of the coordinate being queried
     * @param r the radius of the field of view
     * @return the field of view on world from (x, y)
     */
    protected abstract Collection<Coordinate> calcViewField(World world, int x, int y, int r);

    /**
     * Calculates the field of view on the given {@code World} from the given (x, y) coordinates
     * with the specified radius.
     * @param world the {@code World} on which field of view is being calculated
     * @param x the x value of the coordinate being queried
     * @param y the y value of the coordinate being queried
     * @param r the radius of the field of view
     * @return the field of view on world from (x, y)
     */
    public final Collection<Coordinate> getViewField(World world, int x, int y, int r)
    {
        Guard.argumentIsNotNull(world);
        Guard.argumentsInsideBounds(x, y, world.width(), world.height());
        Guard.argumentIsNonNegative(r);

        return calcViewField(world, x, y, r);
    }

    /**
     * Calculates the field of view on the given {@code World} from the given {@code Coordinate}
     * with the specified radius.
     * @param world the {@code World} on which field of view is being calculated
     * @param coord the value of the coordinate being queried
     * @param r the radius of the field of view
     * @return the field of view on world from the given {@code Coordinate}
     */
    public final Collection<Coordinate> getViewField(World world, Coordinate coord, int r)
    {
        Guard.argumentIsNotNull(coord);

        return getViewField(world, coord.x(), coord.y(), r);
    }
}
