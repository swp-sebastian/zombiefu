package zombiefu.fov;

import jade.core.World;
import jade.fov.ViewField;
import jade.path.PathFinder;
import jade.util.datatype.Coordinate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class CircularRayCaster extends ViewField {

    private PathFinder raycaster;

    public CircularRayCaster() {
        raycaster = new Bresenham();
    }

    private Collection<Coordinate> getCircle(Coordinate c, int radius) {        
        Collection<Coordinate> set = new ArrayList<>();
        
        int x0 = c.x();
        int y0 = c.y();
        
        int f = 1 - radius;
        int ddF_x = 1;
        int ddF_y = -2 * radius;
        int x = 0;
        int y = radius;

        set.add(new Coordinate(x0, y0 + radius));
        set.add(new Coordinate(x0, y0 - radius));
        set.add(new Coordinate(x0 + radius, y0));
        set.add(new Coordinate(x0 - radius, y0));

        while (x < y) {
            if (f >= 0) {
                y--;
                ddF_y += 2;
                f += ddF_y;
            }
            x++;
            ddF_x += 2;
            f += ddF_x;
            set.add(new Coordinate(x0 + x, y0 + y));
            set.add(new Coordinate(x0 - x, y0 + y));
            set.add(new Coordinate(x0 + x, y0 - y));
            set.add(new Coordinate(x0 - x, y0 - y));
            set.add(new Coordinate(x0 + y, y0 + x));
            set.add(new Coordinate(x0 - y, y0 + x));
            set.add(new Coordinate(x0 + y, y0 - x));
            set.add(new Coordinate(x0 - y, y0 - x));
        }
        
        return set;
    }

    @Override
    protected Collection<Coordinate> calcViewField(World world, int x, int y, int r) {
        Collection<Coordinate> fov = new HashSet<>();
        
        Coordinate center = new Coordinate(x, y);
        fov.add(center);

        for (Coordinate c : getCircle(center,r)) {
            fov.addAll(raycaster.getPartialPath(world, center, c));
        }

        return fov;
    }
}
