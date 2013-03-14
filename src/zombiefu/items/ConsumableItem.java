/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.items;

import jade.util.datatype.ColoredChar;
import zombiefu.creature.Player;
import zombiefu.exception.CannotBeConsumedException;

/**
 *
 * @author tomas
 */
public abstract class ConsumableItem extends Item {

    public ConsumableItem(ColoredChar face, String s) {
        super(face, s);
    }

    public abstract void getConsumedBy(Player pl) throws CannotBeConsumedException;
}
