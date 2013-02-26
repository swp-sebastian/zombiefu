package test.util.datatypes;

import jade.util.datatype.Direction;
import junit.framework.Assert;
import org.junit.Test;

public class DirectionTest
{
    @Test
    public void vectorNorth()
    {
        verifyVector(Direction.NORTH, 0, -1);
    }

    @Test
    public void vectorNortheast()
    {
        verifyVector(Direction.NORTHEAST, 1, -1);
    }

    @Test
    public void vectorEast()
    {
        verifyVector(Direction.EAST, 1, 0);
    }

    @Test
    public void vectorSoutheast()
    {
        verifyVector(Direction.SOUTHEAST, 1, 1);
    }

    @Test
    public void vectorSouth()
    {
        verifyVector(Direction.SOUTH, 0, 1);
    }

    @Test
    public void vectorSouthwest()
    {
        verifyVector(Direction.SOUTHWEST, -1, 1);
    }

    @Test
    public void vectorWest()
    {
        verifyVector(Direction.WEST, -1, 0);
    }

    @Test
    public void vectorNorthWest()
    {
        verifyVector(Direction.NORTHWEST, -1, -1);
    }

    @Test
    public void vectorOrigin()
    {
        verifyVector(Direction.ORIGIN, 0, 0);
    }

    private void verifyVector(Direction direction, int dx, int dy)
    {
        Assert.assertEquals(dx, direction.dx());
        Assert.assertEquals(dy, direction.dy());
    }

    @Test
    public void keyDirNorth()
    {
        verifyKeyDir('k', Direction.NORTH);
        verifyKeyDir('8', Direction.NORTH);
    }

    @Test
    public void keyDirNortheast()
    {
        verifyKeyDir('u', Direction.NORTHEAST);
        verifyKeyDir('9', Direction.NORTHEAST);
    }

    @Test
    public void keyDirEast()
    {
        verifyKeyDir('l', Direction.EAST);
        verifyKeyDir('6', Direction.EAST);
    }

    @Test
    public void keyDirSoutheast()
    {
        verifyKeyDir('n', Direction.SOUTHEAST);
        verifyKeyDir('3', Direction.SOUTHEAST);
    }

    @Test
    public void keyDirSouth()
    {
        verifyKeyDir('j', Direction.SOUTH);
        verifyKeyDir('2', Direction.SOUTH);
    }

    @Test
    public void keyDirSouthwest()
    {
        verifyKeyDir('b', Direction.SOUTHWEST);
        verifyKeyDir('1', Direction.SOUTHWEST);
    }

    @Test
    public void keyDirWest()
    {
        verifyKeyDir('h', Direction.WEST);
        verifyKeyDir('4', Direction.WEST);
    }

    @Test
    public void keyDirNorthWest()
    {
        verifyKeyDir('y', Direction.NORTHWEST);
        verifyKeyDir('7', Direction.NORTHWEST);
    }

    @Test
    public void keyDirOrigin()
    {
        verifyKeyDir('.', Direction.ORIGIN);
        verifyKeyDir('5', Direction.ORIGIN);
    }

    @Test
    public void keyDirNull()
    {
        verifyKeyDir('a', null);
        verifyKeyDir('q', null);
        verifyKeyDir('c', null);
        verifyKeyDir('+', null);
    }

    public void verifyKeyDir(char key, Direction expected)
    {
        Assert.assertEquals(expected, Direction.keyToDir(key));
    }
}
