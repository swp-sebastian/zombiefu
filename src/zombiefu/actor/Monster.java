package zombiefu.actor;

import jade.core.Actor;
import jade.fov.RayCaster;
import zombiefu.items.Weapon;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombiefu.exception.CannnotMoveToNonPassableActorException;
import zombiefu.exception.CannotAttackWithoutMeleeWeaponException;
import zombiefu.exception.CannotMoveToIllegalFieldException;
import zombiefu.exception.NoPlaceToMoveException;
import zombiefu.exception.WeaponHasNoMunitionException;
import zombiefu.exception.TargetNotFoundException;
import zombiefu.exception.NoDirectionGivenException;
import zombiefu.exception.TargetIsNotInThisWorldException;
import zombiefu.ki.StupidMover;
import zombiefu.ki.MoveAlgorithm;
import zombiefu.player.Attribute;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public class Monster extends Creature {

    protected MoveAlgorithm movealg;
    private Weapon waffe;
    protected int ectsYield;
    private Set<Actor> dropOnDeath;

    public Monster(ColoredChar face, String n, HashMap<Attribute, Integer> attSet, Weapon w, int ects, Set<Actor> drop) {
        super(face, n, attSet);
        waffe = w;
        ectsYield = ects;
        dropOnDeath = drop;

        movealg = new StupidMover();
        fov = new RayCaster();
        sichtweite = 10;
    }

    private void moveRandomly() throws NoPlaceToMoveException {
        List<Direction> dirs = ZombieTools.getAllowedDirections();
        Collections.shuffle(dirs);
        for (Direction d : dirs) {
            try {
                tryToMove(d);
                return;
            } catch (CannotMoveToIllegalFieldException | CannotAttackWithoutMeleeWeaponException | CannnotMoveToNonPassableActorException ex) {
            }
        }
        throw new NoPlaceToMoveException();
    }

    private Coordinate getPlayerPosition()
            throws TargetIsNotInThisWorldException {
        Guard.argumentIsNotNull(world());
        Player player = world().getActor(Player.class);

        if (player == null) {
            throw new TargetIsNotInThisWorldException();
        }

        return player.pos();
    }

    protected boolean positionIsVisible(Coordinate pos)
            throws TargetIsNotInThisWorldException {
        return fov.getViewField(world(), pos(), sichtweite).contains(pos);
    }

    protected Direction directionToPlayer() throws TargetNotFoundException,
            TargetIsNotInThisWorldException {
        return movealg.directionTo(world(), pos(), getPlayerPosition());
    }

    protected void moveToPlayer() throws TargetIsNotInThisWorldException, TargetNotFoundException, WeaponHasNoMunitionException {
        try {
            tryToMove(directionToPlayer());
        } catch (CannotMoveToIllegalFieldException | CannotAttackWithoutMeleeWeaponException | CannnotMoveToNonPassableActorException ex) {
        }
    }

    @Override
    public void pleaseAct() {
        try {
            if (positionIsVisible(getPlayerPosition())) {
                moveToPlayer();
                return;
            }
        } catch (TargetIsNotInThisWorldException | TargetNotFoundException | WeaponHasNoMunitionException ex) {
        }
        try {
            moveRandomly();
        } catch (NoPlaceToMoveException ex) {
            ZombieTools.log(getName() + ": Cannot move - doing nothing");
        }
    }

    @Override
    public Weapon getActiveWeapon() {
        return waffe;
    }

    @Override
    public void killed(Creature killer) {
        for (Actor it : dropOnDeath) {
            world().addActor(it, pos());
        }

        if (ZombieGame.getPlayer() == killer) {
            ZombieGame.getPlayer().giveECTS(ectsYield);
        }
        expire();
        ZombieGame.newMessage(killer.getName() + " hat " + getName()
                + " getötet.");
    }

    @Override
    protected Direction getAttackDirection() throws NoDirectionGivenException {
        // TODO: Überprüfen, ob Gegner wirklich in einer Linie ist
        try {
            return directionToPlayer();
        } catch (TargetNotFoundException | TargetIsNotInThisWorldException e) {
            throw new NoDirectionGivenException();
        }
    }

    @Override
    protected boolean isEnemy(Creature enemy) {
        return enemy instanceof Player;
    }
}
