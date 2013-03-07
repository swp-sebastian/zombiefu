package jade.fov;

import jade.core.World;
import jade.util.datatype.Coordinate;
import jade.util.datatype.MutableCoordinate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Uses shadow casting to quickly calculate a view field. Shadow casting is more complicated than
 * ray casting, but will only visit each tile once. This gives it a significant advantage over ray
 * casting which revisits cells multiple times when the view field is large. For this reason, shadow
 * casting is much more popular than the simpler ray casting.
 */
public class ShadowCaster extends ViewField
{
    @Override
    protected Collection<Coordinate> calcViewField(World world, int x, int y, int r)
    {
        Set<Coordinate> fov = new HashSet<Coordinate>();
        Coordinate orig = new Coordinate(x, y);
        fov.add(orig);
        for(int octant = 1; octant <= 8; octant++)
            scan(1, 1f, 0, orig, fov, world, r, octant);
        return fov;
    }

    private void scan(int depth, float startslope, float endslope, Coordinate orig,
            Collection<Coordinate> fov, World world, int range, int octant)
    {
        if(depth > range)
            return;
        int y = Math.round(startslope * depth);
        while(slope(depth, y) >= endslope)
        {
            Coordinate curr = getCurr(orig, depth, y, octant);
            Coordinate prev = getPrev(orig, curr, octant, world);
            // we were scanning open tiles and hit a wall
            if(world.passableAt(curr) && !world.passableAt(prev))
                startslope = newStartslope(depth, endslope, y);
            // we were scanning wall tiles and hit an open tile
            if(!world.passableAt(curr) && world.passableAt(prev))
                scan(depth + 1, startslope, newEndslope(depth, y), orig, fov, world, range, octant);
            fov.add(curr);
            y--;
        }
        y++;
        if(world.passableAt(getCurr(orig, depth, y, octant)))
            scan(depth + 1, startslope, endslope, orig, fov, world, range, octant);
    }

    private float newEndslope(int depth, int y)
    {
        return slope(depth - .5f, y + .5f);
    }

    private float newStartslope(int depth, float endslope, int y)
    {
        return Math.max(slope(depth + .5f, y - .5f), endslope);
    }

    private float slope(float x, float y)
    {
        return y / x;
    }

    private Coordinate getCurr(Coordinate orig, int x, int y, int octant)
    {
        MutableCoordinate curr = orig.mutableCopy();
        switch(octant)
        {
            case 1:
                curr.translate(x, y);
                break;
            case 2:
                curr.translate(x, -y);
                break;
            case 3:
                curr.translate(y, x);
                break;
            case 4:
                curr.translate(-y, x);
                break;
            case 5:
                curr.translate(-x, y);
                break;
            case 6:
                curr.translate(-x, -y);
                break;
            case 7:
                curr.translate(y, -x);
                break;
            case 8:
                curr.translate(-y, -x);
                break;
        }
        return curr;
    }

    private Coordinate getPrev(Coordinate orig, Coordinate curr, int octant, World world)
    {
        MutableCoordinate prev = curr.mutableCopy();
        if(prev.x() == 0 || prev.y() == 0 || prev.x() == world.width() - 1
                || prev.y() == world.height() - 1)
            return prev;
        switch(octant)
        {
            case 1:
                prev.translate(0, 1);
                break;
            case 2:
                prev.translate(0, -1);
                break;
            case 3:
                prev.translate(1, 0);
                break;
            case 4:
                prev.translate(-1, 0);
                break;
            case 5:
                prev.translate(0, 1);
                break;
            case 6:
                prev.translate(0, -1);
                break;
            case 7:
                prev.translate(1, 0);
                break;
            case 8:
                prev.translate(-1, 0);
                break;
        }
        return prev;
    }
}
