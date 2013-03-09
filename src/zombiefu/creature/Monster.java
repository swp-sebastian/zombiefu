package zombiefu.creature;

import java.util.Arrays;

import zombiefu.items.Waffe;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;

public class Monster extends Creature {

    public Monster(ColoredChar face, String n, int h, int a, int d, Waffe w) {
        super(face, n, h, a, d, w);
    }

    public Monster(ColoredChar face) {
        super(face);
    }

    @Override
    public void act() {
    	if (super.healthPoints==0) expire();
    	else tryToMove(Dice.global.choose(Arrays.asList(Direction.values())));
    }
}
