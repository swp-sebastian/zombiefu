package zombiefu.creature;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
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
    }

    @Override
    public Waffe getActiveWeapon() {
        return null;
    }

    @Override
    protected void killed(Creature killer) {
        // TODO: Spiel sofort beenden.
    }

    @Override
    protected Direction getAttackDirection() {
        return null;
    }
}
