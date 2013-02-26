package test.fov;

import jade.core.World;
import jade.fov.ViewField;
import jade.util.datatype.Coordinate;
import java.util.Collection;
import java.util.HashSet;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import test.core.CoreTest;
import test.core.CoreTest.BoundTest;

public class ViewFieldTest
{
    private World world;
    private int x;
    private int y;
    private int r;
    Collection<Coordinate> expected;

    @Before
    public void init()
    {
        world = new World(10, 10)
        {};
        x = 5;
        y = 5;
        r = 5;
        expected = new HashSet<Coordinate>();
    }

    @Test
    public void getViewFieldCallsCalc() throws Exception
    {
        EchoViewField fov = Mockito.spy(new EchoViewField(expected));

        Assert.assertSame(expected, fov.getViewField(world, x, y, r));
        Mockito.verify(fov).calcViewField(world, x, y, r);
    }

    @Test
    public void getViewFieldCoordCallsCalc() throws Exception
    {
        EchoViewField fov = Mockito.spy(new EchoViewField(expected));

        Assert.assertSame(expected, fov.getViewField(world, new Coordinate(x, y), r));
        Mockito.verify(fov).calcViewField(world, x, y, r);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getViewFieldNullCoord()
    {
        EchoViewField fov = new EchoViewField(expected);
        fov.getViewField(world, null, r);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getViewFieldNullWorld()
    {
        EchoViewField fov = new EchoViewField(expected);
        fov.getViewField(null, x, y, r);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getViewFieldCoordNullWorld()
    {
        EchoViewField fov = new EchoViewField(expected);
        fov.getViewField(null, new Coordinate(x, y), r);
    }

    @Test
    public void getViewFieldBounds()
    {
        CoreTest.testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                EchoViewField fov = new EchoViewField(expected);
                fov.getViewField(world, x, y, r);
            }
        }, world.width(), world.height());
    }

    @Test
    public void getViewFieldBoundsCoord()
    {
        CoreTest.testBounds(new BoundTest()
        {
            @Override
            public void action(int x, int y)
            {
                EchoViewField fov = new EchoViewField(expected);
                fov.getViewField(world, new Coordinate(x, y), r);
            }
        }, world.width(), world.height());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getViewFieldNegativeRadius()
    {
        EchoViewField fov = new EchoViewField(expected);
        fov.getViewField(world, x, y, -1);
    }

    @Test
    public void getViewFieldZeroRadius()
    {
        EchoViewField fov = new EchoViewField(expected);
        fov.getViewField(world, x, y, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getViewFieldCoordNegativeRadius()
    {
        EchoViewField fov = new EchoViewField(expected);
        fov.getViewField(world, new Coordinate(x, y), -1);
    }

    @Test
    public void getViewFieldCoordZeroRadius()
    {
        EchoViewField fov = new EchoViewField(expected);
        fov.getViewField(world, new Coordinate(x, y), 0);
    }

    private static class EchoViewField extends ViewField
    {
        private Collection<Coordinate> expected;

        public EchoViewField(Collection<Coordinate> expected)
        {
            this.expected = expected;
        }

        @Override
        protected Collection<Coordinate> calcViewField(World world, int x, int y, int r)
        {
            return expected;
        }
    }
}
