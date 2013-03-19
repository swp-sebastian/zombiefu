package test.path;

import jade.core.World;
import jade.path.Bresenham;
import jade.path.PathFinder;
import jade.util.datatype.Coordinate;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import test.WorldBuilder;
import test.core.CoreTest;
import test.core.CoreTest.BoundTest;

public class BresenhamTest extends PathFinderImplTests
{
    @Override
    protected PathFinder getInstance()
    {
        return new Bresenham();
    }

    @Test
    public void slantNW()
    {
        World world = WorldBuilder.getEmptyMap();
        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(11, 5);

        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(2, 1));
        expected.add(new Coordinate(3, 2));
        expected.add(new Coordinate(4, 2));
        expected.add(new Coordinate(5, 3));
        expected.add(new Coordinate(6, 3));
        expected.add(new Coordinate(7, 3));
        expected.add(new Coordinate(8, 4));
        expected.add(new Coordinate(9, 4));
        expected.add(new Coordinate(10, 5));
        expected.add(new Coordinate(11, 5));

        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void slantSE()
    {
        World world = WorldBuilder.getEmptyMap();
        Coordinate start = new Coordinate(11, 5);
        Coordinate end = new Coordinate(1, 1);

        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(10, 5));
        expected.add(new Coordinate(9, 4));
        expected.add(new Coordinate(8, 4));
        expected.add(new Coordinate(7, 3));
        expected.add(new Coordinate(6, 3));
        expected.add(new Coordinate(5, 3));
        expected.add(new Coordinate(4, 2));
        expected.add(new Coordinate(3, 2));
        expected.add(new Coordinate(2, 1));
        expected.add(new Coordinate(1, 1));

        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void slantNE()
    {
        World world = WorldBuilder.getEmptyMap();
        Coordinate start = new Coordinate(11, 1);
        Coordinate end = new Coordinate(1, 5);

        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(10, 1));
        expected.add(new Coordinate(9, 2));
        expected.add(new Coordinate(8, 2));
        expected.add(new Coordinate(7, 3));
        expected.add(new Coordinate(6, 3));
        expected.add(new Coordinate(5, 3));
        expected.add(new Coordinate(4, 4));
        expected.add(new Coordinate(3, 4));
        expected.add(new Coordinate(2, 5));
        expected.add(new Coordinate(1, 5));

        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void slantSW()
    {
        World world = WorldBuilder.getEmptyMap();
        Coordinate start = new Coordinate(1, 5);
        Coordinate end = new Coordinate(11, 1);

        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(2, 5));
        expected.add(new Coordinate(3, 4));
        expected.add(new Coordinate(4, 4));
        expected.add(new Coordinate(5, 3));
        expected.add(new Coordinate(6, 3));
        expected.add(new Coordinate(7, 3));
        expected.add(new Coordinate(8, 2));
        expected.add(new Coordinate(9, 2));
        expected.add(new Coordinate(10, 1));
        expected.add(new Coordinate(11, 1));

        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void steepNW()
    {
        World world = WorldBuilder.getEmptyMap();
        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(5, 11);

        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(1, 2));
        expected.add(new Coordinate(2, 3));
        expected.add(new Coordinate(2, 4));
        expected.add(new Coordinate(3, 5));
        expected.add(new Coordinate(3, 6));
        expected.add(new Coordinate(3, 7));
        expected.add(new Coordinate(4, 8));
        expected.add(new Coordinate(4, 9));
        expected.add(new Coordinate(5, 10));
        expected.add(new Coordinate(5, 11));

        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void steepSE()
    {
        World world = WorldBuilder.getEmptyMap();
        Coordinate start = new Coordinate(5, 11);
        Coordinate end = new Coordinate(1, 1);

        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(5, 10));
        expected.add(new Coordinate(4, 9));
        expected.add(new Coordinate(4, 8));
        expected.add(new Coordinate(3, 7));
        expected.add(new Coordinate(3, 6));
        expected.add(new Coordinate(3, 5));
        expected.add(new Coordinate(2, 4));
        expected.add(new Coordinate(2, 3));
        expected.add(new Coordinate(1, 2));
        expected.add(new Coordinate(1, 1));

        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void steepNE()
    {
        World world = WorldBuilder.getEmptyMap();
        Coordinate start = new Coordinate(11, 1);
        Coordinate end = new Coordinate(7, 11);

        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(11, 2));
        expected.add(new Coordinate(10, 3));
        expected.add(new Coordinate(10, 4));
        expected.add(new Coordinate(9, 5));
        expected.add(new Coordinate(9, 6));
        expected.add(new Coordinate(9, 7));
        expected.add(new Coordinate(8, 8));
        expected.add(new Coordinate(8, 9));
        expected.add(new Coordinate(7, 10));
        expected.add(new Coordinate(7, 11));

        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void steepSW()
    {
        World world = WorldBuilder.getEmptyMap();
        Coordinate start = new Coordinate(7, 11);
        Coordinate end = new Coordinate(11, 1);

        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(7, 10));
        expected.add(new Coordinate(8, 9));
        expected.add(new Coordinate(8, 8));
        expected.add(new Coordinate(9, 7));
        expected.add(new Coordinate(9, 6));
        expected.add(new Coordinate(9, 5));
        expected.add(new Coordinate(10, 4));
        expected.add(new Coordinate(10, 3));
        expected.add(new Coordinate(11, 2));
        expected.add(new Coordinate(11, 1));

        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void blockedSlant()
    {
        World world = WorldBuilder.getWallNS();
        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(11, 5);

        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(2, 1));
        expected.add(new Coordinate(3, 2));
        expected.add(new Coordinate(4, 2));
        expected.add(new Coordinate(5, 3));
        expected.add(new Coordinate(6, 3));

        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void blockedSteep()
    {
        World world = WorldBuilder.getWallWE();
        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(5, 11);

        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(1, 2));
        expected.add(new Coordinate(2, 3));
        expected.add(new Coordinate(2, 4));
        expected.add(new Coordinate(3, 5));
        expected.add(new Coordinate(3, 6));

        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPartialNullWorld()
    {
        pathfinder.getPartialPath(null, new Coordinate(1, 1), new Coordinate(5, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPartialNullStart()
    {
        World world = WorldBuilder.getEmptyMap();
        pathfinder.getPartialPath(world, null, new Coordinate(5, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPartialNullEnd()
    {
        World world = WorldBuilder.getEmptyMap();
        pathfinder.getPartialPath(world, new Coordinate(5, 5), null);
    }

    @Test
    public void getPartialStartBounds()
    {
        final World world = WorldBuilder.getEmptyMap();
        final Coordinate end = new Coordinate(5, 5);
        CoreTest.testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                pathfinder.getPartialPath(world, new Coordinate(x, y), end);
            }
        }, world.width(), world.height());
    }

}
