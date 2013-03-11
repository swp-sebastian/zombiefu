package zombiefu.creature;


import zombiefu.items.Waffe;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import zombiefu.ki.StupidMover;
import zombiefu.ki.MoveAlgorithm;
import zombiefu.util.TargetIsNotInThisWorldException;
import zombiefu.util.ZombieTools;

public class Monster extends Creature {

    protected MoveAlgorithm movealg;

    public Monster(ColoredChar face, String n, int h, int a, int d, Waffe w, MoveAlgorithm m) {
        super(face, n, h, a, d, w);
        movealg = m;
    }

    public Monster(ColoredChar face, String n, int h, int a, int d, Waffe w) {
        this(face, n, h, a, d, w, new StupidMover());
    }

    public Monster(ColoredChar face, MoveAlgorithm m) {
        super(face);
        movealg = m;
    }

    public Monster(ColoredChar face) {
        this(face, new StupidMover());
    }

    protected void moveRandomly() {
        tryToMove(ZombieTools.getRandomDirection());
    }

    private Coordinate getPlayerPosition() throws TargetIsNotInThisWorldException {
        Guard.argumentIsNotNull(world());
        Player player = world().getActor(Player.class);

        if (player == null) {
            throw new TargetIsNotInThisWorldException();
        }

        return player.pos();
    }
    
    protected boolean positionIsVisible(Coordinate pos) throws TargetIsNotInThisWorldException {
        return pos().distance(getPlayerPosition()) <= 10;
    }

    protected void moveToPlayer() throws TargetIsNotInThisWorldException {
        tryToMove(movealg.directionTo(world(), pos(), getPlayerPosition()));
    }

    @Override
    public void act() {
        try {
            if (positionIsVisible(getPlayerPosition())) {
                moveToPlayer();
            } else {
                moveRandomly();
            }
        } catch (TargetIsNotInThisWorldException ex) {
        }
    }
    
}