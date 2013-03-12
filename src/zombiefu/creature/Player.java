package zombiefu.creature;

import jade.core.World;
import zombiefu.items.Waffe;
import jade.fov.RayCaster;
import jade.ui.Camera;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import java.util.ArrayList;
import zombiefu.fov.ViewEverything;
import zombiefu.items.ConsumableItem;
import zombiefu.items.Item;
import zombiefu.level.Level;
import zombiefu.util.NoDirectionGivenException;
import zombiefu.util.ZombieGame;

public class Player extends Creature implements Camera {

    private int intelligenceValue;
    private int money;
    private int ects;
    private int semester;
    private int maximalHealthPoints;
    private ArrayList<ConsumableItem> inventar;
    private ArrayList<Waffe> waffen;

    public Player(ColoredChar face, String name,
            int healthPoints, int attackValue, int defenseValue,
            int intelligenceValue, ArrayList<Waffe> w) {

        super(face, name, healthPoints, attackValue, defenseValue);

        this.maximalHealthPoints = healthPoints;
        this.intelligenceValue = intelligenceValue;
        this.godMode = true;
        this.money = 10;
        this.ects = 0;
        this.semester = 1;

        this.inventar = new ArrayList<ConsumableItem>();
        this.waffen = w;

        this.sichtweite = 20;
        this.fov = new RayCaster();
    }

    public int getSemester() {
        return semester;
    }

    public int getECTS() {
        return ects;
    }

    public int getMoney() {
        return money;
    }

    public int getMaximalHealthPoints() {
        return maximalHealthPoints;
    }

    public int getIntelligenceValue() {
        return intelligenceValue;
    }

    public ArrayList<ConsumableItem> getInventar() {
        return inventar;
    }

    @Override
    public void act() {
        try {
            char key;
            key = ZombieGame.askPlayerForKey();
            switch (key) {
                case 'q':
                    switchWeapon(true);
                    ZombieGame.refreshBottomFrame();
                    act();
                    break;
                case 'e':
                    switchWeapon(false);
                    ZombieGame.refreshBottomFrame();
                    act();
                    break;
                case 'f':
                    if (fov instanceof RayCaster) {
                        fov = new ViewEverything();
                    } else {
                        fov = new RayCaster();
                    }
                    ZombieGame.refreshMainFrame();
                    act();
                    break;
                case 'i':
                    ConsumableItem it = ZombieGame.askPlayerForItem();
                    if (it == null) {
                        act();
                    } else {
                        consumeItem(it);
                    }
                    break;
                case (char) 27:
                    System.exit(0);
                case 'g':
                    godMode = !godMode;
                    act();
                    break;
                case '\n':
                    try {
                        attack();
                    } catch (NoDirectionGivenException ex) {
                        act();
                    }
                    break;
                default:
                    Direction dir = Direction.keyToDir(key);
                    if (dir != null) {
                        tryToMove(dir);
                    } else {
                        act();
                    }
                    break;
            }
        } catch (InterruptedException e) {
        } catch (CannotMoveToImpassableFieldException ex) {
            act();
        }
    }

    public void switchWeapon(boolean backwards) {
        if (backwards) {
            Waffe tmp = waffen.remove(waffen.size() - 1);
            waffen.add(0, tmp);
        } else {
            Waffe tmp = waffen.remove(0);
            waffen.add(tmp);
        }
    }

    public void changeWorld(World world) {
        world().removeActor(this);
        world.addActor(this);
        ((Level) world).fillWithEnemies();
    }

    public void changeWorld(String level) {
        changeWorld(Level.levelFromFile(level));
    }

    public void toInventar(Item i) {
        if (i instanceof Waffe) {
            waffen.add((Waffe) i);
        } else if (i instanceof ConsumableItem) {
            inventar.add((ConsumableItem) i);
        } else {
            throw new IllegalStateException("Items should either be Weapons or consumable.");
        }
    }

    @Override
    public Waffe getActiveWeapon() {
        return waffen.get(0);
    }

    public void heal(int i) {
        System.out.print(getName() + " hat " + i + " HP geheilt. ");
        healthPoints += i;
        if (healthPoints >= maximalHealthPoints) {
            healthPoints = maximalHealthPoints;
        }
        System.out.println("HP: " + healthPoints);
    }

    private void consumeItem(ConsumableItem it) {
        ZombieGame.newMessage("Du benutzt '" + it.getName() + "'.");
        System.out.println(getName() + " benutzt Item " + it.getName());
        it.getConsumedBy(this);
        inventar.remove(it);
        it.expire();
        ZombieGame.refreshBottomFrame();
    }

    @Override
    protected void killed(Creature killer) {
        ZombieGame.endGame();
    }

    public void addMoney(int m) {
        this.money += m;
    }

    @Override
    protected Direction getAttackDirection() throws NoDirectionGivenException {
        return ZombieGame.askPlayerForDirection();
    }
}
