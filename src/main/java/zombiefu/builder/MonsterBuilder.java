package zombiefu.builder;

import jade.util.datatype.ColoredChar;
import zombiefu.creature.AttributeSet;
import zombiefu.items.Weapon;
import zombiefu.creature.Monster;
import zombiefu.player.Attribute;
import zombiefu.util.ITMString;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public class MonsterBuilder {

    private ColoredChar face;
    private String name;
    private AttributeSet attSet;
    private int ects;
    private Weapon weapon;
    private ITMString dropOnDeath;
    private boolean staticAttributes;
    private double habitatRadius;
    private int chaseDistance;

    public MonsterBuilder(ColoredChar face, String name, AttributeSet attSet, Weapon w, int ects, ITMString dropOnDeath, boolean staticAttributes, int chaseDistance) {
        this.name = name;
        this.face = face;
        this.attSet = attSet;
        this.weapon = w;
        this.dropOnDeath = dropOnDeath;
        this.ects = ects;
        this.staticAttributes = staticAttributes;
        this.chaseDistance = chaseDistance;
        this.habitatRadius = 6.0; // TODO
    }

    public Monster buildMonster() {
        AttributeSet calcAtt;
        if (staticAttributes) {
            calcAtt = attSet;
        } else {
            calcAtt = new AttributeSet(null);
            double plvl = (double) ZombieGame.getPlayer().getSemester();
            double faktor = (plvl + 1.0) / 2.0 * ZombieTools.getRandomDouble(0.8, 1.2);
            if(faktor < 1) {
                faktor = 1.0;
            }
            for (Attribute att : Attribute.values()) {
                calcAtt.put(att, (int) (attSet.get(att) * faktor));
            }
        }
        return new Monster(face, name, calcAtt, weapon, ects, dropOnDeath.getActorSet(), habitatRadius, chaseDistance);
    }
}
