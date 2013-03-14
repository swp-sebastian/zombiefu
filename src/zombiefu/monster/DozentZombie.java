package zombiefu.monster;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.items.Item;
import zombiefu.items.Waffe;
import zombiefu.util.ConfigHelper;

public class DozentZombie extends Monster {

    public DozentZombie() {
        super(ColoredChar.create('\u265E', Color.RED), "Prof. Jung",
                Dice.global.nextInt(20, 50), Dice.global.nextInt(20, 50),
                Dice.global.nextInt(20, 50), ConfigHelper.newWaffeByName("Feuerlöscher"));
    }

    @Override
    protected Item itemDroppedOnKill() {
        return getActiveWeapon();
    }
}
