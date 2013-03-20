package zombiefu.level;

import jade.core.Actor;
import jade.core.World;
import jade.gen.Generator;
import jade.ui.TermPanel;
import jade.util.Dice;
import java.util.ArrayList;
import zombiefu.actor.Monster;
import zombiefu.player.Player;
import zombiefu.actor.Door;
import zombiefu.items.Item;
import zombiefu.actor.Teleporter;
import zombiefu.fight.DamageAnimation;
import zombiefu.exception.TargetIsNotInThisWorldException;
import zombiefu.human.Human;
import zombiefu.util.ConfigHelper;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public class Level extends World {

    private static final double ENEMY_BASE_DENSITY = 0.015;
    private int numberOfPassableFields;
    private String name;
    private boolean global;

    public Level(int width, int height, Generator gen, String name, boolean global) {
        super(width, height);
        gen.generate(this);

        this.name = name;
        this.global = global;

        drawOrder = new ArrayList<>();
        drawOrder.add(DamageAnimation.class);
        drawOrder.add(Player.class);
        drawOrder.add(Monster.class);
        drawOrder.add(Human.class);
        drawOrder.add(Item.class);
        drawOrder.add(Door.class);
        drawOrder.add(Teleporter.class);
        drawOrder.add(Actor.class);

    }

    public String getName() {
        return name;
    }

    public boolean isGlobalMap() {
        return global;
    }

    public Player getPlayer() throws TargetIsNotInThisWorldException {
        Player pl = super.getActor(Player.class);
        if (pl == null) {
            throw new TargetIsNotInThisWorldException();
        }
        return pl;
    }

    private int getNumberOfPassableFields() {
        if (numberOfPassableFields == 0) {
            numberOfPassableFields = 0;
            for (int x = 0; x < width(); x++) {
                for (int y = 0; y < height(); y++) {
                    if (passableAt(x, y)) {
                        numberOfPassableFields ++;
                    }
                }
            }
        }
        return numberOfPassableFields;
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

    private void fillWithItems() {
    }

    private void fillWithEnemies() {
        int n = (int) ((ZombieGame.getPlayer().getSemester() + 9) * 0.1 * ENEMY_BASE_DENSITY * getNumberOfPassableFields() * ZombieTools.getRandomDouble(0.85, 1.15));
        
        // Normale Zombies
        int k = n / 2;
        for (int i = 0; i < k; i++) {
            addActor(ConfigHelper.newMonsterByName("Zombie"));        
            n--;            
        }
            addActor(ConfigHelper.newMonsterByName("RaketenwerferZombie"));  
        
        
        
    }

    public void refill() {
        for (Monster a : getActors(Monster.class)) {
            removeActor(a);
        }
        for (Item a : getActors(Item.class)) {
            removeActor(a);
        }
        fillWithEnemies();
        fillWithItems();
    }

    public void refresh(TermPanel term) {
        term.clearBuffer();
        term.bufferCameras();
        term.refreshScreen();
    }
}