package rogue.creature;

import jade.util.datatype.ColoredChar;
import java.awt.Color;
import rogue.weapon.Weapon;

public class Zombie extends Monster
{
    public Zombie()
    {
        super(ColoredChar.create('Z', Color.GREEN),"Zombie",1,1,1,new Weapon("Axt",1));
    }
}
