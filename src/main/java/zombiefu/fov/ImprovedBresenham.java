package zombiefu.fov;

import jade.core.World;
import jade.path.PathFinder;
import jade.util.datatype.Coordinate;
import java.util.ArrayList;
import java.util.List;

public class ImprovedBresenham extends PathFinder {

    @Override
    public List<Coordinate> calcPath(World world, Coordinate start, Coordinate end) {

        List<Coordinate> path = new ArrayList<>();

        int i;               // loop counter 
        int ystep, xstep;    // the step on y and x axis 
        int error;           // the error accumulated during the increment 
        int errorprev;       // *vision the previous value of the error variable 
        int y = start.y(), x = start.x();  // the line points 
        int ddy, ddx;        // compulsory variables: the double values of dy and dx 
        int dx = end.x() - start.x();
        int dy = end.y() - start.y();
        path.add(start);  // first point 

        // NB the last point can't be here, because of its previous point (which has to be verified) 
        if (dy < 0) {
            ystep = -1;
            dy = -dy;
        } else {
            ystep = 1;
        }
        if (dx < 0) {
            xstep = -1;
            dx = -dx;
        } else {
            xstep = 1;
        }
        ddy = 2 * dy;  // work with double values for full precision 
        ddx = 2 * dx;
        if (ddx >= ddy) {  // first octant (0 <= slope <= 1) 
            // compulsory initialization (even for errorprev, needed when dx==dy) 
            errorprev = error = dx;  // start in the middle of the square 
            for (i = 0; i < dx; i++) {  // do not use the first point (already done) 
                x += xstep;
                error += ddy;
                if (error > ddx) {  // increment y if AFTER the middle ( > ) 
                    y += ystep;
                    error -= ddx;
                    // three cases (octant == right->right-top for directions below): 
                    if (error + errorprev < ddx) // bottom square also 
                    {
                        if (world.insideBounds(x, y - ystep)) {
                            path.add(new Coordinate(x, y - ystep));
                        }
                    } else if (error + errorprev > ddx) // left square also 
                    {
                        if (world.insideBounds(x - xstep, y)) {
                            path.add(new Coordinate(x - xstep, y));
                        }
                    } else {  // corner: bottom and left squares also 
                        if (world.insideBounds(x, y - ystep)) {
                            path.add(new Coordinate(x, y - ystep));
                        }
                        if (world.insideBounds(x - xstep, y)) {
                            path.add(new Coordinate(x - xstep, y));
                        }
                    }
                }
                if (world.insideBounds(x, y)) {
                    path.add(new Coordinate(x, y));
                }
                if ((!world.insideBounds(x, y) || !world.transparentAt(x, y)) && (!world.insideBounds(x, y - ystep) || !world.transparentAt(x, y - ystep)) && (!world.insideBounds(x - xstep, y) || !world.transparentAt(x - xstep, y))) {
                    break;
                }
                errorprev = error;
            }
        } else {  // the same as above 
            errorprev = error = dy;
            for (i = 0; i < dy; i++) {
                y += ystep;
                error += ddx;
                if (error > ddy) {
                    x += xstep;
                    error -= ddy;
                    if (error + errorprev < ddy) {
                        if (world.insideBounds(x - xstep, y)) {
                            path.add(new Coordinate(x - xstep, y));
                        }
                    } else if (error + errorprev > ddy) {
                        if (world.insideBounds(x, y - ystep)) {
                            path.add(new Coordinate(x, y - ystep));
                        }
                    } else {
                        if (world.insideBounds(x - xstep, y)) {
                            path.add(new Coordinate(x - xstep, y));
                        }
                        if (world.insideBounds(x, y - ystep)) {
                            path.add(new Coordinate(x, y - ystep));
                        }
                    }
                }
                if (world.insideBounds(x, y)) {
                    path.add(new Coordinate(x, y));
                }
                if ((!world.insideBounds(x, y) || !world.transparentAt(x, y)) && (!world.insideBounds(x, y - ystep) || !world.transparentAt(x, y - ystep)) && (!world.insideBounds(x - xstep, y) || !world.transparentAt(x - xstep, y))) {
                    break;
                }
                errorprev = error;
            }
        }
        return path;
    }
}
