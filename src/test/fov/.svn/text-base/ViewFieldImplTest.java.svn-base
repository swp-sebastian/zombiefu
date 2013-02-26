package test.fov;

import jade.core.World;
import jade.fov.ViewField;
import jade.util.datatype.Coordinate;
import java.util.Collection;
import java.util.HashSet;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import test.WorldBuilder;

public abstract class ViewFieldImplTest
{
    protected ViewField viewField;

    @Before
    public void init()
    {
        viewField = getInstance();
    }

    protected abstract ViewField getInstance();

    @Test
    public void emptyAllVisible()
    {
        World world = WorldBuilder.getEmptyMap();
        int cx = world.width() / 2;
        int cy = world.height() / 2;
        int r = Math.max(cx, cy);

        Collection<Coordinate> expected = new HashSet<Coordinate>();
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                expected.add(new Coordinate(x, y));
        for(int x = 0; x < world.width(); x += world.width() - 1)
            for(int y = 0; y < world.height(); y += world.height() - 1)
                expected.remove(new Coordinate(x, y));
        Collection<Coordinate> actual = viewField.getViewField(world, cx, cy, r);

        for(Coordinate coord : expected)
            Assert.assertTrue(actual.contains(coord));
    }

    @Test
    public void rangeLimits()
    {
        World world = WorldBuilder.getEmptyMap();
        int cx = world.width() / 2;
        int cy = world.height() / 2;
        int r = 2;

        Collection<Coordinate> expected = new HashSet<Coordinate>();
        for(int x = cx - r; x <= cx + r; x++)
            for(int y = cy - r; y <= cy + r; y++)
                expected.add(new Coordinate(x, y));
        Collection<Coordinate> actual = viewField.getViewField(world, cx, cy, r);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void pillarN()
    {
        World world = WorldBuilder.getSinglePillar();
        Collection<Coordinate> actual = viewField.getViewField(world, 6, 5, 10);

        Assert.assertTrue(actual.contains(new Coordinate(6, 6)));
        for(int y = 7; y <= 12; y++)
            Assert.assertFalse(actual.contains(new Coordinate(6, y)));
    }

    @Test
    public void pillarS()
    {
        World world = WorldBuilder.getSinglePillar();
        Collection<Coordinate> actual = viewField.getViewField(world, 6, 7, 10);

        Assert.assertTrue(actual.contains(new Coordinate(6, 6)));
        for(int y = 0; y <= 5; y++)
            Assert.assertFalse(actual.contains(new Coordinate(6, y)));
    }

    @Test
    public void pillarE()
    {
        World world = WorldBuilder.getSinglePillar();
        Collection<Coordinate> actual = viewField.getViewField(world, 7, 6, 10);

        Assert.assertTrue(actual.contains(new Coordinate(6, 6)));
        for(int x = 0; x <= 5; x++)
            Assert.assertFalse(actual.contains(new Coordinate(x, 6)));
    }

    @Test
    public void pillarW()
    {
        World world = WorldBuilder.getSinglePillar();
        Collection<Coordinate> actual = viewField.getViewField(world, 5, 6, 10);

        Assert.assertTrue(actual.contains(new Coordinate(6, 6)));
        for(int x = 7; x <= 12; x++)
            Assert.assertFalse(actual.contains(new Coordinate(x, 6)));
    }

    @Test
    public void pillarNW()
    {
        World world = WorldBuilder.getSinglePillar();
        Collection<Coordinate> actual = viewField.getViewField(world, 5, 5, 10);

        Assert.assertTrue(actual.contains(new Coordinate(6, 6)));
        for(int i = 7; i <= 12; i++)
            Assert.assertFalse(actual.contains(new Coordinate(i, i)));
    }

    @Test
    public void pillarSE()
    {
        World world = WorldBuilder.getSinglePillar();
        Collection<Coordinate> actual = viewField.getViewField(world, 7, 7, 10);

        Assert.assertTrue(actual.contains(new Coordinate(6, 6)));
        for(int i = 0; i <= 5; i++)
            Assert.assertFalse(actual.contains(new Coordinate(i, i)));
    }

    @Test
    public void pillarNE()
    {
        World world = WorldBuilder.getSinglePillar();
        Collection<Coordinate> actual = viewField.getViewField(world, 7, 5, 10);

        Assert.assertTrue(actual.contains(new Coordinate(6, 6)));
        for(int i = 0; i <= 5; i++)
            Assert.assertFalse(actual.contains(new Coordinate(i, 12 - i)));
    }

    @Test
    public void pillarSW()
    {
        World world = WorldBuilder.getSinglePillar();
        Collection<Coordinate> actual = viewField.getViewField(world, 5, 7, 10);

        Assert.assertTrue(actual.contains(new Coordinate(6, 6)));
        for(int i = 0; i <= 5; i++)
            Assert.assertFalse(actual.contains(new Coordinate(7 + i, 5 - i)));
    }

    @Test
    public void wallBlocksNE()
    {
        World world = WorldBuilder.getWallNS();
        Collection<Coordinate> actual = viewField.getViewField(world, 3, 6, 10);

        for(int x = 1; x <= 5; x++)
            for(int y = 0; y <= 12; y++)
                Assert.assertTrue(actual.contains(new Coordinate(x, y)));
        for(int x = 7; x <= 12; x++)
            for(int y = 0; y <= 6; y++)
                Assert.assertFalse(actual.contains(new Coordinate(x, y)));
    }

    @Test
    public void wallBlocksNW()
    {
        World world = WorldBuilder.getWallNS();
        Collection<Coordinate> actual = viewField.getViewField(world, 9, 6, 10);

        for(int x = 7; x <= 11; x++)
            for(int y = 0; y <= 12; y++)
                Assert.assertTrue(actual.contains(new Coordinate(x, y)));
        for(int x = 0; x <= 5; x++)
            for(int y = 0; y <= 6; y++)
                Assert.assertFalse(actual.contains(new Coordinate(x, y)));
    }

    @Test
    public void wallBlocksSE()
    {
        World world = WorldBuilder.getWallSN();
        Collection<Coordinate> actual = viewField.getViewField(world, 3, 6, 10);

        for(int x = 1; x <= 5; x++)
            for(int y = 0; y <= 12; y++)
                Assert.assertTrue(actual.contains(new Coordinate(x, y)));
        for(int x = 7; x <= 12; x++)
            for(int y = 6; y <= 11; y++)
                Assert.assertFalse(actual.contains(new Coordinate(x, y)));
    }

    @Test
    public void wallBlocksSW()
    {
        World world = WorldBuilder.getWallSN();
        Collection<Coordinate> actual = viewField.getViewField(world, 9, 6, 10);

        for(int x = 7; x <= 11; x++)
            for(int y = 0; y <= 12; y++)
                Assert.assertTrue(actual.contains(new Coordinate(x, y)));
        for(int x = 0; x <= 5; x++)
            for(int y = 6; y <= 11; y++)
                Assert.assertFalse(actual.contains(new Coordinate(x, y)));
    }
}
