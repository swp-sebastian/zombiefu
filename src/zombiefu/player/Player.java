package zombiefu.player;

import jade.core.World;
import zombiefu.items.Weapon;
import jade.fov.RayCaster;
import jade.fov.ViewField;
import jade.ui.Camera;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import zombiefu.actor.Creature;
import zombiefu.exception.CanNotAffordException;
import zombiefu.exception.CannotMoveToIllegalFieldException;
import zombiefu.exception.MaximumHealthPointException;
import zombiefu.exception.WeaponHasNoMunitionException;
import zombiefu.exception.CannotBeConsumedException;
import zombiefu.exception.NoDirectionGivenException;
import zombiefu.exception.DoesNotPossessThisItemException;
import zombiefu.fov.ViewEverything;
import zombiefu.items.ConsumableItem;
import zombiefu.items.Item;
import zombiefu.level.Level;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;
import zombiefu.util.Action;
import zombiefu.util.ConfigHelper;

public class Player extends Creature implements Camera {

    private final static int ECTS_FOR_NEXT_SEMESTER = 30;
    private final static ViewField DEFAULT_VIEWFIELD = new RayCaster();
    private int dexterityValue;
    private int money;
    private int ects;
    private int semester;
    private int maximalHealthPoints;
    private HashMap<String, ArrayList<ConsumableItem>> inventar;
    private HashMap<String, Weapon> waffen;
    private ArrayList<String> waffenListe;

