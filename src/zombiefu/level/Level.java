package zombiefu.level;

import java.awt.Color;

import jade.core.Actor;
import jade.core.World;
import jade.gen.Generator;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import zombiefu.ui.ZombiePanel;
import zombiefu.creature.DozentZombie;
import zombiefu.creature.Player;
import zombiefu.creature.Zombie;
import zombiefu.items.Teleporter;
import zombiefu.map.RoomBuilder;

public class Level extends World {

    public Level(int width, int height, Generator gen) {
        super(width, height);
        gen.generate(this);
        fillWithEnemies();
    }
    
    public static Level levelFromFile(String file) {
        RoomBuilder builder = new RoomBuilder(file, "src/sources/CharSet.txt");
        System.out.println(builder.width() + " " + builder.height());
        return new Level(builder.width(), builder.height(), builder);
    }

    public void fillWithEnemies() {
        // Der Zombie-Dozent kommt hinzu.
        addActor(new DozentZombie());
        // 6 normale Zombies kommen hinzu
        for (int i = 0; i <= 5; i++) {
            addActor(new Zombie());
        }
    }

    public void refresh(ZombiePanel term) {
        term.clearBuffer();
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                term.bufferChar(x, y, look(x, y));
            }
        }
        term.bufferCameras();
        term.refreshScreen();
        tick();
    }
}
