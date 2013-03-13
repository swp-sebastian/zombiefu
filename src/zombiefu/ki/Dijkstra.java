/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.ki;

import jade.core.World;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import zombiefu.creature.Monster;
import zombiefu.util.ZombieTools;

/**
 *
 * @author tomas
 */
public class Dijkstra implements MoveAlgorithm {

    private int maxDistance;

    public Dijkstra(int s) {
        maxDistance = s;
    }

    public Dijkstra() {
        this(30);
    }

    @Override
    public Direction directionTo(World w, Coordinate start, Coordinate ziel) throws TargetNotFoundException {

        List<Direction> dirs = ZombieTools.getAllowedDirections();

        int[][] distance = new int[w.width()][w.height()];
        distance[start.x()][start.y()] = 1;

        ArrayList<Coordinate> queue = new ArrayList<Coordinate>();
        queue.add(start);

        Coordinate[][] previous = new Coordinate[w.width()][w.height()];

        while (!queue.isEmpty()) {

            Coordinate vertex = queue.remove(0);
            int ndist = distance[vertex.x()][vertex.y()] + 1;
            if (ndist > maxDistance) {
                throw new TargetNotFoundException();
            }
            
            Collections.shuffle(dirs);

            for (Direction d : dirs) {
                Coordinate nachbar = vertex.getTranslated(d);

                if (!w.insideBounds(nachbar) || !w.passableAt(nachbar) || !w.getActorsAt(Monster.class, nachbar).isEmpty() || previous[nachbar.x()][nachbar.y()] != null) {
                    continue;
                }
                queue.add(nachbar);
                distance[nachbar.x()][nachbar.y()] = ndist;
                previous[nachbar.x()][nachbar.y()] = vertex;

                if (nachbar.equals(ziel)) {
                    Coordinate back = nachbar;
                    while (distance[back.x()][back.y()] > 2) {
                        back = previous[back.x()][back.y()];
                    }
                    return start.directionTo(back);
                }
            }
        }

        throw new TargetNotFoundException();
    }
}
