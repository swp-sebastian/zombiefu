/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.fight;

import jade.core.World;
import jade.path.PathFinder;
import jade.util.Guard;
import jade.util.datatype.Coordinate;
import java.util.ArrayList;
import java.util.List;
import zombiefu.actor.NotPassableActor;

/**
 *
 * @author tomas
 */
public class ProjectileBresenham extends PathFinder {

    private int maxRange;
    public ProjectileBresenham(int range) {
        maxRange = range;
    }

    @Override
    protected List<Coordinate> calcPath(World world, Coordinate start, Coordinate end) {
        Guard.argumentsAreNotNull(world, start, end);
        Guard.argumentsInsideBounds(start.x(), start.y(), world.width(), world.height());

        List<Coordinate> path = new ArrayList<Coordinate>();
        int dx = end.x() - start.x();
        int dy = end.y() - start.y();

        if (Math.abs(dx) > Math.abs(dy)) {
            float slope = (float) dy / dx;
            int ix = (start.x() < end.x()) ? 1 : -1;
            int x = start.x();
            int n = 0;
            while (x != end.x()) {
                x += ix;
                n++;
                int y = (int) (slope * (x - start.x()) + start.y() + .5f);
                if (!world.insideBounds(x, y)) {
                    break;
                }
                Coordinate nc = new Coordinate(x, y);
                path.add(nc);
                if (!world.passableAt(nc) || world.getActorAt(NotPassableActor.class, nc) != null || n > maxRange) {
                    break;
                }
            }
        } else {
            float slope = (float) dx / dy;
            int iy = (start.y() < end.y()) ? 1 : -1;
            int y = start.y();
            int n = 0;
            while (y != end.y()) {
                n++;
                y += iy;
                int x = (int) (slope * (y - start.y()) + start.x() + .5f);
                if (!world.insideBounds(x, y)) {
                    break;
                }
                Coordinate nc = new Coordinate(x, y);
                path.add(nc);
                if (!world.passableAt(nc) || world.getActorAt(NotPassableActor.class, nc) != null || n > maxRange) {
                    break;
                }
            }
        }
        return path;
    }
}
