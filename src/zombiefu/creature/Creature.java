package zombiefu.creature;

import jade.core.Actor;
import jade.fov.RayCaster;
import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import zombiefu.items.Waffe;
import zombiefu.items.Waffentyp;
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

    private void createDetonation(Coordinate c, double blastRadius) {
        ZombieTools.sendMessage("Detonation nocht nicht implementiert. Sorry.");
        /*
         RayCaster rayCaster = new RayCaster();
         Collection<Coordinate> viewField = rayCaster.getViewField(world(), c, blastRadius);
         for (Iterator<Coordinate> it = viewField.iterator(); it.hasNext();) {
         Coordinate coord = it.next();
         world().setTile(ColoredChar.create('D'), coord.x(), coord.y());
         }
         */
    }

    private Coordinate findTargetInDirection(Direction dir, int maxDistance) {
        Coordinate nPos = pos();
        int dcounter = 0;
        do {
            System.out.println(nPos);
            nPos = nPos.getTranslated(dir);
            if (world().insideBounds(nPos)) {
                return nPos.getTranslated(ZombieTools.getReversedDirection(dir));
            }
            dcounter++;
        } while (world().getActorsAt(Creature.class, nPos).isEmpty() && dcounter < maxDistance);
        return nPos;
    }

    public void attack() {
        Direction dir;
        Waffentyp typ = getActiveWeapon().getTyp();
        if (typ != Waffentyp.UMKREIS) {
            dir = getAttackDirection();
            if (dir == null) {
                return;
            }
        } else {
            dir = Direction.ORIGIN;
        }
        Coordinate ziel;
        if (typ.isRanged()) {
            ziel = findTargetInDirection(dir, getActiveWeapon().getRange());
        } else {
            ziel = pos().getTranslated(dir);
        }
        if(typ.isDirected()) {
            attack(ziel);
        } else {
            createDetonation(ziel, getActiveWeapon().getBlastRadius());            
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