    public Player(ColoredChar face, String name, Discipline discipline, HashMap<Attribute, Integer> attr) {

        super(face, name, attr.get(Attribute.MAXHP), attr.get(Attribute.ATTACK), attr.get(Attribute.DEFENSE));

        this.maximalHealthPoints = attr.get(Attribute.MAXHP);
        this.dexterityValue = attr.get(Attribute.DEXTERITY);
        this.godMode = true;
        this.money = 0;
        this.ects = 0;
        this.semester = 1;
        this.discipline = discipline;

        this.inventar = new HashMap<String, ArrayList<ConsumableItem>>();
        this.waffen = new HashMap<String, Weapon>();
        this.waffenListe = new ArrayList<String>();

        this.sichtweite = 20;
        this.fov = DEFAULT_VIEWFIELD;
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

    public int getDexterity() {
        return dexterityValue;
    }

    public HashMap<String, ArrayList<ConsumableItem>> getInventar() {
        return inventar;
    }

    @Override
    public void act() {
        try {
            char key = ZombieGame.askPlayerForKey();

            // Diese Keys haben noch keine Property sie sind fürs Debuggen und
            // werden später abgeschaltet. FIX (macht sgs)
            if (key == 'f') {
                if (fov instanceof RayCaster) {
                    fov = new ViewEverything();
                } else {
                    fov = new RayCaster();
                }
                ZombieGame.refreshMainFrame();
                act();
            }

            if (key == 27) {
                System.exit(0);
            }

            if (key == 'g') {
                godMode = !godMode;
                ZombieGame.refreshBottomFrame();
                act();
            }

            Action action = ZombieTools.keyToAction(ZombieGame.getSettings().keybindings, key);

            if (action != null) {

                switch (action) {

                    case PREV_WEAPON:
                        switchWeapon(true);
                        ZombieGame.refreshBottomFrame();
                        act();
                        break;

                    case NEXT_WEAPON:
                        switchWeapon(false);
                        ZombieGame.refreshBottomFrame();
                        act();
                        break;

                    case INVENTORY:
                        String it = ZombieGame.askPlayerForItemInInventar();
                        if (it == null) {
                            act();
                        } else {
                            consumeItem(it);
                        }
                        break;

                    case ATTACK:
                        try {
                            attack();
                        } catch (NoDirectionGivenException ex) {
                            act();
                        }
                        break;

                    case HELP:
                        ZombieGame.showHelp();
                        act();
                        break;

                    case UP:
                        tryToMove(Direction.NORTH);
                        break;

                    case DOWN:
                        tryToMove(Direction.SOUTH);
                        break;

                    case LEFT:
                        tryToMove(Direction.WEST);
                        break;

                    case RIGHT:
                        tryToMove(Direction.EAST);
                        break;


                }
            } else {
                act();
            }
        } catch (CannotMoveToIllegalFieldException ex) {
            act();
        } catch (WeaponHasNoMunitionException ex) {
            ZombieGame.newMessage("Du hast keine Munition für " + getActiveWeapon().getName());
            act();
        }
    }

    public void switchWeapon(boolean backwards) {
        if (backwards) {
            String tmp = waffenListe.remove(waffen.size() - 1);
            waffenListe.add(0, tmp);
        } else {
            String tmp = waffenListe.remove(0);
            waffenListe.add(tmp);
        }
    }

    public void changeWorld(World world) {

        Guard.verifyState(world instanceof Level);
        Level lvl = (Level) world;

        if (bound()) {
            world().removeActor(this);
        }
        lvl.addActor(this);
        if (lvl == ZombieGame.getGlobalMap()) {
            fov = new ViewEverything();
        } else {
            fov = DEFAULT_VIEWFIELD;
            lvl.fillWithEnemies();
        }
    }

    public void obtainItem(Item i) {
        if (i instanceof Weapon) {
            Weapon w = (Weapon) i;
            if (waffenListe.contains(w.getName())) {
                waffen.get(w.getName()).addMunition(w.getMunition());
                w.expire();
            } else {
                waffen.put(w.getName(), w);
                waffenListe.add(w.getName());
            }
        } else if (i instanceof ConsumableItem) {
            ConsumableItem c = (ConsumableItem) i;
            if (!inventar.containsKey(i.getName())) {
                inventar.put(i.getName(), new ArrayList<ConsumableItem>());
            }
            inventar.get(i.getName()).add(c);
        } else {
            throw new IllegalStateException(
                    "Items should either be Weapons or consumable.");
        }
    }

    public ConsumableItem removeConsumableItemFromInventar(String s) throws DoesNotPossessThisItemException {
        if (inventar.containsKey(s) && !inventar.get(s).isEmpty()) {
            return inventar.get(s).remove(0);
        } else {
            throw new DoesNotPossessThisItemException();
        }
    }

    public Item removeItem(String itemName) throws DoesNotPossessThisItemException {
        if (waffenListe.contains(itemName)) {
            waffenListe.remove(itemName);
            return waffen.remove(itemName);
        } else {
            return removeConsumableItemFromInventar(itemName);
        }
    }

    @Override
    public Weapon getActiveWeapon() {
        return waffen.get(waffenListe.get(0));
    }

    public void heal(int i) throws MaximumHealthPointException {
        ZombieTools.log(getName() + " hat " + i + " HP geheilt. ");
        if (healthPoints == maximalHealthPoints) {
            throw new MaximumHealthPointException();
        }
        healthPoints += i;
        if (healthPoints >= maximalHealthPoints) {
            healthPoints = maximalHealthPoints;
        }
    }

    private void consumeItem(String itemName) {
        ZombieGame.newMessage("Du benutzt '" + itemName + "'.");
        ConsumableItem it;
        try {
            it = removeConsumableItemFromInventar(itemName);
            try {
                it.getConsumedBy(this);
                it.expire();
            } catch (CannotBeConsumedException ex) {
                obtainItem(it);
            }
        } catch (DoesNotPossessThisItemException ex) {
        }
        ZombieGame.refreshBottomFrame();
    }

    @Override
    public void killed(Creature killer) {
        ZombieGame.endGame();
    }

    public void addMoney(int m) {
        this.money += m;
    }

    public void pay(int m) throws CanNotAffordException {
        if (m > money) {
            throw new CanNotAffordException();
        } else {
            this.money -= m;
            ZombieGame.refreshBottomFrame();
        }

    }

    @Override
    protected Direction getAttackDirection() throws NoDirectionGivenException {
        return ZombieGame.askPlayerForDirection();
    }

    public void giveECTS(int e) {
        ects += e;

        while (ects >= semester * ECTS_FOR_NEXT_SEMESTER) {
            levelUp();
        }
    }

    public void levelUp() {
        semester += 1;
        ZombieGame.refreshBottomFrame();
        Attribute att = ZombieGame.askPlayerForAttrbuteToRaise();
        int step = att.getStep();
        switch (att) {
            case MAXHP:
                maximalHealthPoints += step;
                break;
            case ATTACK:
                attackValue += step;
                break;
            case DEFENSE:
                defenseValue += step;
                break;
            case DEXTERITY:
                dexterityValue += step;
                break;
            default:
                throw new AssertionError(att.name());
        }
        ZombieGame.refreshBottomFrame();
    }
}
