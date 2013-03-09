package zombiefu.items;

import zombiefu.creature.Player;
import zombiefu.level.Level;
import jade.core.Actor;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;

public class Teleporter extends Actor {

	public Teleporter(ColoredChar face) {
		super(face);
	}

	@Override
	public void act() {
		Level world = (Level) world();
		Player player = world.getActor(Player.class);
		if (player==null || world==null) return;
		if (player.x()==x()&&player.y()==y()-1)
			world.changeLevel("src/sources/TestraumZ.txt",new Coordinate(33,1));
	}
}
