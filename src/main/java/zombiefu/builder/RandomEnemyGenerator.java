/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.builder;

import jade.util.Dice;
import java.util.Arrays;
import zombiefu.creature.Monster;
import zombiefu.items.Item;
import zombiefu.items.MensaCard;
import zombiefu.util.ConfigHelper;
import zombiefu.util.ITMString;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

/**
 *
 * @author tomas
 */
public enum RandomEnemyGenerator {

    MASTER,
    ADVANCED,
    COMMON;

    public static RandomEnemyGenerator fromString(String string) {
        for (RandomEnemyGenerator d : RandomEnemyGenerator.values()) {
            if (d.toString().equals(string.toUpperCase())) {
                return d;
            }
        }
        throw new IllegalArgumentException("Ungültiger RandomEnemyGenerator-Name: " + string);
    }

    public Monster getOneOf(String s) {
        ITMString dec = new ITMString("monster(" + Dice.global.choose(Arrays.asList(s.split(" "))) + ")");
        return dec.getSingleMonster();
    }

    public Monster getRandomMonster() {
        Item i = null;
        switch (this) {
            case MASTER:
                return getOneOf("Dozent");
            case ADVANCED:
                return getOneOf("KanonenZombie ChemikerZombie");
            case COMMON:
                if (Dice.global.chance(80)) {
                    return getOneOf("Zombie");
                } else {
                    return getOneOf("VerwirrterZombie");
                }
            default:
                throw new AssertionError(this.name());
        }
    }
}
