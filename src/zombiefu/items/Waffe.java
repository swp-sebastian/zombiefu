package zombiefu.items;

import jade.util.datatype.ColoredChar;

/**
 *
 * @author tomas
 */
public class Waffe extends Item {

    private static final int DEFAULT_RANGE = 10;
    private static final double DEFAULT_RADIUS = 3.0;
    protected int damage;
    protected Waffentyp wtyp;
    protected double blastRadius;
    protected int range;
    protected int munition;

    public Waffe(ColoredChar c, String n, int d, Waffentyp w, double radius, int range) {
        super(c, n);
        this.damage = d;
        this.wtyp = w;
        this.blastRadius = radius;
        this.range = range;
        this.munition = 0;
    }

    public Waffe(ColoredChar c, String n, int d, Waffentyp w, double radius) {
        this(c, n, d, w, radius, DEFAULT_RANGE);
    }

    public Waffe(ColoredChar c, String n, int d, Waffentyp w, int range) {
        this(c, n, d, w, DEFAULT_RADIUS, range);
    }

    public Waffe(ColoredChar c, String n, int d, Waffentyp w) {
        this(c, n, d, w, DEFAULT_RADIUS, DEFAULT_RANGE);
    }

    public Waffe(ColoredChar c, String n, int d) {
        this(c, n, d, Waffentyp.NAHKAMPF, DEFAULT_RADIUS, DEFAULT_RANGE);
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public double getBlastRadius() {
        return blastRadius;
    }

    public Waffentyp getTyp() {
        return wtyp;
    }

    public boolean hasMunition() {
        return munition != 0;
    }

    public void addMunition(int m) {
        if (munition == -1) {
            return;
        }
        munition += m;
    }

    public void useMunition() {
        if (munition == -1) {
            return;
        }
        munition -= 1;
    }

    public void setUnlimitedMunition(boolean unlim) {
        if (unlim) {
            munition = -1;
        } else if (munition == -1) {
            munition = 0;
        }
    }
}
