package zombiefu.creature;

import jade.core.Actor;
import jade.fov.RayCaster;
import jade.fov.ViewField;
import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import zombiefu.items.Waffe;
import zombiefu.items.Waffentyp;
import zombiefu.util.DamageAnimation;
import zombiefu.util.ZombieTools;

public abstract class Creature extends Actor {

    protected int healthPoints;
    protected int attackValue;
    protected int defenseValue;
    private int dazed;
    protected String name;
    protected ViewField fov;
    protected int sichtweite;
    protected boolean godMode;

    public Creature(ColoredChar face, String n, int h, int a, int d) {
        super(face);
        dazed = 0;
        name = n;
        healthPoints = h;
        attackValue = a;
        defenseValue = d;
    }

    public Collection<Coordinate> getViewField() {
        return fov.getViewField(world(), pos(), sichtweite);
    }

    public Creature(ColoredChar face, String name) {
        this(face, name, 1, 1, 1);
    }

    public Creature(ColoredChar face) {
        this(face, "Zombie");
    }

    public abstract Waffe getActiveWeapon();

    @Override
    public void setPos(int x, int y) {
        if (world().passableAt(x, y)) {
            super.setPos(x, y);
        }
    }

    public String getName() {
        return name;
    }

    protected abstract Direction getAttackDirection();

    public void attackCreature(Creature cr) {
        if (this.equals(cr)) {
            return;
        }

        // Wer keine Waffe hat, kann nicht angreifen!
        if (getActiveWeapon() == null) {
            return;
        }

        // Monster greifen keine Monster an!
        if (this instanceof Monster && cr instanceof Monster) {
            return;
        }

        System.out.println(getName() + " attacks " + cr.getName() + " with " + getActiveWeapon().getName() + " (Damage: " + getActiveWeapon().getDamage() + "). Attack value: " + attackValue + ", Defense Value: " + cr.defenseValue);

        // Calculate damage
        int damage = (int) (((double) getActiveWeapon().getDamage()) * ((double) attackValue / (double) cr.defenseValue) * (double) Dice.global.nextInt(20, 40) / 30);
        if (damage == 0) {
            damage = 1;
        }

        ZombieTools.sendMessage(getName() + " hat " + cr.getName() + " " + damage + " Schadenspunkte hinzugefügt.");

        cr.hurt(damage, this);
    }

    public void attackCoordinate(Coordinate coord) {
        Guard.argumentIsNotNull(coord);
        DamageAnimation anim = new DamageAnimation();
        world().addActor(anim, coord);
        Collection<Creature> actors = world().getActorsAt(Creature.class, coord);
        if (actors.isEmpty()) {
            ZombieTools.sendMessage("Niemanden getroffen!");
        } else {
            Iterator<Creature> it = actors.iterator();
            while (it.hasNext()) {
                attackCreature(it.next());
            }
        }
        world().removeActor(anim);
        anim.expire();
    }

    private void createDetonation(Coordinate c, double blastRadius) {
        // TODO: Verschönern (mit RayCaster)
        Collection<Creature> targets = new HashSet<Creature>();
        Collection<DamageAnimation> anims = new HashSet<DamageAnimation>();
        int blastMax = (int) Math.ceil(blastRadius);
        for (int x = c.x() - blastMax; x <= c.x() + blastMax; x++) {
            for (int y = c.y() - blastMax; y <= c.y() + blastMax; y++) {
                Coordinate neu = new Coordinate(x, y);
                if (neu.distance(c) <= blastRadius) {
                    DamageAnimation anim = new DamageAnimation();
                    anims.add(anim);
                    world().addActor(anim, neu);
                    Collection<Creature> actors = world().getActorsAt(Creature.class, neu);
                    Iterator<Creature> it = actors.iterator();
                    while (it.hasNext()) {
                        Creature next = it.next();
                        if (!equals(next)) {
                            targets.add(next);
                        }
                    }
                }
            }
        }
        if (targets.isEmpty()) {
            ZombieTools.sendMessage("Niemanden getroffen!");
        } else {
            for (Creature target : targets) {
                attackCreature(target);
            }
        }
        for (DamageAnimation anim : anims) {
            world().removeActor(anim);
            anim.expire();
        }

    }

    private Coordinate findTargetInDirection(Direction dir, int maxDistance) {
        Coordinate nPos = pos();
        int dcounter = 0;
        do {
            System.out.println(nPos);
            nPos = nPos.getTranslated(dir);
            if (!world().insideBounds(nPos) || !world().passableAt(nPos)) {
                return nPos.getTranslated(ZombieTools.getReversedDirection(dir));
            }
            dcounter++;
        } while (world().getActorsAt(Creature.class, nPos).isEmpty() && dcounter < maxDistance);
        return nPos;
    }

    public void attack(Direction dir) {
        Waffentyp typ = getActiveWeapon().getTyp();
        Coordinate ziel;
        if (typ.isRanged()) {
            ziel = findTargetInDirection(dir, getActiveWeapon().getRange());
        } else {
            ziel = pos().getTranslated(dir);
        }
        if (typ.isDirected()) {
            attackCoordinate(ziel);
        } else {
            createDetonation(ziel, getActiveWeapon().getBlastRadius());
        }
    }

    public void attack() {
        Direction dir;
        if (getActiveWeapon().getTyp() != Waffentyp.UMKREIS) {
            dir = getAttackDirection();
            if (dir == null) {
                return;
            }
        } else {
            dir = Direction.ORIGIN;
        }
        attack(dir);
    }

    public void tryToMove(Direction dir) throws CannotMoveToImpassableFieldException {
        Guard.argumentIsNotNull(world());
        Guard.argumentIsNotNull(dir);
        if (dazed > 0) {
            dazed--;
            return;
        }
        if (dir == Direction.ORIGIN) {
            return;
        }
        Coordinate targetField = pos().getTranslated(dir);
        if (!world().passableAt(targetField)) {
            throw new CannotMoveToImpassableFieldException();
        }
        if (world().getActorsAt(Creature.class, pos().getTranslated(dir)).isEmpty()) {
            move(dir);
        } else {
            if (getActiveWeapon().getTyp() == Waffentyp.NAHKAMPF) {
                attack(dir);
            } else {
                throw new CannotMoveToImpassableFieldException();
            }
        }
    }

    protected abstract void killed(Creature killer);

    private void hurt(int i, Creature hurter) {
        System.out.println(getName() + " hat " + i + " HP verloren. ");
        if (godMode) {
            return;
        }
        if (i >= healthPoints) {
            killed(hurter);
        } else {
            healthPoints -= i;
        }
    }
}
