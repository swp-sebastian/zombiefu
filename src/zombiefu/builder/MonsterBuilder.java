package zombiefu.builder;

import jade.util.datatype.ColoredChar;
import java.util.HashMap;
import zombiefu.items.Weapon;
import zombiefu.actor.Monster;
import zombiefu.player.Attribute;
import zombiefu.util.ITMString;

public class MonsterBuilder {

    private ColoredChar face;
    private String name;
    private HashMap<Attribute, Integer> attSet;
    private int ects;
    private Weapon w;
    private ITMString dropOnDeath;

    public MonsterBuilder(ColoredChar face, String name, HashMap<Attribute, Integer> attSet, Weapon w, int ects, ITMString dropOnDeath) {
        this.name = name;
        this.face = face;
        this.attSet = attSet;
        this.w = w;
        this.dropOnDeath = dropOnDeath;
        this.ects = ects;
    }

    public Monster buildMonster() {
        return new Monster(face, name, attSet, w, ects, dropOnDeath.getActorSet());
    }
}
