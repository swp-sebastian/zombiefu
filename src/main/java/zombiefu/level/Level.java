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
import zombiefu.builder.RandomEnemyGenerator;
import zombiefu.builder.RandomItemGenerator;
import zombiefu.fight.DamageAnimation;
import zombiefu.exception.TargetIsNotInThisWorldException;
import zombiefu.human.Human;
import zombiefu.items.ConsumableItem;
import zombiefu.items.Food;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public class Level extends World {

    private static final double ENEMY_BASE_DENSITY = 0.015;
    private static final double ITEM_BASE_DENSITY = 0.008;
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

        int commonItems = (int) Math.ceil(n * 0.7);
        for (int i = 0; i < commonItems; i++) {
            addActor(RandomItemGenerator.GOOD_ITEM.getRandomItem());
        }
        n -= commonItems;

        int advancedItems = (int) Math.ceil(n * 0.7);
        for (int i = 0; i < advancedItems; i++) {
            addActor(RandomItemGenerator.COMMON_ITEM.getRandomItem());
        }
        n -= advancedItems;

        for (int i = 0; i < n; i++) {
            addActor(RandomItemGenerator.RARE_ITEM.getRandomItem());
        }
    }

    private void fillWithEnemies() {
        int n = (int) ((ZombieGame.getPlayer().getSemester() + 9) * 0.1 * ENEMY_BASE_DENSITY * getNumberOfPassableFields() * ZombieTools.getRandomDouble(0.85, 1.15));
        n -= getActors(Monster.class).size();

        if (n <= 0) {
            return;
        }

        int commonZombies = (int) Math.ceil(n * 0.8);
        for (int i = 0; i < commonZombies; i++) {
            addActor(RandomEnemyGenerator.COMMON.getRandomMonster());
        }
        n -= commonZombies;

        int advancedZombies = (int) Math.ceil(n * 0.8);
        for (int i = 0; i < advancedZombies; i++) {
            addActor(RandomEnemyGenerator.ADVANCED.getRandomMonster());
        }
        n -= advancedZombies;

        for (int i = 0; i < advancedZombies; i++) {
            addActor(RandomEnemyGenerator.MASTER.getRandomMonster());
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