package zombiefu.level;

import jade.core.Actor;
import jade.core.World;
import jade.gen.Generator;
import jade.ui.TermPanel;
import jade.util.datatype.ColoredChar;
import java.awt.Color;
import java.util.logging.Logger;
import zombiefu.creature.DozentZombie;
import zombiefu.creature.Player;
import zombiefu.creature.Zombie;
import zombiefu.items.Item;
import zombiefu.items.HealingItem;
import zombiefu.items.Waffe;
import zombiefu.map.RoomBuilder;
import zombiefu.util.TargetIsNotInThisWorldException;

public class Level extends World {
    
    public Level(int width, int height, Generator gen) {
        super(width, height);
        gen.generate(this);
        fillWithEnemies();
        fillWithItems();
    }

    public static Level levelFromFile(String file) {
        RoomBuilder builder = new RoomBuilder(file, "src/sources/CharSet.txt");
        return new Level(builder.width(), builder.height(), builder);
    }

    public Player getPlayer() throws TargetIsNotInThisWorldException {
        Player pl = super.getActor(Player.class);
        if(pl == null) {
            throw new TargetIsNotInThisWorldException();
        }
        return pl;
    }
    
    @Override
    public void tick() {
        // Der Player führt IMMER die erste Aktion aus.
        try {
            getPlayer().act();
        } catch (TargetIsNotInThisWorldException ex) {
        }
        for (Class<? extends Actor> cls : super.getActOrder()) {
            for (Actor actor : getActors(cls)) {
                if (!(actor instanceof Player)) {
                    actor.act();
                }
            }
        }
        removeExpired();
    }

    public void fillWithEnemies() {
        // Der Zombie-Dozent kommt hinzu.
        addActor(new DozentZombie());
        // 6 normale Zombies kommen hinzu
        for (int i = 0; i <= 5; i++) {
            addActor(new Zombie());
        }
    }

    private void fillWithItems() {
        addActor(new HealingItem(ColoredChar.create('☕', new Color(80, 0, 0)), "Kaffee", 40));
        addActor(new Waffe(ColoredChar.create('⚩', new Color(90, 90, 90)), "Kettensäge", 15));
    }

    public void refresh(TermPanel term) {
        term.clearBuffer();
        term.bufferCameras();
        term.refreshScreen();
    }
}
