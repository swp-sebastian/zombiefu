package zombiefu.creature;

import java.util.Arrays;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import zombiefu.weapon.Weapon;

public class Monster extends Creature {

    public Monster(ColoredChar face, String n, int h, int a, int d, Weapon w) {
        super(face, n, h, a, d, w);
    }

    public Monster(ColoredChar face) {
        super(face);
    }

    @Override
    public void act() {
        tryToMove(Dice.global.choose(Arrays.asList(Direction.values())));
    }
}
