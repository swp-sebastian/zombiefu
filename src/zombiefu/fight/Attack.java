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
import java.util.List;
import java.util.Set;
import zombiefu.actor.Creature;
import zombiefu.exception.NoEnemyHitException;
import zombiefu.exception.WeaponHasNoMunitionException;
import zombiefu.items.Weapon;
import zombiefu.items.WeaponType;
import static zombiefu.items.WeaponType.FERNKAMPF;
import static zombiefu.items.WeaponType.GRANATE;
import static zombiefu.items.WeaponType.NAHKAMPF;
import static zombiefu.items.WeaponType.UMKREIS;
import zombiefu.player.Attribute;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

/**
 *
 * @author tomas
 */
public class Attack {

    private static final double EXPERT_BONUS = 1.75; // Faktor
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

    private Coordinate getMissileImpactPoint(Direction dir, int maxDistance) {
        Direction noiseDirection = ZombieTools.getRotatedDirection(dir, 90);
        int noise = (int) Math.floor(((double) maxDistance) * ZombieTools.getRandomDouble(0,1,attacker.getAttribute(Attribute.DEXTERITY) - 3) / (Dice.global.chance() ? 4.0 : -4.0));

        Coordinate neu = attacker.pos().getTranslated(dir.dx() * maxDistance, dir.dy() * maxDistance).getTranslated(noiseDirection.dx() * noise, noiseDirection.dy() * noise);

        List<Coordinate> partialPath = new ProjectileBresenham(weapon.getRange()).getPartialPath(world, attacker.pos(), neu);

        return partialPath.get(partialPath.size() - 1);
    }

    private void hurtCreature(Creature cr) {

        double faktor = getDamageCoefficient(cr);

        ZombieTools.log("hurtCreature(): " + attacker.getName() + " hurts "
                + cr.getName() + " with " + weapon.getName()
                + " (Damage: " + weapon.getDamage()
                + ", Experte: " + weapon.isExpert(attacker.getDiscipline()) + "). Attack value: " + attacker.getAttribute(Attribute.ATTACK) + ", Defense Value: "
                + cr.getAttribute(Attribute.DEFENSE) + ", Faktor: " + (Math.round(100*faktor) / 100.0));

        // Calculate damage
        int damage = (int) (((double) weapon.getDamage())
                * ((double) attacker.getAttribute(Attribute.ATTACK) / (double) cr.getAttribute(Attribute.DEFENSE))
                * ZombieTools.getRandomDouble(0.7, 1.3) * faktor * (weapon.isExpert(attacker.getDiscipline()) ? EXPERT_BONUS : 1.0));
        if (damage == 0) {
            damage = 1;
        }

        ZombieGame.newMessage(attacker.getName() + " hat " + cr.getName() + " " + damage + " Schadenspunkte hinzugefügt.");

        cr.hurt(damage, attacker);
    }

    private void dazeCreature(Creature cr) throws NoEnemyHitException {
        if (Dice.global.chance((int) (100 * weapon.getDazeProbability()))) {
            ZombieTools.log("dazeCreature(): " + attacker.getName() + " dazes " + cr.getName() + " for " + weapon.getDazeTurns() + " turns.");
            ZombieGame.newMessage(attacker.getName() + " hat " + cr.getName() + " für " + String.valueOf(weapon.getDazeTurns()) + " Runden gelähmt.");
            cr.daze(weapon.getDazeTurns(), cr);
        } else {
            throw new NoEnemyHitException(this);
        }
    }

    private void attackCreature(Creature cr) throws NoEnemyHitException {

        // Wer keine Weapon hat, kann nicht angreifen!
        if (weapon == null) {
            return;
        }

        if (weapon.getDamage() > 0) {
            hurtCreature(cr);
        }

        if (weapon.getDazeTurns() > 0) {
            try {
                dazeCreature(cr);
            } catch (NoEnemyHitException ex) {
                if (weapon.getDamage() == 0) {
                    throw ex;
                }
            }
        }
    }

    private void attackCreatureSet(Collection<Creature> targets) throws NoEnemyHitException {
        for (Creature target : targets) {
            attackCreature(target);
        }
        if (targets.isEmpty()) {
            throw new NoEnemyHitException(this);
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

    private double getDamageCoefficient(Creature cr) {
        Guard.argumentIsNotNull(impactPoint);
        double distance;
        switch (wtype) {
            case NAHKAMPF:
                Guard.verifyState(cr.pos().equals(impactPoint));
                return 1.0;
            case FERNKAMPF:
                Guard.verifyState(cr.pos().equals(impactPoint));
                distance = attacker.pos().distance(impactPoint) / ((double) weapon.getRange());
                return 1.0 - distance * distance / 2.0;
            case UMKREIS:
                distance = cr.pos().distance(impactPoint) / weapon.getBlastRadius();
                return 1.0 - distance * distance / 2.0;
            case GRANATE:
                distance = cr.pos().distance(impactPoint) / weapon.getBlastRadius();
                return 1.0 - distance * distance / 2.0;
            default:
                throw new AssertionError(wtype.name());
        }
    }

    private int getSuccessProbability() {
        return (int) (100.0 - 90.0 * Math.pow(Math.E, attacker.getAttribute(Attribute.DEXTERITY) / (-5.0)));
    }

    public void perform() throws WeaponHasNoMunitionException, NoEnemyHitException {
        weapon.useMunition();

        try {
            switch (wtype) {
                case NAHKAMPF:
                    // Dexterity decides, whether the attacker hits
                    impactPoint = attacker.pos().getTranslated(dir);
                    if (Dice.global.chance(getSuccessProbability())) {
                        attackCoordinate(impactPoint);
                    } else {
                        throw new NoEnemyHitException(this);
                    }
                    break;
                case FERNKAMPF:
                    // Dexterity determines the accuracy
                    impactPoint = getMissileImpactPoint(dir, weapon.getRange());
                    attackCoordinate(impactPoint);
                    break;
                case UMKREIS:
                    // Dexterity decides, whether the attacker accidently hits himself
                    impactPoint = attacker.pos().getTranslated(dir);
                    createDetonation(impactPoint, weapon.getBlastRadius(), !Dice.global.chance(getSuccessProbability()));
                    break;
                case GRANATE:
                    // Dexterity determines the accuracy
                    impactPoint = getMissileImpactPoint(dir, weapon.getRange());
                    createDetonation(impactPoint, weapon.getBlastRadius(), true);
                    break;
                default:
                    throw new AssertionError(wtype.name());
            }
        } catch (NoEnemyHitException ex) {
            throw ex;
        }
        close();
    }

    public void close() {
        clearAllAnimations();
    }

    // Debug
    private void dump() {
        ZombieTools.log("Attack-Dump: Attacker " + attacker.getName() + ", Weapon: " + weapon.getName() + " (" + wtype.toString() + "), Direction: " + dir.toString() + ", impactPoint: " + impactPoint.toString());
    }
}