package zombiefu.creature;

import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.awt.Color;
import java.util.Arrays;
import zombiefu.items.Waffe;

public class Zombie extends Monster {

    public Zombie() {
        super(ColoredChar.create('\u263F', Color.GREEN), "Zombie", 1, 1, 1, new Waffe("Axt", 1,ColoredChar.create('|')));
    }

    @Override
    public void act() {
        // TODO: Player suchen
        Player player = world().getActor(Player.class);
        Guard.argumentIsNotNull(player);
        Coordinate playerPos = player.pos();

        double distance = playerPos.distance(pos());
        if(distance <= 5)
            tryToMove(pos().directionTo(playerPos));
        else
            tryToMove(Dice.global.choose(Arrays.asList(Direction.values())));
    }
}
