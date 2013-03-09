package zombiefu.creature;

import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.weapon.Weapon;

public class Dragon extends Monster
{
    public Dragon()
    {
        super(ColoredChar.create('D', Color.RED),"Dragon",1,1,1,new Weapon("Feuriger Atem",100));
    }
}
