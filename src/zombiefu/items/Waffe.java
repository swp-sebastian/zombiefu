package zombiefu.items;

import jade.util.datatype.ColoredChar;

/**
 *
 * @author tomas
 */
public class Waffe extends Item {

    private int damage;

    public Waffe(String n, int d, ColoredChar c) {
    	super(c);
        name = n;
        damage = d;
    }
    
    public int getDamage() {
        return damage;
    }
    
}
