/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.ki;

import jade.util.datatype.Coordinate;

/**
 *
 * @author tomas
 */
public interface Habitat {
    
    public Coordinate home();
    public boolean isAtHome(Coordinate c);
    public boolean isAtHome();
    
}
