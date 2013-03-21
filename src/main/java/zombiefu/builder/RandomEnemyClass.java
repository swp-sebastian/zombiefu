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
public enum RandomEnemyClass {

    ADVANCED,
    COMMON,
    POOR;

    public static RandomEnemyClass fromString(String string) {
        for (RandomEnemyClass d : RandomEnemyClass.values()) {
            if (d.toString().equals(string.toUpperCase())) {
                return d;
            }
        }
        throw new IllegalArgumentException("Ungültiger RandomEnemyGenerator-Name: " + string);
    }

    public Monster getRandomMonster() {
        return ConfigHelper.newRandomMonster(this);
    }
}
