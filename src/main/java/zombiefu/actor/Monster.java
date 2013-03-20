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
import zombiefu.exception.NoEnemyHitException;
import zombiefu.exception.TargetIsNotInThisWorldException;
import zombiefu.fight.Attack;
import zombiefu.items.WeaponType;
import zombiefu.ki.CircularHabitat;
import zombiefu.ki.Dijkstra;
import zombiefu.ki.Habitat;
import zombiefu.ki.ChaseAlgorithm;
import zombiefu.player.Attribute;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public class Monster extends NonPlayer {

    private Weapon waffe;
    protected int ectsYield;
    private Set<Actor> dropOnDeath;

    public Monster(ColoredChar face, String name, HashMap<Attribute, Integer> attSet, Weapon waffe, int ectsYield, Set<Actor> dropOnDeath, double maxDistance) {
        super(face, name, attSet, maxDistance);
        this.waffe = waffe;
        this.ectsYield = ectsYield;
        this.dropOnDeath = dropOnDeath;
    }

    @Override
    protected void pleaseAct() {

        if (habitat == null) {
            habitat = new CircularHabitat(this, maxDistance);
        }

        try {
            Coordinate pos = getPlayerPosition();
            if (positionIsVisible(pos)) {
                if (getActiveWeapon().getTyp().isRanged() && false) {
                }

                if (getActiveWeapon().getTyp() == WeaponType.UMKREIS && pos().distance(getPlayerPosition()) <= getActiveWeapon().getBlastRadius()) {
                    System.out.println("hier bin ich");
                    try {
                        new Attack(this, null).perform();
                        return;
                    } catch (NoEnemyHitException ex) {
                        return;
                    }
                }

                // Gegner nicht aus der Ferne angreifen, also in seine Richtung.
                moveToCoordinate(getPlayerPosition());
                return;
            }
        } catch (TargetIsNotInThisWorldException | TargetNotFoundException | WeaponHasNoMunitionException ex) {
        }

        if (!habitat.atHome()) {
            try {
                moveToCoordinate(habitat.home());
                return;
            } catch (TargetIsNotInThisWorldException | TargetNotFoundException | WeaponHasNoMunitionException ex) {
            }
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
    public void kill(Creature killer) {
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
            return getDirectionTo(getPlayerPosition());
        } catch (TargetNotFoundException | TargetIsNotInThisWorldException e) {
            throw new NoDirectionGivenException();
        }
    }

    @Override
    protected boolean isEnemy(Creature enemy) {
        return enemy instanceof Player;
    }
}
