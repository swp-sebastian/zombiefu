package zombiefu.creature;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.items.Waffe;

public class DozentZombie extends Monster {

	public DozentZombie() {
		super(ColoredChar.create('\u265E', Color.RED), "Prof. Jung",
				Dice.global.nextInt(20, 50), Dice.global.nextInt(20, 50),
				Dice.global.nextInt(20, 50), new Waffe("MonsterKralle", 3,
						ColoredChar.create('|')));
	}
}
