package zombiefu.builder;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import java.util.Set;
import zombiefu.items.Item;
import zombiefu.items.Weapon;
import zombiefu.items.WeaponType;
import zombiefu.player.Discipline;

/**
 *
 * @author tomas
 */
public class WeaponBuilder extends ItemBuilder {

    private static final int DEFAULT_RANGE = 10;
    private static final double DEFAULT_RADIUS = 3.0;
    private int damage;
    private WeaponType wtyp;
    private double blastRadius;
    private int range;
    private int munition;
    private Set<Discipline> experts;

    public WeaponBuilder(ColoredChar c, String n, int d, WeaponType w, Set<Discipline> experts, int munition, double radius, int range) {
        this.face = c;
        this.name = n;
        this.damage = d;
        this.wtyp = w;
        this.munition = munition;
        this.blastRadius = radius;
        this.range = range;
        this.experts = experts;
    }

    public WeaponBuilder(ColoredChar c, String n, int d, WeaponType w, Set<Discipline> experts, int munition, double radius) {
        this(c, n, d, w, experts, munition, radius, DEFAULT_RANGE);
    }

    public WeaponBuilder(ColoredChar c, String n, int d, WeaponType w, Set<Discipline> experts, int munition, int range) {
        this(c, n, d, w, experts, munition, DEFAULT_RADIUS, range);
    }

    public WeaponBuilder(ColoredChar c, String n, int d, WeaponType w, Set<Discipline> experts, int munition) {
        this(c, n, d, w, experts, munition, DEFAULT_RADIUS, DEFAULT_RANGE);
    }

    @Override
    public Item buildItem() {
        Weapon w = new Weapon(face, name, damage, wtyp, experts, blastRadius, range);
        if (munition == -1) {
            w.setUnlimitedMunition(true);
        } else {
            w.addMunition(Dice.global.nextInt(1, munition));
        }
        return w;
    }
}
