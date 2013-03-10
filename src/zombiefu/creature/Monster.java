package zombiefu.creature;

import jade.path.Bresenham;
import jade.path.PathFinder;
import java.util.Arrays;

import zombiefu.items.Waffe;
import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;

public class Monster extends Creature {
    
    private PathFinder pathfinder;

    public Monster(ColoredChar face, String n, int h, int a, int d, Waffe w) {
        super(face, n, h, a, d, w);
        pathfinder = new Bresenham();
    }

    public Monster(ColoredChar face) {
        super(face);
        pathfinder = new Bresenham();
    }

    private void moveRandomly() {
        tryToMove(Dice.global.choose(Arrays.asList(Direction.values())));
    }

    @Override
    public void act() {
        Guard.argumentIsNotNull(world());
        Player player = world().getActor(Player.class);

        if (player == null) {
            // Der Player ist in einer anderen Welt
            moveRandomly();
            return;
        }
        
        // Player suchen
        Coordinate playerPos = player.pos();
        double distance = playerPos.distance(pos());
        
        if (distance <= 10) {
            Direction toPlayer = pathfinder.getDirectionOfFirstStep(world(), pos(), playerPos);
            if(toPlayer == null)
                tryToMove(pos().directionTo(playerPos));
            else
                tryToMove(toPlayer);
        } else {
            moveRandomly();            
        }
    }
}
