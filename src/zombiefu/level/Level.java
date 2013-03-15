package zombiefu.level;

import jade.core.Actor;
import jade.core.World;
import jade.gen.Generator;
import jade.ui.TermPanel;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import java.util.ArrayList;
import zombiefu.monster.Monster;
import zombiefu.player.Player;
import zombiefu.human.Shop;
import zombiefu.monster.Zombie;
import zombiefu.actor.Door;
import zombiefu.items.Item;
import zombiefu.actor.Teleporter;
import zombiefu.util.DamageAnimation;
import zombiefu.exception.TargetIsNotInThisWorldException;
import zombiefu.human.Human;
import zombiefu.human.ItemGivingHuman;
import zombiefu.human.TalkingHuman;
import zombiefu.items.MensaCard;
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
        drawOrder.add(Human.class);
        drawOrder.add(Item.class);
        drawOrder.add(Door.class);
        drawOrder.add(Teleporter.class);
        drawOrder.add(Actor.class);

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
           // addActor(new Zombie());
        }
        ZombieGame.getPlayer().obtainItem(ConfigHelper.newItemByName("Mate"));
        addActor(new ItemGivingHuman(ColoredChar.create('='), "Joachim", "Hallo, schenke!", new MensaCard(30), "Mate"));
    }

    protected void fillWithItems() {
    }

    public void refresh(TermPanel term) {
        term.clearBuffer();
        term.bufferCameras();
        term.refreshScreen();
    }
}
