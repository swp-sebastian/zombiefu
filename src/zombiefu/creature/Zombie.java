package zombiefu.creature;

import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.awt.Color;
import java.util.Arrays;
import zombiefu.weapon.Weapon;

public class Zombie extends Monster {

    public Zombie() {
        super(ColoredChar.create('Z', Color.GREEN), "Zombie", 1, 1, 1, new Weapon("Axt", 1));
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
