package zombiefu.creature;

import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.items.Waffe;

public class DozentZombie extends Monster
{
    public DozentZombie()
    {
        super(ColoredChar.create('\u265E', Color.RED),"Prof. Jung",1,1,1,new Waffe("Feuriger Atem",100,ColoredChar.create('|')));
    }
}
