/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.creature;

import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.Collections;
import java.util.List;
import zombiefu.exception.CannnotMoveToNonPassableActorException;
import zombiefu.exception.CannotAttackWithoutMeleeWeaponException;
import zombiefu.exception.CannotMoveToIllegalFieldException;
import zombiefu.exception.DidNotActException;
import zombiefu.exception.NoEnemyHitException;
import zombiefu.exception.NoPlaceToMoveException;
import zombiefu.exception.TargetIsNotInThisWorldException;
import zombiefu.exception.TargetNotFoundException;
import zombiefu.exception.WeaponHasNoMunitionException;
import zombiefu.fov.CircularRayCaster;
import zombiefu.ki.ChaseAlgorithm;
import zombiefu.ki.CircularHabitat;
import zombiefu.ki.Dijkstra;
import zombiefu.ki.Habitat;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

/**
 *
 * @author tomas
 */
public abstract class NonPlayer extends Creature {

    protected ChaseAlgorithm chasealg;
    protected Habitat habitat;
    protected double maxDistance;

    public NonPlayer(ColoredChar face, String name, AttributeSet attSet, double maxDistance) {
        super(face, name, attSet);
        this.maxDistance = maxDistance;

        this.chasealg = new Dijkstra();
        this.fov = new CircularRayCaster();
        this.sichtweite = 10;
    }

    protected void moveRandomly() throws NoPlaceToMoveException {

        if (habitat == null) {
            habitat = new CircularHabitat(this, maxDistance);
        }

        if (!habitat.isAtHome()) {
            try {
                moveToCoordinate(habitat.home());
                return;
            } catch (TargetIsNotInThisWorldException | TargetNotFoundException | WeaponHasNoMunitionException ex) {
            }
        }

        List<Direction> dirs = ZombieTools.getAllowedDirections();
        Collections.shuffle(dirs);
        for (Direction d : dirs) {
            if (!habitat.isAtHome(pos().getTranslated(d))) {
                continue;
            }
            try {
                tryToMove(d);
                return;
            } catch (CannotMoveToIllegalFieldException | WeaponHasNoMunitionException | CannotAttackWithoutMeleeWeaponException | CannnotMoveToNonPassableActorException ex) {
            } catch (NoEnemyHitException ex) {
                ex.close();
            }
        }
        throw new NoPlaceToMoveException();
    }

    protected Coordinate getPlayerPosition() throws TargetIsNotInThisWorldException {
        Guard.argumentIsNotNull(world());
        Player player = ZombieGame.getPlayer();

        if (player.world() != world()) {
            throw new TargetIsNotInThisWorldException();
        }

        return player.pos();
    }

    protected boolean positionIsVisible(Coordinate pos) throws TargetIsNotInThisWorldException {
        return fov.getViewField(world(), pos(), sichtweite).contains(pos);
    }

    protected Direction getDirectionTo(Coordinate coord) throws TargetNotFoundException {
        return chasealg.directionTo(world(), pos(), coord);
    }

    protected void moveToCoordinate(Coordinate coord) throws TargetIsNotInThisWorldException, TargetNotFoundException, WeaponHasNoMunitionException {
        moveToDirection(getDirectionTo(coord));
    }

    protected void moveToDirection(Direction dir) throws TargetIsNotInThisWorldException, WeaponHasNoMunitionException {
        try {
            tryToMove(dir);
        } catch (CannotMoveToIllegalFieldException | CannotAttackWithoutMeleeWeaponException | CannnotMoveToNonPassableActorException ex) {
        } catch (NoEnemyHitException ex) {
            ex.close();
        }
    }

    @Override
    public void pleaseActDazed() {
    }
}
