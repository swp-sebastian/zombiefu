package zombiefu.items;

import jade.util.datatype.ColoredChar;

/**
 *
 * @author tomas
 */
public class Waffe extends Item {

    private int damage;
    private boolean ranged;
    private boolean directed;
    private Waffentyp wtyp;

    public Waffe(ColoredChar c, String n, int d, Waffentyp w) {
    	super(c, n);
        damage = d;
        wtyp = w;
    }
    
    public Waffe(ColoredChar c, String n, int d) {
        this(c, n, d, Waffentyp.NAHKAMPF);
    }
    
    public int getDamage() {
        return damage;
    }
    
}