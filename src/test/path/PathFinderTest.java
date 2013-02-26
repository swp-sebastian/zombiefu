package test.path;

import jade.core.World;
import jade.path.PathFinder;
import jade.util.datatype.Coordinate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import test.core.CoreTest;
import test.core.CoreTest.BoundTest;

public class PathFinderTest
{
    private World world;
    private Coordinate start;
    private Coordinate end;
    private List<Coordinate> partial;
    private List<Coordinate> full;

    @Before
    public void initMock()
    {
        world = new World(10, 10)
        {};
        start = new Coordinate(4, 4);
        end = new Coordinate(6, 6);
        partial = new ArrayList<Coordinate>();
        partial.add(new Coordinate(5, 5));
        full = new ArrayList<Coordinate>();
        full.add(new Coordinate(5, 5));
        full.add(new Coordinate(6, 6));
    }

    @Test
    public void getPartialIntCallsCalc()
    {
        int sx = start.x();
        int sy = start.y();
        int ex = end.x();
        int ey = end.y();

        EchoPathFinder path = Mockito.spy(new EchoPathFinder(partial));

        List<Coordinate> actual = path.getPartialPath(world, sx, sy, ex, ey);
        Assert.assertSame(partial, actual);
        Mockito.verify(path).calcPath(world, start, end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPartialIntNullWorld()
    {
        PathFinder path = new EchoPathFinder(partial);
        path.getPartialPath(null, start.x(), start.y(), end.y(), end.y());
    }

    @Test
    public void getPartialIntStartBounds()
    {
        CoreTest.testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                PathFinder path = new EchoPathFinder(partial);
                path.getPartialPath(world, x, y, end.x(), end.y());
            }
        }, world.width(), world.height());
    }

    @Test
    public void getPathCoordIncomplete()
    {
        EchoPathFinder path = Mockito.spy(new EchoPathFinder(partial));

        List<Coordinate> actual = path.getPath(world, start, end);
        Assert.assertNull(actual);
        Mockito.verify(path).calcPath(world, start, end);
    }

    @Test
    public void getPathCoordComplete()
    {
        EchoPathFinder path = Mockito.spy(new EchoPathFinder(full));

        List<Coordinate> actual = path.getPath(world, start, end);
        Assert.assertSame(full, actual);
        Mockito.verify(path).calcPath(world, start, end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathCoordNullWorld()
    {
        PathFinder path = new EchoPathFinder(full);
        path.getPath(null, start, end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathCoordNullStart()
    {
        PathFinder path = new EchoPathFinder(full);
        path.getPath(world, null, end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathCoordNullEnd()
    {
        PathFinder path = new EchoPathFinder(full);
        path.getPath(world, start, null);
    }

    @Test
    public void getPathCoordStartBounds()
    {
        CoreTest.testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                PathFinder path = new EchoPathFinder(full);
                path.getPath(world, new Coordinate(x, y), end);
            }
        }, world.width(), world.height());
    }

    @Test
    public void getPathIntIncomplete()
    {
        EchoPathFinder path = Mockito.spy(new EchoPathFinder(partial));

        List<Coordinate> actual = path.getPath(world, start.x(), start.y(), end.x(), end.y());
        Assert.assertNull(actual);
        Mockito.verify(path).calcPath(world, start, end);
    }

    @Test
    public void getPathIntComplete()
    {
        EchoPathFinder path = Mockito.spy(new EchoPathFinder(full));

        List<Coordinate> actual = path.getPath(world, start.x(), start.y(), end.x(), end.y());
        Assert.assertSame(full, actual);
        Mockito.verify(path).calcPath(world, start, end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathIntNullWorld()
    {
        PathFinder path = new EchoPathFinder(full);
        path.getPath(null, start.x(), start.y(), end.x(), end.y());
    }

    @Test
    public void getPathIntStartBounds()
    {
        CoreTest.testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                PathFinder path = new EchoPathFinder(full);
                path.getPath(world, x, y, end.x(), end.y());
            }
        }, world.width(), world.height());
    }

    @Test(expected = IllegalStateException.class)
    public void calcNullPath()
    {
        PathFinder path = Mockito.spy(new EchoPathFinder(null));
        path.getPartialPath(world, start, end);
    }

    public static class EchoPathFinder extends PathFinder
    {
        private List<Coordinate> expected;

        public EchoPathFinder(List<Coordinate> expected)
        {
            this.expected = expected;
        }

        @Override
        public List<Coordinate> calcPath(World world, Coordinate start, Coordinate end)
        {
            return expected;
        }
    }
}
