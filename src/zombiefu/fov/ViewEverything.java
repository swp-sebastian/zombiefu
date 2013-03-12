/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.fov;

import jade.core.World;
import jade.fov.ViewField;
import jade.util.datatype.Coordinate;
import java.util.Collection;
import java.util.HashSet;

public class ViewEverything extends ViewField {
    
    @Override
    protected Collection<Coordinate> calcViewField(World world, int x, int y, int r) {
        HashSet<Coordinate> output = new HashSet<Coordinate>();
        for (int i = 0; i < world.width(); i++) {
            for (int j = 0; j < world.height(); j++) {
                output.add(new Coordinate(i, j));
            }
        }
        return output;
    }
}
