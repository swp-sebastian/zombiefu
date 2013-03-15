package zombiefu.monster;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.items.Item;
import zombiefu.items.Waffe;
import zombiefu.util.ConfigHelper;

public class DozentZombie extends Monster {
    
    private Item item;
    
    public DozentZombie(String name,int h, int a,int d,Waffe w,int ects, Item item) {
        super(ColoredChar.create('\u265E', Color.RED),name,h,a,d,w,ects);
        this.item = item;
    }

}
