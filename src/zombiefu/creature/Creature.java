package zombiefu.creature;

import jade.core.Actor;
import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import zombiefu.items.Waffe;
import zombiefu.util.ZombieTools;

public abstract class Creature extends Actor {

    protected int healthPoints;
    protected int attackValue;
    protected int defenseValue;
    private int dazed;
    protected String name;
    protected boolean godMode;

    public Creature(ColoredChar face, String n, int h, int a, int d) {
        super(face);
        dazed = 0;
        name = n;
        healthPoints = h;
        attackValue = a;
        defenseValue = d;
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

    public void attack(Creature cr) {
        Guard.validateArgument(!this.equals(cr));

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
        System.out.println("Berechneter Schaden: " + damage);

        if (damage == 0) {
            damage = 1;
        }
        cr.hurt(damage, this);
    }

    public String getName() {
        return name;
    }

    public void attack(int x, int y) {
        Guard.argumentIsNotNull(x);
        Guard.argumentIsNotNull(y);
        attack(new Coordinate(x, y));
    }

    public void attack(Coordinate coord) {
        Guard.argumentIsNotNull(coord);
        Collection<Creature> actors = world().getActorsAt(Creature.class, coord);
        Iterator<Creature> it = actors.iterator();
        while (it.hasNext()) {
            attack(it.next());
        }
    }

    public void attack(Direction dir) {
        Guard.argumentIsNotNull(dir);
        attack(pos().getTranslated(dir));
    }

    protected abstract Direction getAttackDirection();

    private void createDetonation(Coordinate c) {
    }

    private Coordinate findTargetInDirection(Direction dir, int maxDistance) {
        Coordinate detPos = pos();
        int dcounter = 0;
        do {
            detPos = detPos.getTranslated(dir);
            dcounter++;
        } while (world().getActorsAt(Creature.class, detPos).isEmpty() || dcounter == maxDistance);
        return detPos;
    }

    public void attack() {
        Direction dir;
        switch (getActiveWeapon().getTyp()) {
            case NAHKAMPF:
                dir = getAttackDirection();
                if (dir != null) {
                    attack(dir);
                }
                break;
            case FERNKAMPF:
                dir = getAttackDirection();
                if (dir != null) {
                    attack(findTargetInDirection(dir, getActiveWeapon().getRange()));
                }
                break;
            case GRANATE:
                dir = getAttackDirection();
                if (dir != null) {
                    createDetonation(findTargetInDirection(dir, getActiveWeapon().getRange()));
                }
                break;
            case UMKREIS:
                for (Iterator<Direction> it = Arrays.asList(Direction.values()).iterator(); it.hasNext();) {
                    dir = it.next();
                    attack(dir);
                }
                break;
        }
    }

    public void tryToMove(Direction dir) {
        try {
            Guard.argumentIsNotNull(world());
        } catch (IllegalArgumentException e) {
            return;
        }
        Creature creature = world().getActorAt(Creature.class, pos().getTranslated(dir));
        if (dir == null || dir == Direction.ORIGIN) {
            return;
        }
        if (dazed > 0) {
            dazed--;
            return;
        }
        if (creature == null) {
            move(dir);
        } else {
            attack(creature);
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
