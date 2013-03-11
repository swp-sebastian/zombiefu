package zombiefu.creature;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import zombiefu.items.Waffe;

public class Human extends Creature {

    private static final String[] randomNames = {"John", "Paul", "Celestina",
        "Marcus", "Nadine", ""};

    public Human(ColoredChar face, String rank) {
        super(face, rank + randomNames[Dice.global.nextInt(randomNames.length)]);
        godMode = true;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub
    }

    @Override
    public Waffe getActiveWeapon() {
        return null;
    }
}
