package zombiefu.creature;

import jade.fov.RayCaster;
import zombiefu.items.Waffe;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.Collections;
import java.util.List;
import zombiefu.exception.CannotMoveToIllegalFieldException;
import zombiefu.exception.NoPlaceToMoveException;
import zombiefu.exception.WeaponHasNoMunitionException;
import zombiefu.exception.TargetNotFoundException;
import zombiefu.exception.NoDirectionGivenException;
import zombiefu.exception.TargetIsNotInThisWorldException;
import zombiefu.items.Item;
import zombiefu.ki.StupidMover;
import zombiefu.ki.MoveAlgorithm;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public abstract class Monster extends Creature {

    protected MoveAlgorithm movealg;
    private Waffe waffe;

    public Monster(ColoredChar face, String n, int h, int a, int d, Waffe w, MoveAlgorithm m) {
        super(face, n, h, a, d);
        waffe = w;
        movealg = m;
        fov = new RayCaster();
        sichtweite = 10;
    }

    public Monster(ColoredChar face, String n, int h, int a, int d, Waffe w) {
        this(face, n, h, a, d, w, new StupidMover());
    }

    public Monster(ColoredChar face, MoveAlgorithm m) {
        super(face);
        waffe = null;
        movealg = m;
    }

    public Monster(ColoredChar face) {
        this(face, new StupidMover());
    }

    private void moveRandomly() throws NoPlaceToMoveException {
        List<Direction> dirs = ZombieTools.getAllowedDirections();
        Collections.shuffle(dirs);
        for (Direction d : dirs) {
            try {
                tryToMove(d);
                return;
            } catch (CannotMoveToIllegalFieldException ex) {
            } catch (WeaponHasNoMunitionException ex) {
            }
        }
        throw new NoPlaceToMoveException();
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
        return fov.getViewField(world(), pos(), sichtweite).contains(pos);
    }

    protected Direction directionToPlayer() throws TargetNotFoundException, TargetIsNotInThisWorldException {
        return movealg.directionTo(world(), pos(), getPlayerPosition());
    }

    protected void moveToPlayer() throws TargetIsNotInThisWorldException, TargetNotFoundException, WeaponHasNoMunitionException {
        try {
            tryToMove(directionToPlayer());
        } catch (CannotMoveToIllegalFieldException ex) {
            //Logger.getLogger(Monster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void act() {
        try {
            if (positionIsVisible(getPlayerPosition())) {
                moveToPlayer();
                return;
            }
        } catch (TargetIsNotInThisWorldException ex) {
        } catch (TargetNotFoundException ex) {
        } catch (WeaponHasNoMunitionException ex) {
        }
        try {
            moveRandomly();
        } catch (NoPlaceToMoveException ex) {
            ZombieTools.log(getName() + ": Cannot move - doing nothing");
            return;
        }
    }

    @Override
    public Waffe getActiveWeapon() {
        return waffe;
    }

    protected abstract Item itemDroppedOnKill();

    @Override
    protected void killed(Creature killer) {
        Item it = itemDroppedOnKill();
        if (it != null) {
            world().addActor(it, pos());
        }
        expire();
        ZombieGame.newMessage(killer.getName() + " hat " + getName() + " getötet.");
    }

    @Override
    protected Direction getAttackDirection() throws NoDirectionGivenException {
        // TODO: Überprüfen, ob Gegner wirklich in einer Linie ist
        try {
            return directionToPlayer();
        } catch (TargetNotFoundException e) {
            throw new NoDirectionGivenException();
        } catch (TargetIsNotInThisWorldException ex) {
            throw new NoDirectionGivenException();
        }
    }
}
