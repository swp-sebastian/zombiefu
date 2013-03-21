package zombiefu.items;

import jade.util.datatype.ColoredChar;
import zombiefu.actor.PassableActor;
import zombiefu.player.Player;
import zombiefu.level.Level;
import zombiefu.util.ZombieGame;

public abstract class Item extends PassableActor {

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

    protected void pickedUpByPlayer(Player player) {
            ZombieGame.newMessage("Du hast '" + getName() + "' aufgehoben.");
            player.obtainItem(this);        
    }
    
    @Override
    public void act() {
        Level world = (Level) world();
        Player player = world.getActor(Player.class);
        if (player == null) {
            return;
        }
        if (player.pos().equals(pos())) {
            world.removeActor(this);
            pickedUpByPlayer(player);
        }
    }
}
