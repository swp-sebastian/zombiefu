/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.items;

import jade.util.Dice;
import java.util.Arrays;
import zombiefu.player.Discipline;
import zombiefu.util.ITMString;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

/**
 *
 * @author tomas
 */
public enum RandomItemGenerator {

    RARE,
    HIGH,
    GOOD,
    COMMON,
    JUNK;

    public static RandomItemGenerator fromString(String string) {
        for (RandomItemGenerator d : RandomItemGenerator.values()) {
            if (d.toString().equals(string.toUpperCase())) {
                return d;
            }
        }
        throw new IllegalArgumentException("Ungültige ItemRank-Name: " + string);
    }

    public MensaCard getMensacard(int baseValue) {
        int value = (int) (baseValue * (ZombieGame.getPlayer().getSemester() + 1) * ZombieTools.getRandomDouble(0.5, 2.0) / 2);
        if (value < 1) {
            value = 1;
        }
        return new MensaCard(value);
    }

    public Item getOneOf(String s) {
        ITMString dec = new ITMString(Dice.global.choose(Arrays.asList(s.split(" "))));
        return dec.getSingleItem();
    }

    public Item getRandomItem() {
        Item i = null;
        switch (this) {
            case RARE:
                return getOneOf("weapon(Nuklearrakete)");
            case HIGH:
                switch (ZombieTools.getRandomIndex(35, 65)) {
                    case 0:
                        return getOneOf("weapon(Speer)");
                    case 1:
                        return getMensacard(10);
                }
                break;
            case GOOD:
                switch (ZombieTools.getRandomIndex(35, 65)) {
                    case 0:
                        return getOneOf("weapon(Speer)");
                    case 1:
                        return getMensacard(3);
                }
                break;
            case COMMON:
                switch (ZombieTools.getRandomIndex(1, 1, 4)) {
                    case 0:
                        return null;
                    case 1:
                        return getOneOf("weapon(Feuerloescher) food(Mate)");
                    case 2:
                        return getMensacard(1);
                }
                break;
            case JUNK:
                switch (ZombieTools.getRandomIndex(1, 1, 4)) {
                    case 0:
                        return getMensacard(1);
                    case 1:
                        return getOneOf("food(Wasser)");
                    case 2:
                        return null;
                }
                break;
            default:
                throw new AssertionError(this.name());
        }
        throw new AssertionError();
    }
}
