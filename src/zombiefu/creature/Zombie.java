package zombiefu.creature;

import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.items.Waffe;
import zombiefu.ki.Dijkstra;

public class Zombie extends Monster {

    public Zombie() {
        super(ColoredChar.create('\u263F', Color.GREEN), "Zombie", 1, 1, 1, new Waffe("Axt", 1,ColoredChar.create('|')), new Dijkstra());
    }
    
}
