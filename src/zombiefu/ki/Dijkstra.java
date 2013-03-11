/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.ki;

import jade.core.World;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.ArrayList;
import java.util.List;
import zombiefu.util.ZombieTools;

/**
 *
 * @author tomas
 */
public class Dijkstra implements MoveAlgorithm {

    @Override
    public Direction directionTo(World w, Coordinate start, Coordinate ziel) {
        
        List<Direction> dirs = ZombieTools.getAllowedDirections();
        
        int[][] distance = new int[w.width()][w.height()];
        distance[start.x()][start.y()] = 1;
        
        ArrayList<Coordinate> queue = new ArrayList<>();
        queue.add(start);
        
        Coordinate[][] previous = new Coordinate[w.width()][w.height()];
        
        while(!queue.isEmpty()) {
            
            Coordinate vertex = queue.remove(0);
            int ndist = distance[vertex.x()][vertex.y()] + 1;
            
            for(Direction d: dirs) {
                Coordinate nachbar = vertex.getTranslated(d);
                
                if(nachbar.equals(ziel)) {
                    Coordinate back = vertex;
                    while(distance[back.x()][back.y()] > 2) {
                        back = previous[back.x()][back.y()];
                    }
                    return start.directionTo(back);
                }
                
                if(!w.passableAt(nachbar) || previous[nachbar.x()][nachbar.y()] != null) {
                    continue;
                }
                queue.add(nachbar);
                distance[nachbar.x()][nachbar.y()] = ndist;
                previous[nachbar.x()][nachbar.y()] = vertex;
            }
        }
        
        return null;
    }
    
}
