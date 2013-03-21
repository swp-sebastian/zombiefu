/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.ki;

import jade.util.datatype.Coordinate;
import zombiefu.creature.NonPlayer;

/**
 *
 * @author tomas
 */
public class CircularHabitat implements Habitat {

    private NonPlayer monster;
    private double maxDistance;
    private Coordinate center;

    public CircularHabitat(NonPlayer monster, double maxDistance) {
        this.monster = monster;
        this.maxDistance = maxDistance;
        this.center = monster.pos();
    }

    @Override
    public boolean isAtHome(Coordinate c) {
        return c.distance(center) <= maxDistance;
    }
    @Override
    public boolean isAtHome() {
        return isAtHome(monster.pos());
    }

    @Override
    public Coordinate home() {
        return center;
    }
}
