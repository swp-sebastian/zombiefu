package test.gen.map;

import jade.core.World;
import jade.gen.map.MapGenerator;
import jade.util.datatype.Coordinate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import test.core.CoreTest.ConcreteWorld;

public abstract class MapGeneratorTest
{
    private World world;

    @Before
    public void init()
    {
        world = new ConcreteWorld(80, 24);
        getInstance().generate(world);
    }

    protected abstract MapGenerator getInstance();

    @Test
    public void connected()
    {
        Coordinate first = world.getOpenTile();
        Assert.assertNotNull(first);
        Set<Coordinate> connected = new HashSet<Coordinate>();
        Queue<Coordinate> queue = new LinkedList<Coordinate>();
        queue.add(first);
        while(!queue.isEmpty())
        {
            Coordinate next = queue.remove();
            if(!connected.add(next))
                continue;
            for(int dx = -1; dx <= 1; dx++)
                for(int dy = -1; dy <= 1; dy++)
                {
                    Coordinate pos = new Coordinate(next.x() + dx, next.y() + dy);
                    if(inBounds(world, pos) && world.passableAt(pos))
                        queue.add(pos);
                }
        }

        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
            {
                if(world.passableAt(x, y) && !connected.contains(new Coordinate(x, y)))
                    Assert.fail();
            }
    }

    private boolean inBounds(World world, Coordinate pos)
    {
        return pos.x() >= 0 && pos.y() >= 0 && pos.x() < world.width() && pos.y() < world.height();
    }
}
