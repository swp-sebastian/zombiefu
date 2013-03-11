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
import java.util.logging.Level;
import java.util.logging.Logger;
import zombiefu.util.ZombieTools;

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

    protected void moveRandomly() {
        tryToMove(ZombieTools.getRandomDirection());
    }

    private Coordinate getPlayerPosition() throws PlayerIsNotInThisWorldException {
        Guard.argumentIsNotNull(world());
        Player player = world().getActor(Player.class);

        if (player == null) {
            throw new PlayerIsNotInThisWorldException();
        }

        return player.pos();
    }

    protected double getDistanceToPlayer() throws PlayerIsNotInThisWorldException {
        return getPlayerPosition().distance(pos());
    }

    protected void moveToPlayer() throws PlayerIsNotInThisWorldException {
        tryToMove(pos().directionTo(getPlayerPosition()));
    }

    @Override
    public void act() {
        try {
            double distance = getDistanceToPlayer();
            if (distance <= 10) {
                moveToPlayer();
            } else {
                moveRandomly();
            }
        } catch (PlayerIsNotInThisWorldException ex) {
        }
    }

    private static class PlayerIsNotInThisWorldException extends Exception {

        public PlayerIsNotInThisWorldException() {
        }
    }
}
