package zombiefu.items;

import jade.core.Actor;
import jade.util.datatype.ColoredChar;
import zombiefu.creature.Creature;
import zombiefu.creature.Player;
import zombiefu.level.Level;

public abstract class Item extends Actor {

    protected String name;

    public Item(ColoredChar face, String s) {
        super(face);
        this.name = s;
    }

    public Item(ColoredChar face) {
        this(face, "Unbekannter Gegenstand");
    }

    public String getName() {
        return name;
    }

    @Override
    public void act() {
        Level world = (Level) world();
        Player player = world.getActor(Player.class);
        if (player == null) {
            return;
        }
        if (player.pos().equals(pos())) {
            // Player picks up item
            System.out.println(player.getName() + " hat " + getName() + " gefunden.");
            world.removeActor(this);
            player.toInventar(this);
        }
    }
}
