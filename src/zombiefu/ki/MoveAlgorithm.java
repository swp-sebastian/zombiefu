/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.ki;

import jade.core.Actor;
import jade.core.World;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import zombiefu.util.TargetIsNotInThisWorldException;

/**
 *
 * @author tomas
 */
public abstract class MoveAlgorithm {
    
    public Direction directionTo(Actor start, Actor ziel) throws TargetIsNotInThisWorldException {
        World w = start.world();
        if(ziel.bound(w)) {
            throw new TargetIsNotInThisWorldException();
        }
        return directionTo(w, start.pos(), ziel.pos());
   }
    
    public abstract Direction directionTo(World w, Coordinate start, Coordinate ziel);
    
}
