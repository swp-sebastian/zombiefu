package zombiefu.level;

import jade.core.Actor;
import jade.core.World;
import jade.gen.Generator;
import jade.ui.TermPanel;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Logger;
import zombiefu.creature.Monster;
import zombiefu.creature.Player;
import zombiefu.creature.Shop;
import zombiefu.creature.Zombie;
import zombiefu.creature.Door;
import zombiefu.items.Item;
import zombiefu.items.HealingItem;
import zombiefu.items.Teleporter;
import zombiefu.items.Waffe;
import zombiefu.map.RoomBuilder;
import zombiefu.util.DamageAnimation;
import zombiefu.exception.TargetIsNotInThisWorldException;
import zombiefu.util.ConfigHelper;
import zombiefu.util.ZombieGame;

public class Level extends World {

    private int numberOfPassableFields;
    private String name;

    public Level(int width, int height, Generator gen, String name) {
        super(width, height);
        gen.generate(this);
        fillWithItems();
        calculateNumberOfPassableFields();
        
        this.name = name;

        drawOrder = new ArrayList<Class<? extends Actor>>();
        drawOrder.add(DamageAnimation.class);
        drawOrder.add(Player.class);
        drawOrder.add(Monster.class);
        drawOrder.add(Shop.class);
        drawOrder.add(Item.class);
        drawOrder.add(Door.class);
        drawOrder.add(Teleporter.class);
        
    }
    
    public String getName() {
        return name;
    }

    public Player getPlayer() throws TargetIsNotInThisWorldException {
        Player pl = super.getActor(Player.class);
        if (pl == null) {
            throw new TargetIsNotInThisWorldException();
        }
        return pl;
    }

    private void calculateNumberOfPassableFields() {
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                numberOfPassableFields += passableAt(x, y) ? 1 : 0;
            }
        }
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
                if (!(actor instanceof Player) && !actor.expired()) {
                    actor.act();
                }
            }
        }
        removeExpired();
    }

    public void fillWithEnemies() {
        int oldEnemies = getActors(Monster.class).size();
        int semester = ZombieGame.getPlayer().getSemester();
        int newEnemies = (int) (semester * 0.005 * numberOfPassableFields * Dice.global.nextInt(40, 60) / 50);
        // 6 normale Zombies kommen hinzu
        for (int i = oldEnemies; i <= newEnemies; i++) {
            addActor(new Zombie());
        }
    }

    private void fillWithItems() {
        addActor(ConfigHelper.newItemByName("Kaffee"));
        addActor(ConfigHelper.newItemByName("Kettensäge"));
    }

    public void refresh(TermPanel term) {
        term.clearBuffer();
        term.bufferCameras();
        term.refreshScreen();
    }
}
