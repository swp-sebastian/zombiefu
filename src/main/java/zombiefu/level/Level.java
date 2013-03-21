package zombiefu.level;

import jade.core.Actor;
import jade.core.World;
import jade.gen.Generator;
import jade.ui.TermPanel;
import java.util.ArrayList;
import zombiefu.creature.Monster;
import zombiefu.player.Player;
import zombiefu.actor.Door;
import zombiefu.items.Item;
import zombiefu.actor.Teleporter;
import zombiefu.builder.RandomEnemyClass;
import zombiefu.builder.RandomItemClass;
import zombiefu.fight.DamageAnimation;
import zombiefu.exception.TargetIsNotInThisWorldException;
import zombiefu.human.Human;
import zombiefu.items.ConsumableItem;
import zombiefu.items.Food;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public class Level extends World {

    private static final double ENEMY_BASE_DENSITY = 0.008;
    private static final double ITEM_BASE_DENSITY = 0.008;
    private int numberOfPassableFields;
    private String name;
    private boolean fullView;
    private boolean hasEnemies;

    public Level(int width, int height, Generator gen, String name, boolean fullView, boolean hasEnemies) {
        super(width, height);
        gen.generate(this);

        this.name = name;
        this.fullView = fullView;
        this.hasEnemies = hasEnemies;

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

    public boolean hasFullView() {
        return fullView;
    }

    public boolean hasEnemies() {
        return hasEnemies;
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
                        numberOfPassableFields++;
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
        int n = (int) ((ZombieGame.getPlayer().getSemester() + 9) * 0.1 * ITEM_BASE_DENSITY * getNumberOfPassableFields() * ZombieTools.getRandomDouble(0.85, 1.15));
        n -= getActors(Food.class).size();
        n -= getActors(ConsumableItem.class).size();
        if (n <= 0) {
            return;
        }

        ZombieTools.log("fillWithItems(): Items to be spawned: " + n);
        
        int commonItems = (int) Math.ceil(n * 0.8);
        n -= commonItems;
        int advancedItems = (int) Math.ceil(n * 0.8);
        n -= advancedItems;
        int awesomeItems = n;
        n -= awesomeItems;
        
        ZombieTools.log("fillWithItems(): COMMON-Items to be spawned: " + commonItems);
        ZombieTools.log("fillWithItems(): ADVANCE-Items to be spawned: " + advancedItems);
        ZombieTools.log("fillWithItems(): AWESOME-Items to be spawned: " + awesomeItems);
        
        for (int i = 0; i < commonItems; i++) {
            addActor(RandomItemClass.GOOD.newRandomItem());
        }

        for (int i = 0; i < advancedItems; i++) {
            addActor(RandomItemClass.COMMON.newRandomItem());
        }

        for (int i = 0; i < awesomeItems; i++) {
            addActor(RandomItemClass.AWESOME.newRandomItem());
        }
    }

    private void fillWithEnemies() {
        int n = (int) ((ZombieGame.getPlayer().getSemester() + 9) * 0.1 * ENEMY_BASE_DENSITY * getNumberOfPassableFields() * ZombieTools.getRandomDouble(0.85, 1.15));
        n -= getActors(Monster.class).size();

        if (n <= 0) {
            return;
        }
        
        ZombieTools.log("fillWithEnemies(): Zombies to be spawned: " + n);        

        int commonZombies = (int) Math.ceil(n * 0.7);
        n -= commonZombies;
        int poorZombies = (int) Math.ceil(n * 0.25);
        n -= poorZombies;
        int advancedZombies = n;
        
        ZombieTools.log("fillWithEnemies(): COMMON-Zombies to be spawned: " + commonZombies);
        ZombieTools.log("fillWithEnemies(): POOR-Zombies to be spawned: " + poorZombies);
        ZombieTools.log("fillWithEnemies(): ADVANCED-Zombies to be spawned: " + advancedZombies);
        
        for (int i = 0; i < commonZombies; i++) {
            addActor(RandomEnemyClass.COMMON.getRandomMonster());
        }
        
        for (int i = 0; i < poorZombies; i++) {
            addActor(RandomEnemyClass.POOR.getRandomMonster());
        }

        for (int i = 0; i < advancedZombies; i++) {
            addActor(RandomEnemyClass.ADVANCED.getRandomMonster());
        }
    }

    public void refill() {
        fillWithEnemies();
        fillWithItems();
    }

    public void refresh(TermPanel term) {
        term.clearBuffer();
        term.bufferCameras();
        term.refreshScreen();
    }
}