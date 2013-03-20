/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.fov;

import jade.core.World;
import jade.fov.ViewField;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.ArrayList;
import java.util.Collection;
import zombiefu.util.ZombieTools;

/**
 *
 * @author tomas
 */
public abstract class ImprovedRayCaster extends ViewField {

    protected abstract Collection<Coordinate> getBaseField(World world, int x, int y, int r);
    
    @Override
    protected Collection<Coordinate> calcViewField(World world, int x, int y, int r) {
        Collection<Coordinate> bonus = new ArrayList<>();
        Collection<Coordinate> baseField = getBaseField(world, x, y, r);
        Coordinate n;
        for(Coordinate c: baseField) {
            for(Direction dir: ZombieTools.getAllowedDirections()) {
                n = c.getTranslated(dir);
                if(world.insideBounds(n) && !world.transparentAt(n))
                    bonus.add(n);
            }
        }
        baseField.addAll(bonus);
        return baseField;
    }
    
}
