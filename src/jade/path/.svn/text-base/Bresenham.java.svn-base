package jade.path;

import jade.core.World;
import jade.util.Guard;
import jade.util.datatype.Coordinate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements Bresenham's line drawing algorithm for finding straight paths. Note that the integer
 * only optimization is not included as using floats makes for more intuitive and readable code.
 * Hopefully the performance issues of using a float are not what they were back in the days of
 * Bresenham.
 */
public class Bresenham extends PathFinder
{
    @Override
    protected List<Coordinate> calcPath(World world, Coordinate start, Coordinate end)
    {
        Guard.argumentsAreNotNull(world, start, end);
        Guard.argumentsInsideBounds(start.x(), start.y(), world.width(), world.height());

        List<Coordinate> path = new ArrayList<Coordinate>();
        int dx = end.x() - start.x();
        int dy = end.y() - start.y();

        if(Math.abs(dx) > Math.abs(dy))
        {
            float slope = (float)dy / dx;
            int ix = (start.x() < end.x()) ? 1 : -1;
            int x = start.x();
            while(x != end.x())
            {
                x += ix;
                int y = (int)(slope * (x - start.x()) + start.y() + .5f);
                path.add(new Coordinate(x, y));
                if(!world.passableAt(x, y))
                    break;
            }
        }
        else
        {
            float slope = (float)dx / dy;
            int iy = (start.y() < end.y()) ? 1 : -1;
            int y = start.y();
            while(y != end.y())
            {
                y += iy;
                int x = (int)(slope * (y - start.y()) + start.x() + .5f);
                path.add(new Coordinate(x, y));
                if(!world.passableAt(x, y))
                    break;
            }
        }
        return path;
    }
}
