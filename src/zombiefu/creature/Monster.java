package zombiefu.creature;

import java.util.Arrays;

import zombiefu.items.Waffe;
import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
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
    	try {Guard.argumentIsNotNull(world());}
    	catch (IllegalArgumentException e){return;}
        Player player = world().getActor(Player.class);
        try {Guard.argumentIsNotNull(player);}
    	catch (IllegalArgumentException e){return;}
        Coordinate playerPos = player.pos();
        // TODO: Player suchen
        double distance = playerPos.distance(pos());
        if(distance <= 5)
            tryToMove(pos().directionTo(playerPos));
        else
            tryToMove(Dice.global.choose(Arrays.asList(Direction.values())));
    }
}
