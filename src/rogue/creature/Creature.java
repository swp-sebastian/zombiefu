package rogue.creature;

import jade.core.Actor;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.util.Collection;
import java.util.Iterator;
import rogue.weapon.Weapon;

public abstract class Creature extends Actor
{
    private int healthPoints;
    private int attackValue;
    private int defenseValue;
    private Weapon activeWeapon;
    
    public Creature(ColoredChar face, int h, int a, int d, Weapon w)
    {
        super(face);
        healthPoints = h;
        attackValue = a;
        defenseValue = d;
        activeWeapon = w;
    }
    
    public Creature(ColoredChar face)
    {
        this(face,1,1,1,new Weapon("Faust",1));
    }

    @Override
    public void setPos(int x, int y)
    {
        if(world().passableAt(x, y))
            super.setPos(x, y);
    }
    
    public void attack(Creature cr) {
        Guard.validateArgument(Math.abs(x()-cr.x())+Math.abs(y()-cr.y()) == 1);
        Guard.validateArgument(!this.equals(cr));     
        if(activeWeapon == null)
            return;
        cr.hurt(activeWeapon.getDamage() * ( attackValue / cr.defenseValue ));
    }
    
    public void attack(int x, int y) 
    {
        Collection<Creature> actors = world().getActorsAt(Creature.class, x, y);
        Iterator<Creature> it = actors.iterator();
        while(it.hasNext())
            attack(it.next());
    }
    
    public void attack(Coordinate coord) {
        attack(coord.x(),coord.y());
    }

    private void hurt(int i) {
        if(i >= healthPoints) {
            expire();
        } else {
            healthPoints -= i;
        }    
    }
    
}
