package zombiefu.level;

import jade.core.Actor;
import jade.core.World;
import jade.gen.Generator;
import jade.ui.TermPanel;
import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.creature.DozentZombie;
import zombiefu.creature.Player;
import zombiefu.creature.Zombie;
import zombiefu.items.Item;
import zombiefu.items.Nahrung;
import zombiefu.items.Waffe;
import zombiefu.map.RoomBuilder;

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

    @Override
    public void tick() {
        // Der Player führt IMMER die erste Aktion aus.
        try {
            super.getActor(Player.class).act();
        } catch (IllegalArgumentException e) {
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
        addActor(new Nahrung(ColoredChar.create('A', Color.decode("0x7D26CD")), "Apfel", 40));
        addActor(new Waffe("Kettensäge", 15, ColoredChar.create('K', Color.decode("0x7D26CD"))));
    }

    public void refresh(TermPanel term) {
        term.clearBuffer();
        // for (int x = 0; x < width(); x++) {
        //     for (int y = 0; y < height(); y++) {
        //         term.bufferChar(x, y, look(x, y));
        //     }
        // }
        term.bufferCameras();
        term.refreshScreen();
        tick();
    }
}
