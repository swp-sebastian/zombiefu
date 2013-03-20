/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.ki;

import jade.core.World;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import zombiefu.exception.TargetNotFoundException;

/**
 *
 * @author tomas
 */
public interface ChaseAlgorithm {
    
    public Direction directionTo(World w, Coordinate start, Coordinate ziel) throws TargetNotFoundException;
    
}
