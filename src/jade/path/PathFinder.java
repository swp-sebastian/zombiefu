package jade.path;

import jade.core.World;
import jade.util.datatype.Coordinate;
import jade.util.Guard;
import jade.util.datatype.Direction;
import java.util.List;

/**
 * Represents an algorithm for calculating paths on a {@code World}. These paths
 * can be either partial, meaning the path may not actually reach its goal, or
 * complete, meaning it is either {@code null} or reaches its goal.
 */
public abstract class PathFinder {

    /**
     * Calculates and returns the partial path from the given start to end on
     * the provided {@code
     * World}. Since the path is incomplete, it may or may not terminate at the
     * provided end. Null should never be returned from this method. If a path
     * cannot be found, a partial path, or at least an empty path should be
     * returned.
     *
     * @param world the world on which the path is calculated
     * @param start the start of the calculated path
     * @param end the intended end of the calculated path
     * @return a path from start to end on world
     */
    protected abstract List<Coordinate> calcPath(World world, Coordinate start, Coordinate end);

    /**
     * Returns a partial path from the given start to end on the provided
     * {@code World}. Since the path is incomplete, it may or may not terminate
     * at the provided end.
     *
     * @param world the world on which the path is calculated
     * @param start the start of the calculated path
     * @param end the intended end of the calculated path
     * @return a path from start to end on world
     */
    public final List<Coordinate> getPartialPath(World world, Coordinate start, Coordinate end) {
        Guard.argumentsAreNotNull(world, start, end);
        Guard.argumentsInsideBounds(start.x(), start.y(), world.width(), world.height());

        return Guard.returnNonNull(calcPath(world, start, end));
    }

    /**
     * Returns a partial path from the given start to end on the provided
     * {@code World}. Since the path is incomplete, it may or may not terminate
     * at the provided end.
     *
     * @param world the world on which the path is calculated
     * @param posX the x value of the start
     * @param posY the y value of the start
     * @param endX the x value of the end
     * @param endY the y value of the end
     * @return a path from start to end on world
     */
    public final List<Coordinate> getPartialPath(World world, int posX, int posY, int endX, int endY) {
        return getPartialPath(world, new Coordinate(posX, posY), new Coordinate(endX, endY));
    }

    /**
     * Returns a partial path from the given start to end on the provided
     * {@code World}. Since the path is complete, it will either be null, or
     * terminate at the given end.
     *
     * @param world the world on which the path is calculated
     * @param start the start of the calculated path
     * @param end the intended end of the calculated path
     * @return a path from start to end on world
     */
    public final List<Coordinate> getPath(World world, Coordinate start, Coordinate end) {
        List<Coordinate> path = getPartialPath(world, start, end);
        return path.get(path.size() - 1).equals(end) ? path : null;
    }

    /**
     * Returns a complete path from the given start to end on the provided
     * {@code World}. Since the path is complete, it will either be null, or
     * terminate at the given end.
     *
     * @param world the world on which the path is calculated
     * @param posX the x value of the start
     * @param posY the y value of the start
     * @param endX the x value of the end
     * @param endY the y value of the end
     * @return a path from start to end on world
     */
    public final List<Coordinate> getPath(World world, int posX, int posY, int endX, int endY) {
        return getPath(world, new Coordinate(posX, posY), new Coordinate(endX, endY));
    }

    public final Direction getDirectionOfFirstStep(World world, Coordinate start, Coordinate end) {
        // TODO: Effizienter berechnen
        List<Coordinate> path = getPath(world, start, end);
        if (path != null) {
            return start.directionTo(path.get(0));
        } else {
            return null;
        }
    }
}
