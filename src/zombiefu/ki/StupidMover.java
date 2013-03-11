/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.ki;

import jade.core.World;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;

/**
 *
 * @author tomas
 */
public class StupidMover extends MoveAlgorithm {

    @Override
    public Direction directionTo(World w, Coordinate start, Coordinate ziel) {
        return start.directionTo(ziel);
    }
    
}
