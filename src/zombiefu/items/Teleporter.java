package zombiefu.items;

import zombiefu.creature.Player;
import zombiefu.level.Level;
import jade.core.Actor;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;

public class Teleporter extends Actor {
	
	private String raum1, raum2;
	private boolean inRaum1 = true;
	private Coordinate coordinateRaum1, coordinateRaum2;

	public Teleporter(ColoredChar face,String raum1, String raum2, Coordinate cRaum1, Coordinate cRaum2) {
		super(face);
		this.raum1 = raum1;
		this.raum2 = raum2;
		this.coordinateRaum1 = cRaum1;
		this.coordinateRaum2 = cRaum2;
	}
	
	public Coordinate aktuell(){
		return inRaum1 ? coordinateRaum1 : coordinateRaum2;
	}
	
	public Coordinate newPos(Level world){
		int x = aktuell().x();
		int y = aktuell().y();
		if (world.passableAt(x, y+1))
			return new Coordinate(x,y+1);
		else if (world.passableAt(x, y-1))
			return new Coordinate(x,y-1);
		else if (world.passableAt(x+1, y))
			return new Coordinate(x+1,y);
		else
			return new Coordinate(x-1,y);
	}
	
	@Override
	public void act() {
		Level world = (Level) world();
		Player player = world.getActor(Player.class);
		if (player==null) return;
		if (player.pos().distance(coordinateRaum1)<1 && inRaum1){
			inRaum1 = false;
			world.changeLevel("src/sources/"+raum2+".txt",this);
		}
		else if (player.pos().distance(coordinateRaum2)<1 && !inRaum1){
			inRaum1 = true;
			world.changeLevel("src/sources/"+raum1+".txt",this);
		}
	}
}
