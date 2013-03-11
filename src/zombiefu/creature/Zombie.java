package zombiefu.creature;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.items.Waffe;
import zombiefu.ki.Dijkstra;

public class Zombie extends Monster {

	public Zombie() {
		super(ColoredChar.create('\u263F', Color.GREEN), "Zombie", Dice.global
				.nextInt(1, 20), Dice.global.nextInt(1, 20), Dice.global
				.nextInt(1, 20), new Waffe("Kralle", 2, ColoredChar.create('|')), new Dijkstra());
	}
	
}
