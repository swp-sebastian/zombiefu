package zombiefu.fov;

import jade.core.World;
import jade.fov.ViewField;
import jade.path.Bresenham;
import jade.path.PathFinder;
import jade.util.datatype.Coordinate;
import java.util.Collection;
import java.util.HashSet;

public class SquareRayCaster extends ViewField
{
    private PathFinder raycaster;

    public SquareRayCaster()
    {
        raycaster = new Bresenham();
    }

    @Override
    protected Collection<Coordinate> calcViewField(World world, int x, int y, int r)
    {
        Collection<Coordinate> fov = new HashSet<Coordinate>();
        fov.add(new Coordinate(x, y));

        for(int dx = -r; dx <= r; dx++)
        {
            fov.addAll(raycaster.getPartialPath(world, x, y, x + dx, y - r));
            fov.addAll(raycaster.getPartialPath(world, x, y, x + dx, y + r));
        }
        for(int dy = -r; dy <= r; dy++)
        {
            fov.addAll(raycaster.getPartialPath(world, x, y, x - r, y + dy));
            fov.addAll(raycaster.getPartialPath(world, x, y, x + r, y + dy));
        }

        return fov;
    }
}
