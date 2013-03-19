package test.path;

import jade.core.World;
import jade.path.PathFinder;
import jade.util.datatype.Coordinate;
import jade.util.datatype.MutableCoordinate;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import test.WorldBuilder;

public abstract class PathFinderImplTests
{
    protected PathFinder pathfinder;

    protected abstract PathFinder getInstance();

    @Before
    public void init()
    {
        pathfinder = getInstance();
    }

    @Test
    public void orthogonalS()
    {
        uniformDelta(1, 11, 1, 1);
    }

    @Test
    public void orthogonalW()
    {
        uniformDelta(1, 1, 11, 1);
    }

    @Test
    public void orthogonalE()
    {
        uniformDelta(11, 1, 1, 1);
    }

    @Test
    public void orthogonalN()
    {
        uniformDelta(1, 1, 1, 11);
    }

    @Test
    public void diagonalNW()
    {
        uniformDelta(1, 1, 11, 11);
    }

    @Test
    public void diagonalSE()
    {
        uniformDelta(11, 11, 1, 1);
    }

    @Test
    public void diagonalNE()
    {
        uniformDelta(11, 1, 1, 11);
    }

    @Test
    public void diagonalSW()
    {
        uniformDelta(1, 11, 11, 1);
    }

    private void uniformDelta(int x1, int y1, int x2, int y2)
    {
        World world = WorldBuilder.getEmptyMap();
        Coordinate start = new Coordinate(x1, y1);
        Coordinate goal = new Coordinate(x2, y2);
        int dx = (x1 < x2) ? 1 : (x1 > x2) ? -1 : 0;
        int dy = (y1 < y2) ? 1 : (y1 > y2) ? -1 : 0;
        List<Coordinate> expected = new ArrayList<Coordinate>();
        MutableCoordinate pos = start.mutableCopy();
        while(!pos.equals(goal))
        {
            pos.translate(dx, dy);
            expected.add(pos.copy());
        }
        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, goal));
    }
}
