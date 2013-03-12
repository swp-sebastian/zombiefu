package zombiefu.creature;

import jade.core.World;
import java.awt.Color;
import java.util.Collection;

import zombiefu.items.Waffe;
import jade.fov.RayCaster;
import jade.fov.ViewField;
import jade.ui.Camera;
import jade.ui.TermPanel;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import zombiefu.fov.ViewEverything;
import zombiefu.items.ConsumableItem;
import zombiefu.items.Item;
import zombiefu.level.Level;
import zombiefu.ui.ZombieFrame;
import zombiefu.util.Screen;
import zombiefu.util.ZombieTools;

public class Player extends Creature implements Camera {

    public ZombieFrame frame;
    private ViewField fov;
    private int intelligenceValue;
    private int money;
    private int ects;
    private int semester;
    private int maximalHealthPoints;
    private ArrayList<ConsumableItem> inventar;
    private ArrayList<Waffe> waffen;

    public Player(ZombieFrame frame, ColoredChar face, String name,
            int healthPoints, int attackValue, int defenseValue,
            int intelligenceValue, Waffe w) {

        super(face, name, healthPoints, attackValue, defenseValue);

        this.maximalHealthPoints = healthPoints;
        this.intelligenceValue = intelligenceValue;
        this.frame = frame;
        this.godMode = true;
        this.money = 10;
        this.ects = 0;
        this.semester = 1;

        this.inventar = new ArrayList<ConsumableItem>();
        this.waffen = new ArrayList<Waffe>();
        waffen.add(w);

        fov = new RayCaster();
    }

    @Override
    public void act() {
        try {
            char key;
            key = frame.mainTerm().getKey();
            switch (key) {
                case 'q':
                    switchWeapon(true);
                    refreshStats();
                    act();
                    break;
                case 'e':
                    switchWeapon(false);
                    refreshStats();
                    act();
                    break;
                case 'f':
                    if (fov instanceof RayCaster) {
                        fov = new ViewEverything();
                    } else {
                        fov = new RayCaster();
                    }
                    refreshWorld();
                    act();
                    break;
                case 'i':
                    ConsumableItem it = chooseItem();
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
                    roundHouseKick();
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

    @Override
    public Collection<Coordinate> getViewField() {
        return fov.getViewField(world(), pos(), 100);
    }

    public void changeWorld(World world) {
        world().removeActor(this);
        world.addActor(this);
    }

    public void changeWorld(String level) {
        changeWorld(Level.levelFromFile(level));
    }

    public void refreshWorld() {
        ((Level) world()).refresh(frame.mainTerm());
    }

    public void refreshStats() {
        frame.bottomTerm().clearBuffer();
        frame.bottomTerm().bufferString(0, 0, "Waffe: " + getActiveWeapon().getName()
                + " (" + getActiveWeapon().getDamage() + ") "
                + " | HP: " + healthPoints + "/" + maximalHealthPoints
                + " | A: " + attackValue
                + " | D: " + defenseValue
                + " | I: " + intelligenceValue);
        frame.bottomTerm().bufferString(0, 1, "Pi-Gebäude"
                + " | $ " + money
                + " | ECTS " + ects
                + " | Sem " + semester);
        frame.bottomTerm().bufferCameras();
        frame.bottomTerm().refreshScreen();
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

    private ConsumableItem chooseItem() {
        ConsumableItem output = null;
        if (inventar.isEmpty()) {
            ZombieTools.sendMessage("Inventar ist leer.", frame);
            return null;
        }
        frame.mainTerm().clearBuffer();
        frame.mainTerm().bufferString(0, 0, "Inventarliste:");
        for (int i = 0; i < inventar.size(); i++) {
            Item it = inventar.get(i);
            frame.mainTerm().bufferString(0, 2 + i, "[" + ((char) (97 + i)) + "] " + it.face() + " - " + it.getName());
        }
        frame.mainTerm().refreshScreen();
        try {
            int key = ((int) frame.mainTerm().getKey()) - 97;
            if (key >= 0 && key <= 25 && key < inventar.size()) {
                output = inventar.get(key);
            }
        } catch (InterruptedException ex) {
        }
        refreshWorld();
        return output;
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
        ZombieTools.sendMessage("Du benutzt '" + it.getName() + "'.", frame);
        System.out.println(getName() + " benutzt Item " + it.getName());
        it.getConsumedBy(this);
        inventar.remove(it);
        it.expire();
        refreshStats();
    }

    @Override
    protected void killed(Creature killer) {
        // TODO: Im Endscreen dynamisch Informationen anzeigen.
        try {
            Screen.showImage(frame.mainTerm(), "src/sources/endscreen.txt");
        } catch (InterruptedException ex) {
            Logger.getLogger(Player.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

    public void addMoney(int m) {
        this.money += m;
    }
}
