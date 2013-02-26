package test.path;

import java.util.ArrayList;
import java.util.List;
import jade.core.World;
import jade.path.AStar;
import jade.path.PathFinder;
import jade.util.datatype.Coordinate;
import junit.framework.Assert;
import org.junit.Test;
import test.WorldBuilder;

public class AStarTest extends PathFinderImplTests
{
    @Override
    protected PathFinder getInstance()
    {
        return new AStar();
    }

    @Test
    public void aroundWall()
    {
        World world = WorldBuilder.getWallNS();
        Coordinate start = new Coordinate(5, 1);
        Coordinate end = new Coordinate(7, 1);
        List<Coordinate> expected = new ArrayList<Coordinate>();
        for(int y = 2; y <= 6; y++)
            expected.add(new Coordinate(5, y));
        expected.add(new Coordinate(6, 7));
        for(int y = 6; y >= 1; y--)
            expected.add(new Coordinate(7, y));
        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void nonGreedyOptimal()
    {
        World world = WorldBuilder.getMaze();
        Coordinate start = new Coordinate(6, 6);
        Coordinate end = new Coordinate(1, 1);
        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(6, 7));
        expected.add(new Coordinate(7, 8));
        expected.add(new Coordinate(8, 7));
        expected.add(new Coordinate(8, 6));
        expected.add(new Coordinate(8, 5));
        expected.add(new Coordinate(8, 4));
        expected.add(new Coordinate(8, 3));
        expected.add(new Coordinate(8, 2));
        expected.add(new Coordinate(7, 1));
        expected.add(new Coordinate(6, 1));
        expected.add(new Coordinate(5, 1));
        expected.add(new Coordinate(4, 1));
        expected.add(new Coordinate(3, 1));
        expected.add(new Coordinate(2, 1));
        expected.add(new Coordinate(1, 1));
        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void avoidDeadEnd()
    {
        World world = WorldBuilder.getDeadEnd();
        Coordinate start = new Coordinate(4, 1);
        Coordinate end = new Coordinate(4, 8);
        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(4, 2));
        expected.add(new Coordinate(4, 3));
        expected.add(new Coordinate(5, 4));
        expected.add(new Coordinate(6, 4));
        expected.add(new Coordinate(7, 4));
        expected.add(new Coordinate(8, 5));
        expected.add(new Coordinate(8, 6));
        expected.add(new Coordinate(7, 7));
        expected.add(new Coordinate(6, 8));
        expected.add(new Coordinate(5, 8));
        expected.add(new Coordinate(4, 8));
        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }

    @Test
    public void partialPathReturns()
    {
        World world = WorldBuilder.getBlocked();
        Coordinate start = new Coordinate(4, 1);
        Coordinate end = new Coordinate(4, 8);
        List<Coordinate> expected = new ArrayList<Coordinate>();
        expected.add(new Coordinate(4, 2));
        expected.add(new Coordinate(4, 3));
        expected.add(new Coordinate(3, 4));
        expected.add(new Coordinate(2, 5));
        expected.add(new Coordinate(2, 6));
        expected.add(new Coordinate(2, 7));
        Assert.assertEquals(expected, pathfinder.getPartialPath(world, start, end));
    }
}
