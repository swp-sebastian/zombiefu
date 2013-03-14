package zombiefu.actor;

import zombiefu.player.Player;
import zombiefu.level.Level;
import jade.core.Actor;
import jade.core.World;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.awt.Color;

public class Teleporter extends Actor {

    private World targetWorld;
    private Coordinate targetCoord;

    public Teleporter(ColoredChar face, World w, Coordinate c) {
        super(face);
        this.targetWorld = w;
        this.targetCoord = c;
    }

    public Teleporter(World w, Coordinate c) {
        this(ColoredChar.create('\u25A0', Color.decode("0x8B4513")), w, c);
    }

    @Override
    public void act() {
        Level world = (Level) world();
        Player player = world.getActor(Player.class);
        if (player == null) {
            return;
        }
        if (player.pos().equals(pos())) {
            player.changeWorld(targetWorld);
            player.setPos(targetCoord);
        }
    }
}
