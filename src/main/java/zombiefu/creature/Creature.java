package zombiefu.creature;

import jade.fov.ViewField;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombiefu.actor.NotPassableActor;
import zombiefu.exception.CannnotMoveToNonPassableActorException;
import zombiefu.exception.CannotMoveToIllegalFieldException;
import zombiefu.exception.WeaponHasNoMunitionException;
import zombiefu.exception.CannotAttackWithoutMeleeWeaponException;
import zombiefu.exception.DidNotActException;
import zombiefu.items.Weapon;
import zombiefu.items.WeaponType;
import zombiefu.exception.NoDirectionGivenException;
import zombiefu.exception.NoEnemyHitException;
import zombiefu.fight.Attack;
import zombiefu.player.Attribute;
import zombiefu.player.Discipline;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public abstract class Creature extends NotPassableActor {

    protected AttributeSet attributSet;
    protected Discipline discipline;
    protected int dazed;
    protected int healthPoints;
    protected String name;
    protected ViewField fov;
    protected int chaseRadius;
    protected boolean godMode;

    public Creature(ColoredChar face, String n, AttributeSet attSet) {
        super(face);
        dazed = 0;
        name = n;
        attributSet = attSet;
        healthPoints = attributSet.get(Attribute.MAXHP);
    }

    public abstract Weapon getActiveWeapon();

    protected abstract Direction getAttackDirection() throws NoDirectionGivenException;

    protected abstract boolean isEnemy(Creature enemy);

    protected abstract void pleaseAct() throws DidNotActException;

    protected abstract void pleaseActDazed();

    public abstract void kill(Creature killer);

    public abstract boolean hasUnlimitedMunition();

    public Discipline getDiscipline() {
        return discipline;
    }

    public boolean isGod() {
        return godMode;
    }

    public boolean isDazed() {
        return dazed > 0;
    }

    public void daze(int d, Creature dazer) {
        dazed = d;
        ZombieGame.refreshBottomFrame();
        ZombieGame.newMessage(dazer.getName() + " hat " + getName() + " gelähmt.");
    }

    public int getAttribute(Attribute att) {
        return attributSet.get(att);
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public Collection<Coordinate> getViewField() {
        return fov.getViewField(world(), pos(), chaseRadius);
    }

    @Override
    public void setPos(int x, int y) {
        if (world().passableAt(x, y)) {
            super.setPos(x, y);
        }
    }

    public String getName() {
        return name;
    }

    public void attack() throws NoDirectionGivenException, WeaponHasNoMunitionException, NoEnemyHitException {
        Direction dir;
        if (getActiveWeapon().getTyp() != WeaponType.UMKREIS) {
            dir = getAttackDirection();
        } else {
            dir = Direction.ORIGIN;
        }
        new Attack(this, dir).perform();
    }

    @Override
    public void act() {
        if (dazed > 0) {
            dazed--;
            pleaseActDazed();
        } else {
            while (true) {
                try {
                    pleaseAct();
                    return;
                } catch (DidNotActException ex) {
                }
            }
        }
    }

    public void tryToMove(Direction dir) throws CannotMoveToIllegalFieldException, CannotAttackWithoutMeleeWeaponException, CannnotMoveToNonPassableActorException, NoEnemyHitException, WeaponHasNoMunitionException {

        Guard.argumentIsNotNull(world());
        Guard.argumentIsNotNull(dir);

        if (dir == Direction.ORIGIN) {
            return;
        }

        Coordinate targetField = pos().getTranslated(dir);
        if (!world().insideBounds(targetField) || !world().passableAt(targetField)) {
            throw new CannotMoveToIllegalFieldException();
        }

        NotPassableActor actor = world().getActorAt(NotPassableActor.class, pos().getTranslated(dir));
        if (actor == null) {
            move(dir);
            return;
        }

        if (actor instanceof Creature && isEnemy((Creature) actor)) {
            if (getActiveWeapon().getTyp() == WeaponType.NAHKAMPF) {
                new Attack(this, dir).perform();
            } else {
                throw new CannotAttackWithoutMeleeWeaponException();
            }
        }

        throw new CannnotMoveToNonPassableActorException(actor);
    }

    public void hurt(int i, Creature hurter) {
        ZombieTools.log(getName() + " hat " + i + " HP verloren. ");
        if (godMode) {
            return;
        }
        if (i >= healthPoints) {
            kill(hurter);
        } else {
            healthPoints -= i;
            ZombieGame.newMessage(hurter.getName() + " hat " + getName() + " " + i + " Schadenspunkte hinzugefügt. Verbleibend: " + healthPoints + "/" + getAttribute(Attribute.MAXHP));
        }
    }
}
