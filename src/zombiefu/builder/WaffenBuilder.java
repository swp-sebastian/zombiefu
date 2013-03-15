package zombiefu.builder;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import java.util.Set;
import zombiefu.items.Item;
import zombiefu.items.Waffe;
import zombiefu.items.Waffentyp;
import zombiefu.player.Discipline;

/**
 *
 * @author tomas
 */
public class WaffenBuilder extends ItemBuilder {

    private static final int DEFAULT_RANGE = 10;
    private static final double DEFAULT_RADIUS = 3.0;
    private int damage;
    private Waffentyp wtyp;
    private double blastRadius;
    private int range;
    private int munition;
    private Set<Discipline> experts;

    public WaffenBuilder(ColoredChar c, String n, int d, Waffentyp w, Set<Discipline> experts, int munition, double radius, int range) {
        this.face = c;
        this.name = n;
        this.damage = d;
        this.wtyp = w;
        this.munition = munition;
        this.blastRadius = radius;
        this.range = range;
        this.experts = experts;
    }

    public WaffenBuilder(ColoredChar c, String n, int d, Waffentyp w, Set<Discipline> experts, int munition, double radius) {
        this(c, n, d, w, experts, munition, radius, DEFAULT_RANGE);
    }

    public WaffenBuilder(ColoredChar c, String n, int d, Waffentyp w, Set<Discipline> experts, int munition, int range) {
        this(c, n, d, w, experts, munition, DEFAULT_RADIUS, range);
    }

    public WaffenBuilder(ColoredChar c, String n, int d, Waffentyp w, Set<Discipline> experts, int munition) {
        this(c, n, d, w, experts, munition, DEFAULT_RADIUS, DEFAULT_RANGE);
    }

    @Override
    public Item buildItem() {
        Waffe w = new Waffe(face, name, damage, wtyp, experts, blastRadius, range);
        if (munition == -1) {
            w.setUnlimitedMunition(true);
        } else {
            w.addMunition(Dice.global.nextInt(1, munition));
        }
        return w;
    }
}
