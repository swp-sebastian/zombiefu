package test.util.datatypes;

import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import jade.util.datatype.MutableCoordinate;
import java.awt.Point;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CoordinateTest
{
    @Test
    public void constructorSetsXY()
    {
        for(int x = -10; x <= 10; x++)
            for(int y = -10; y <= 10; y++)
                verifyCoordinate(new Coordinate(x, y), x, y);
    }

    @Test
    public void copyConstructsNewClone()
    {
        for(int x = 0; x < 10; x++)
            for(int y = 0; y < 10; y++)
            {
                Coordinate origin = new Coordinate(x, y);
                Coordinate copy = origin.copy();
                Assert.assertEquals(origin, copy);
                Assert.assertNotSame(origin, copy);
            }
    }

    @Test
    public void setXYUpdatesXY()
    {
        MutableCoordinate coord = new MutableCoordinate(0, 0);
        for(int x = -10; x <= 10; x++)
            for(int y = -10; y <= 10; y++)
            {
                coord.setXY(x, y);
                verifyCoordinate(coord, x, y);
            }
    }

    @Test
    public void setXYCoordCallsInt()
    {
        MutableCoordinate coord = Mockito.spy(new MutableCoordinate(5, 6));
        for(int x = -10; x <= 10; x++)
            for(int y = -10; y <= 10; y++)
            {
                Coordinate pos = new Coordinate(x, y);
                coord.setXY(pos);
                Mockito.verify(coord).setXY(x, y);
            }
    }

    @Test(expected = IllegalArgumentException.class)
    public void setXYNullCoord()
    {
        MutableCoordinate coord = new MutableCoordinate(5, 5);
        coord.setXY(null);
    }

    @Test
    public void translateSetsXY()
    {
        int x = -100;
        int y = -50;
        MutableCoordinate coord = new MutableCoordinate(x, y);
        for(int i = 0; i < 100; i++)
        {
            x += i;
            y += 2 * i;
            coord.translate(i, 2 * i);
            verifyCoordinate(coord, x, y);
        }
    }

    @Test
    public void translateCallsSetXY()
    {
        int x = -100;
        int y = -50;
        MutableCoordinate coord = Mockito.spy(new MutableCoordinate(x, y));
        for(int i = 0; i < 100; i++)
        {
            x += i;
            y += 2 * i;
            coord.translate(i, 2 * i);
            Mockito.verify(coord).setXY(x, y);
        }
    }

    @Test
    public void translateCoordCallsSetXY()
    {
        int x = -100;
        int y = -50;
        MutableCoordinate coord = Mockito.spy(new MutableCoordinate(x, y));
        for(int i = 0; i < 100; i++)
        {
            x += i;
            y += 2 * i;
            coord.translate(new Coordinate(i, 2 * i));
            Mockito.verify(coord).setXY(x, y);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void translateNullCoord()
    {
        MutableCoordinate coord = new MutableCoordinate(5, 4);
        coord.translate((Coordinate)null);
    }

    @Test
    public void translateDirectionCallsSetXY()
    {
        for(Direction dir : Direction.values())
        {
            MutableCoordinate coord = Mockito.spy(new MutableCoordinate(0, 0));
            coord.translate(dir);
            Mockito.verify(coord).setXY(dir.dx(), dir.dy());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void translateNullDirection()
    {
        MutableCoordinate coord = new MutableCoordinate(5, 4);
        coord.translate((Direction)null);
    }

    @Test
    public void directionToCoord()
    {
        Coordinate start = new Coordinate(4, 6);
        for(int dx = -1; dx <= 1; dx++)
            for(int dy = -1; dy <= 1; dy++)
            {
                Coordinate goal = new Coordinate(start.x() + dx, start.y() + dy);
                Direction dir = start.directionTo(goal);
                Assert.assertEquals(dir.dx(), dx);
                Assert.assertEquals(dir.dy(), dy);
            }
    }

    @Test
    public void directionToInt()
    {
        Coordinate start = new Coordinate(4, 6);
        for(int dx = -1; dx <= 1; dx++)
            for(int dy = -1; dy <= 1; dy++)
            {
                Direction dir = start.directionTo(start.x() + dx, start.y() + dy);
                Assert.assertEquals(dir.dx(), dx);
                Assert.assertEquals(dir.dy(), dy);
            }
    }

    @Test(expected = IllegalArgumentException.class)
    public void directionToNullCoord()
    {
        Coordinate start = new Coordinate(5, 3);
        start.directionTo(null);
    }

    @Test
    public void getTranslatedReturnsCopy()
    {
        Coordinate coord = new Coordinate(6, 3);
        Coordinate actual = coord.getTranslated(0, 0);
        Assert.assertNotSame(coord, actual);
        Assert.assertEquals(coord, actual);
    }

    @Test
    public void getTranslatedTranslatesCopy()
    {
        int dx = 4;
        int dy = -1;
        Coordinate coord = new Coordinate(6, 3);
        Coordinate expected = new Coordinate(coord.x() + dx, coord.y() + dy);
        Assert.assertEquals(expected, coord.getTranslated(dx, dy));
    }

    @Test
    public void getTranslatedLeavesSource()
    {
        Coordinate coord = new Coordinate(6, 3);
        coord.getTranslated(4, -1);
        Assert.assertTrue(coord.equals(new Coordinate(6, 3)));
    }

    @Test
    public void getTranslatedCoord()
    {
        Coordinate expected = new Coordinate(-1, -1);
        Coordinate delta = new Coordinate(4, 2);
        Coordinate coord = Mockito.mock(Coordinate.class, Mockito.CALLS_REAL_METHODS);
        Mockito.doReturn(expected).when(coord).getTranslated(delta.x(), delta.y());
        Assert.assertSame(expected, coord.getTranslated(delta));
        Mockito.verify(coord).getTranslated(delta.x(), delta.y());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTranslatedNullCoord()
    {
        Coordinate coord = new Coordinate(5, 3);
        coord.getTranslated((Coordinate)null);
    }

    @Test
    public void getTranslatedDirection()
    {
        Coordinate expected = new Coordinate(-1, -1);
        Direction dir = Direction.NORTHEAST;
        Coordinate coord = Mockito.mock(Coordinate.class, Mockito.CALLS_REAL_METHODS);
        Mockito.doReturn(expected).when(coord).getTranslated(dir.dx(), dir.dy());
        Assert.assertSame(expected, coord.getTranslated(dir));
        Mockito.verify(coord).getTranslated(dir.dx(), dir.dy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTranslatedNullDirection()
    {
        Coordinate coord = new Coordinate(5, 3);
        coord.getTranslated((Direction)null);
    }

    @Test
    public void equals()
    {
        Coordinate coord1 = new Coordinate(4, 5);
        Coordinate coord2 = new Coordinate(4, 5);

        Assert.assertEquals(coord1, coord1);
        Assert.assertEquals(coord1, coord2);
    }

    @Test
    public void notEquals()
    {
        Coordinate coord1 = new Coordinate(4, 5);
        Coordinate coord2 = new Coordinate(4, 4);
        Coordinate coord3 = new Coordinate(5, 5);
        Coordinate coord4 = new Coordinate(3, 6);

        Assert.assertFalse(coord1.equals(coord2));
        Assert.assertFalse(coord1.equals(coord3));
        Assert.assertFalse(coord1.equals(coord4));

        Assert.assertFalse(coord2.equals(coord3));
        Assert.assertFalse(coord2.equals(coord4));

        Assert.assertFalse(coord3.equals(coord4));
    }

    @Test
    public void notEqualsNull()
    {
        Coordinate coord = new Coordinate(0, 0);
        Assert.assertFalse(coord.equals(null));
    }

    @Test
    public void notEqualsNonCoord()
    {
        Coordinate coord = new Coordinate(0, 0);
        Assert.assertFalse(coord.equals(Direction.ORIGIN));
    }

    @Test
    public void hashUniqueCommonRange()
    {
        HashSet<Integer> usedHashes = new HashSet<Integer>();
        for(int x = 0; x < 512; x++)
            for(int y = 0; y < 512; y++)
            {
                Coordinate coord = new Coordinate(x, y);
                int hash = coord.hashCode();
                Assert.assertFalse(usedHashes.contains(hash));
                usedHashes.add(hash);
            }
    }

    @Test
    public void hashEqualCommonRange()
    {
        for(int x = 0; x < 512; x++)
            for(int y = 0; y < 512; y++)
            {
                Coordinate coord1 = new Coordinate(x, y);
                Coordinate coord2 = new Coordinate(x, y);
                Assert.assertEquals(coord1.hashCode(), coord2.hashCode());
            }
    }

    @Test
    public void stringRepresentation()
    {
        for(int x = 0; x < 5; x++)
            for(int y = 0; y < 5; y++)
            {
                String expected = String.format("(%d, %d)", x, y);
                Coordinate coord = new Coordinate(x, y);
                Assert.assertEquals(expected, coord.toString());
            }
    }

    @Test
    public void distanceCartesianCoord()
    {
        for(int sx = 0; sx < 10; sx++)
            for(int sy = 0; sy < 10; sy++)
                for(int ex = -10; ex < 10; ex++)
                    for(int ey = -10; ey < 10; ey++)
                    {
                        double expected = Point.distance(sx, sy, ex, ey);
                        Coordinate start = new Coordinate(sx, sy);
                        Coordinate end = new Coordinate(ex, ey);
                        Assert.assertEquals(expected, start.distance(end), 0);
                    }
    }

    @Test(expected = IllegalArgumentException.class)
    public void distanceCartesianNullCoord()
    {
        Coordinate start = new Coordinate(0, 0);
        start.distance(null);
    }

    @Test
    public void distanceCartesianInt()
    {
        for(int sx = 0; sx < 10; sx++)
            for(int sy = 0; sy < 10; sy++)
                for(int ex = -10; ex < 10; ex++)
                    for(int ey = -10; ey < 10; ey++)
                    {
                        double expected = Point.distance(sx, sy, ex, ey);
                        Coordinate start = new Coordinate(sx, sy);
                        Assert.assertEquals(expected, start.distance(ex, ey), 0);
                    }
    }

    private void verifyCoordinate(Coordinate coordinate, int x, int y)
    {
        Assert.assertEquals(x, coordinate.x());
        Assert.assertEquals(y, coordinate.y());
    }
}
