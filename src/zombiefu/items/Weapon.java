package zombiefu.items;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import java.util.Set;
import zombiefu.player.Discipline;

/**
 *
 * @author tomas
 */
public class Weapon extends Item {

    protected int damage;
    protected WeaponType wtyp;
    protected double blastRadius;
    protected int range;
    protected int munition;
    protected Set<Discipline> experts;

    public Weapon(ColoredChar c, String n, int d, WeaponType w, Set<Discipline> experts, double radius, int range) {
        super(c, n);
        this.damage = d;
        this.wtyp = w;
        this.blastRadius = radius;
        this.range = range;
        this.munition = 0;
        this.experts = experts;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getMunition() {
        return munition;
    }

    public double getBlastRadius() {
        return blastRadius;
    }

    public WeaponType getTyp() {
        return wtyp;
    }

    public boolean hasMunition() {
        return munition != 0;
    }

    public void addMunition(int m) {
        if (munition == -1) {
            return;
        } else if (m == -1) {
            munition = -1;
        } else {
            munition += m;
        }
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

    public String getMunitionToString() {
        if (munition == -1) {
            return "∞";
        } else {
            return String.valueOf(munition);
        }
    }

    public boolean isExpert(Discipline a) {
        return experts.contains(a);
    }
            
}
