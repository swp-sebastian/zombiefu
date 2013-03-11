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
import zombiefu.items.Item;
import zombiefu.level.Level;
import zombiefu.ui.ZombieFrame;

public class Player extends Creature implements Camera {

    private ZombieFrame frame;
    private ViewField fov;
    private int intelligenceValue;
    private int money;
    private int ects;
    private int semester;
    private int maximalHealthPoints;
    private List<Item> inventar;
    private List<Waffe> waffen;

    public Player(ZombieFrame frame, ColoredChar face, String name,
            int healthPoints, int attackValue, int defenseValue,
            int intelligenceValue, Waffe w) {

        super(face, name, healthPoints, attackValue, defenseValue);

        this.maximalHealthPoints = healthPoints;
        this.intelligenceValue = intelligenceValue;
        this.frame = frame;
        this.godMode = false;
        this.money = 10;
        this.ects = 0;
        this.semester = 1;

        this.inventar = new ArrayList<>();
        this.waffen = new ArrayList<>();
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
                    return;
                case 'e':
                    switchWeapon(false);
                    refreshStats();
                    act();
                    return;
                case 'x':
                    roundHouseKick();
                    break;
                default:
                    Direction dir = Direction.keyToDir(key);
                    if (dir != null) {
                        tryToMove(dir);
                    }
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void switchWeapon(boolean backwards) {
        if (backwards) {
            Waffe tmp = waffen.remove(waffen.size()-1);
            waffen.add(0,tmp);
        } else {
            Waffe tmp = waffen.remove(0);
            waffen.add(tmp);
        }
    }

    @Override
    public Collection<Coordinate> getViewField() {
        return fov.getViewField(world(), pos(), 5);
    }

    public void changeWorld(World world) {
        world().removeActor(this);
        world.addActor(this);
    }

    public void changeWorld(String level) {
        changeWorld(Level.levelFromFile(level));
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
        } else {
            inventar.add(i);
        }
    }

    @Override
    public Waffe getActiveWeapon() {
        return waffen.get(0);
    }
}
