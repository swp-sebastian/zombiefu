package zombiefu.creature;

import jade.core.Actor;
import zombiefu.items.Weapon;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.Set;
import zombiefu.exception.NoPlaceToMoveException;
import zombiefu.exception.WeaponHasNoMunitionException;
import zombiefu.exception.TargetNotFoundException;
import zombiefu.exception.NoDirectionGivenException;
import zombiefu.exception.NoEnemyHitException;
import zombiefu.exception.TargetIsNotInThisWorldException;
import zombiefu.fight.Attack;
import zombiefu.items.WeaponType;
import zombiefu.ki.CircularHabitat;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

/**
 * Creature, which attacks the player.
 */
public class Monster extends NonPlayer {

    private Weapon waffe;
    protected int ectsYield;
    private Set<Actor> dropOnDeath;

    public Monster(ColoredChar face, String name, AttributeSet attSet, Weapon waffe, int ectsYield, Set<Actor> dropOnDeath, double maxDistance) {
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
