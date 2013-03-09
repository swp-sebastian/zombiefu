package zombiefu.items;

import jade.core.Actor;
import jade.util.datatype.ColoredChar;

public abstract class Item extends Actor {

	public Item(ColoredChar face) {
		super(face);
	}

}
