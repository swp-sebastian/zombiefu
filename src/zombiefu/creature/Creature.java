package zombiefu.creature;

import jade.core.Actor;
import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.Collection;
import java.util.Iterator;
import zombiefu.weapon.Weapon;

public abstract class Creature extends Actor {

    private int healthPoints;
    private int attackValue;
    private int defenseValue;
    private int dazed;
    private Weapon activeWeapon;
    private String name;

    public Creature(ColoredChar face, String n, int h, int a, int d, Weapon w) {
        super(face);
        dazed = 0;
        name = n;
        healthPoints = h;
        attackValue = a;
        defenseValue = d;
        activeWeapon = w;
    }

    public Creature(ColoredChar face) {
        this(face, "Zombie", 1, 1, 1, new Weapon("Faust", 1));
    }

    @Override
    public void setPos(int x, int y) {
        if (world().passableAt(x, y)) {
            super.setPos(x, y);
        }
    }
    
    public void attack(Creature cr) {
        // Nicht diagonal bewegen:
        // Guard.validateArgument(Math.abs(x()-cr.x())+Math.abs(y()-cr.y()) == 1);
        Guard.validateArgument(!this.equals(cr));
        if (activeWeapon == null) {
            return;
        }
        System.out.println(getName() + " attacks " + cr.getName() + " with " + activeWeapon.getName() + " (Damage: " + activeWeapon.getDamage() + "). Attack value: " + attackValue + ", Defense Value: " + cr.defenseValue);
        
        // Calculate damage
        int damage = activeWeapon.getDamage() * (attackValue / cr.defenseValue) * Dice.global.nextInt(20, 40) / 30;
        System.out.println("Berechneter Schaden: " + damage);
        
        
        cr.hurt(damage);
    }
    
    public String getName() {
        return name;
    }
    
    public void attack(int x, int y) {
        Collection<Creature> actors = world().getActorsAt(Creature.class, x, y);
        Iterator<Creature> it = actors.iterator();
        while (it.hasNext()) {
            attack(it.next());
        }
    }

    public void attack(Coordinate coord) {
        attack(coord.x(), coord.y());
    }

    public void tryToMove(Direction dir) {
        Creature creature = world().getActorAt(Creature.class, pos().getTranslated(dir));
        if (dir == Direction.ORIGIN) {
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

    public void roundHouseKick() {
        for (Direction dir : Direction.values()) {
            if (dir != Direction.ORIGIN) {
                attack(pos().getTranslated(dir));
            }
        }
    }

    private void hurt(int i) {
        System.out.print(getName() + " hat " + i + " HP verloren. ");
        if (i >= healthPoints) {
            System.out.println("Tot.");
            expire();
        } else {
            healthPoints -= i;
            System.out.println("HP: " + healthPoints);
        }
    }
}
