/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.fight;

import jade.core.World;
import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import zombiefu.actor.Creature;
import zombiefu.actor.NotPassableActor;
import zombiefu.exception.NoEnemyHitException;
import zombiefu.exception.WeaponHasNoMunitionException;
import zombiefu.items.Weapon;
import zombiefu.items.WeaponType;
import zombiefu.player.Attribute;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

/**
 *
 * @author tomas
 */
public class Attack {

    private static final double EXPERT_BONUS = 1.5; // Faktor
    private Creature attacker;
    private Weapon weapon;
    private WeaponType wtype;
    private Direction dir;
    private World world;
    private Set<DamageAnimation> anims;
    private Coordinate impactPoint;

    public Attack(Creature attacker, Direction dir) {
        this.attacker = attacker;
        this.dir = dir;

        this.weapon = attacker.getActiveWeapon();
        this.wtype = this.weapon.getTyp();
        this.world = attacker.world();

        this.anims = new HashSet<>();
    }

    private void createAnimation(Coordinate c) {
        DamageAnimation anim = new DamageAnimation();
        world.addActor(anim, c);
        anims.add(anim);
    }

    private void clearAllAnimations() {
        Iterator<DamageAnimation> it = anims.iterator();
        while (it.hasNext()) {
            DamageAnimation anim = it.next();
            world.removeActor(anim);
            anim.expire();
        }
        anims.clear();
    }

    private Coordinate findTargetInDirection(Direction dir, int maxDistance) {
        Coordinate nPos = attacker.pos();
        int dcounter = 0;
        do {
            nPos = nPos.getTranslated(dir);
            if (!world.insideBounds(nPos) || !world.passableAt(nPos)) {
                return nPos.getTranslated(ZombieTools.getReversedDirection(dir));
            }
            dcounter++;
        } while (world.getActorsAt(NotPassableActor.class, nPos).isEmpty() && dcounter < maxDistance);
        return nPos;
    }

    private void hurtCreature(Creature cr) {

        double faktor = getDamageCoefficient(cr);

        ZombieTools.log("hurtCreature(): " + attacker.getName() + " hurts "
                + cr.getName() + " with " + weapon.getName()
                + " (Damage: " + weapon.getDamage()
                + ", Experte: " + weapon.isExpert(attacker.getDiscipline()) + "). Attack value: " + attacker.getAttribute(Attribute.ATTACK) + ", Defense Value: "
                + cr.getAttribute(Attribute.DEFENSE) + ", Faktor: " + faktor);

        // Calculate damage
        int damage = (int) (((double) weapon.getDamage())
                * ((double) attacker.getAttribute(Attribute.ATTACK) / (double) cr.getAttribute(Attribute.DEFENSE))
                * (double) Dice.global.nextInt(20, 40) / 30 * faktor * (weapon.isExpert(attacker.getDiscipline()) ? EXPERT_BONUS : 1.0));
        if (damage == 0) {
            damage = 1;
        }

        ZombieGame.newMessage(attacker.getName() + " hat " + cr.getName() + " " + damage + " Schadenspunkte hinzugefügt.");

        cr.hurt(damage, attacker);
    }

    private void dazeCreature(Creature cr) {
        if (Dice.global.chance((int) (100 * weapon.getDazeProbability()))) {
            ZombieTools.log("dazeCreature(): " + attacker.getName() + " dazes " + cr.getName() + " for " + weapon.getDazeTurns() + " turns.");
            ZombieGame.newMessage(attacker.getName() + " hat " + cr.getName() + " für " + String.valueOf(weapon.getDazeTurns()) + " Runden gelähmt.");
            cr.daze(weapon.getDazeTurns(), cr);
        } else if (weapon.getDamage() == 0) {
            ZombieGame.newMessage(attacker.getName() + " hat verfehlt.");
        }
    }

    private void attackCreature(Creature cr) {

        // Wer keine Weapon hat, kann nicht angreifen!
        if (weapon == null) {
            return;
        }

        if (weapon.getDamage() > 0) {
            hurtCreature(cr);
        }

        if (weapon.getDazeTurns() > 0) {
            dazeCreature(cr);
        }
    }

    private void attackCreatureSet(Collection<Creature> targets) throws NoEnemyHitException {
        for (Creature target : targets) {
            attackCreature(target);
        }
        if (targets.isEmpty()) {
            throw new NoEnemyHitException();
        }
    }

    private void attackCoordinate(Coordinate coord) throws NoEnemyHitException {
        Guard.argumentIsNotNull(coord);
        createAnimation(coord);
        attackCreatureSet(world.getActorsAt(Creature.class, coord));
    }

    private void createDetonation(Coordinate c, double blastRadius, boolean includeCenter) throws NoEnemyHitException {
        // TODO: Verschönern (mit RayCaster)
        Collection<Creature> targets = new HashSet<>();

        int blastMax = (int) Math.ceil(blastRadius);

        for (int x = Math.max(0, c.x() - blastMax); x <= Math.min(c.x() + blastMax, world.width() - 1); x++) {
            for (int y = Math.max(0, c.y() - blastMax); y <= Math.min(c.y() + blastMax, world.height() - 1); y++) {
                Coordinate neu = new Coordinate(x, y);
                if (neu.distance(c) <= blastRadius && (includeCenter || !c.equals(neu))) {
                    createAnimation(neu);
                    targets.addAll(world.getActorsAt(Creature.class, neu));
                }
            }
        }

        attackCreatureSet(targets);
    }

    public void perform() throws WeaponHasNoMunitionException, NoEnemyHitException {
        weapon.useMunition();

        if (wtype.isRanged()) {
            impactPoint = findTargetInDirection(dir, weapon.getRange());
        } else {
            impactPoint = attacker.pos().getTranslated(dir);
        }

        try {
            if (wtype.isDirected()) {
                attackCoordinate(impactPoint);
            } else {
                createDetonation(impactPoint, weapon.getBlastRadius(), wtype.isRanged());
            }
        } catch (NoEnemyHitException ex) {
            throw ex;
        } finally {
            clearAllAnimations();
        }
    }

    private double getDamageCoefficient(Creature cr) {
        Guard.argumentIsNotNull(impactPoint);
        double distance;
        switch (wtype) {
            case NAHKAMPF:
                Guard.verifyState(cr.pos().equals(impactPoint));
                return 1.0;
            case FERNKAMPF:
                Guard.verifyState(cr.pos().equals(impactPoint));
                distance = attacker.pos().distance(impactPoint) / weapon.getRange();
                return 1 - distance * distance / 2;
            case UMKREIS:
                distance = cr.pos().distance(impactPoint) / weapon.getBlastRadius();
                return 1 - distance * distance / 2;
            case GRANATE:
                distance = cr.pos().distance(impactPoint) / weapon.getBlastRadius();
                return 1 - distance * distance / 2;
            default:
                throw new AssertionError(wtype.name());
        }
    }
}