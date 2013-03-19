package zombiefu.actor;

import zombiefu.player.Player;
import zombiefu.level.Level;
import jade.core.Actor;
import jade.core.World;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.awt.Color;
import zombiefu.util.ConfigHelper;

public class Teleporter extends Actor {

    private String targetWorldName;
    private Coordinate targetCoord;

    public Teleporter(ColoredChar face, String w, Coordinate c) {
        super(face);
        this.targetWorldName = w;
        this.targetCoord = c;
    }

    public Teleporter(String w, Coordinate c) {
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
            player.changeWorld(ConfigHelper.getLevelByName(targetWorldName));
            player.setPos(targetCoord);
        }
    }
}
