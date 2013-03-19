/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.ki;

import jade.util.datatype.Coordinate;
import zombiefu.actor.Monster;

/**
 *
 * @author tomas
 */
public class CircularHabitat implements Habitat {

    private Monster monster;
    private double maxDistance;
    private Coordinate center;

    public CircularHabitat(Monster monster, double maxDistance) {
        this.monster = monster;
        this.maxDistance = maxDistance;
        this.center = monster.pos();
    }

    @Override
    public boolean atHome() {
        return monster.pos().distance(center) <= maxDistance;
    }

    @Override
    public Coordinate home() {
        return center;
    }
}
