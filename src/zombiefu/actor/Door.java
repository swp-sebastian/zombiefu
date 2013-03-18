package zombiefu.actor;

import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.util.ZombieGame;

public class Door extends NotPassableActor {

    private final String name;

    public Door(String name) {
        super(ColoredChar.create('\u25D8', Color.yellow));
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void act() {
    }

    public void open() {
        world().removeActor(this);
        expire();
        ZombieGame.refreshMainFrame();
        ZombieGame.newMessage("Du hast die Tür " + getName() + " aufgeschlossen.");
    }
}
