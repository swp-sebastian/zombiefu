package rogue.creature;

import jade.util.datatype.ColoredChar;
import java.awt.Color;
import rogue.weapon.Weapon;

public class Dragon extends Monster
{
    public Dragon()
    {
        super(ColoredChar.create('D', Color.RED,false),"Dragon",1,1,1,new Weapon("Feuriger Atem",100));
    }
}
