package zombiefu.builder;

import jade.util.datatype.ColoredChar;
import java.util.HashMap;
import zombiefu.items.Weapon;
import zombiefu.actor.Monster;
import zombiefu.player.Attribute;
import zombiefu.util.ITMString;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public class MonsterBuilder {

    private ColoredChar face;
    private String name;
    private HashMap<Attribute, Integer> attSet;
    private int ects;
    private Weapon weapon;
    private ITMString dropOnDeath;
    private boolean staticAttributes;

    public MonsterBuilder(ColoredChar face, String name, HashMap<Attribute, Integer> attSet, Weapon w, int ects, ITMString dropOnDeath, boolean staticAttributes) {
        this.name = name;
        this.face = face;
        this.attSet = attSet;
        this.weapon = w;
        this.dropOnDeath = dropOnDeath;
        this.ects = ects;
        this.staticAttributes = staticAttributes;
    }

    public Monster buildMonster() {
        HashMap<Attribute, Integer> calcAtt;
        if (staticAttributes) {
            calcAtt = attSet;
        } else {
            calcAtt = new HashMap<>();
            double plvl = (double) ZombieGame.getPlayer().getSemester();
            double faktor = (plvl + 1.0) / 2.0 * ZombieTools.getRandomDouble(0.8, 1.2);
            for (Attribute att : Attribute.values()) {
                calcAtt.put(att, (int) (attSet.get(att) * faktor));
            }
        }
        return new Monster(face, name, calcAtt, weapon, ects, dropOnDeath.getActorSet());
    }
}
