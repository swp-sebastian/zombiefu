/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.util;

import jade.core.Actor;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import jade.util.Guard;
import jade.ui.TermPanel;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import zombiefu.exception.NoDirectionGivenException;
import zombiefu.player.Player;
import zombiefu.builder.ItemBuilder;
import zombiefu.items.ConsumableItem;
import zombiefu.items.Item;
import zombiefu.level.Level;
import zombiefu.player.Attribute;
import zombiefu.player.Discipline;
import zombiefu.ui.ZombieFrame;
import zombiefu.ZombieFU;
import zombiefu.creature.AttributeSet;
import zombiefu.human.ShopInventar;

/**
 *
 * @author tomas
 */
public class ZombieGame {

    private static ZombieSettings settings;
    private static ZombieFrame frame;
    private static Player player;

    public static void createGame(String[] args, String name) {
        settings = new ZombieSettings(args);
        frame = new ZombieFrame(name);
    }

    public static ZombieSettings getSettings() {
        return settings;
    }

    public static char showStaticImage(String file) {
        try { //bevor fortgefahren wird
            ColoredChar[][] start = ConfigHelper.getImage(file);
            frame.mainTerm().clearBuffer();
            for (int x = 0; x < frame.mainTerm().DEFAULT_COLS; x++) {
                for (int y = 0; y < frame.mainTerm().DEFAULT_ROWS; y++) {
                    if (y >= start.length || x >= start[0].length) {
                        frame.mainTerm().bufferChar(x, y,
                                ColoredChar.create(' '));
                    } else {
                        frame.mainTerm().bufferChar(x, y, start[y][x]);
                    }
                }
            }
            frame.mainTerm().refreshScreen();
        } catch (IOException ex) {
        }

        return askPlayerForKey();
    }

    public static void initialize() {
        Discipline discipline = askPlayerForDiscipline();

        // Attribute laden.
        AttributeSet atts = new AttributeSet();
        for (Attribute att : Attribute.values()) {
            Integer setting = settings.playerAttributes.get(att);
            if (setting == null) {
                atts.put(att, discipline.getBaseAttribute(att));
            } else {
                atts.put(att, setting);
            }
        }

        // Spieler erzeugen
        player = new Player(settings.playerChar, settings.playerName, discipline, atts);

        // StartItems erzeugen
        player.obtainItem(ConfigHelper.newWeaponByName("Faust"));
        player.obtainItem(ConfigHelper.newFoodByName("Wasser"));
        Set<Actor> items = new ITMString(settings.playerInventar == null ? discipline.getItems() : settings.playerInventar).getActorSet();
        for (Actor a : items) {
            Guard.verifyState(a instanceof Item);
            player.obtainItem((Item) a);
        }

        // Gib Spieler Geld
        player.addMoney(discipline.getMoney());

        // Setze Spieler in Welt
        player.changeWorld(ConfigHelper.getLevelByName(settings.playerStartMap));
        if (player.world().insideBounds(settings.playerStartCoord)) {
            player.setPos(settings.playerStartCoord);
        }

        frame.mainTerm().registerCamera(player, 40, 17);
    }

    public static void setTopFrameContent(String s) {
        frame.topTerm().clearBuffer();
        if (s != null) {
            frame.topTerm().bufferString(0, 0, s);
        }
        frame.topTerm().refreshScreen();
    }

    public static char askPlayerForKeyWithMessage(String s) {
        refreshMainFrame();
        setTopFrameContent(s);
        char key = 0;
        try {
            key = frame.mainTerm().getKey();
        } catch (InterruptedException ex) {
            Guard.verifyState(false);
        }

        setTopFrameContent(null);
        return key;
    }

    public static void newMessage(String s) {
        askPlayerForKeyWithMessage(s);
    }

    public static void refreshMainFrame() {
        frame.mainTerm().clearBuffer();
        frame.mainTerm().bufferCameras();
        frame.mainTerm().refreshScreen();
    }

    public static void refreshBottomFrame() {
        frame.bottomTerm().clearBuffer();
        String firstLine = "Waffe: " + player.getActiveWeapon().getName() + " ("
                + player.getActiveWeapon().getMunitionToString()
                + " / " + player.getActiveWeapon().getDamage() + ")"
                + " | HP: " + player.getHealthPoints() + "/"
                + player.getAttribute(Attribute.MAXHP) + " | A: "
                + player.getAttribute(Attribute.ATTACK) + " | V: "
                + player.getAttribute(Attribute.DEFENSE) + " | G: "
                + player.getAttribute(Attribute.DEXTERITY);
        String secondLine = "Ort: " + ((Level) player.world()).getName();
        if (ZombieGame.settings.debug) {
            secondLine += " (" + player.pos().x() + "|" + player.pos().y() + ")";
        }
        secondLine += " | € " + ZombieTools.getMoneyString(player.getMoney(), false) + " | ECTS "
                + player.getECTS() + " | Sem " + player.getSemester();
        if (ZombieGame.settings.debug) {
            secondLine += " | GodMode: " + (player.isGod() ? "an" : "aus");
        }
        if (player.isDazed()) {
            secondLine += " | BETÄUBT!";
        }
        frame.bottomTerm().bufferString(0, 0, firstLine);
        frame.bottomTerm().bufferString(0, 1, secondLine);
        frame.bottomTerm().bufferCameras();
        frame.bottomTerm().refreshScreen();
    }

    public static void startGame() {
        while (!player.expired()) {
            refreshMainFrame();
            refreshBottomFrame();
            player.world().tick();
        }
        System.exit(0);
    }

    public static void showHelp() {
        showStaticImage("help");
        refreshMainFrame();
    }

    public static Player getPlayer() {
        return player;
    }

    public static char askPlayerForKey() {
        try {
            return frame.mainTerm().getKey();
        } catch (InterruptedException ex) {
            Guard.verifyState(false);
            return 0;
        }
    }

    public static Direction askPlayerForDirection()
            throws NoDirectionGivenException {
        setTopFrameContent("Bitte gib die Richtung an.");
        Direction d = null;
        try {
            d = Direction.keyToDir(frame.mainTerm().getKey());
        } catch (InterruptedException ex) {
        }
        setTopFrameContent(null);
        if (d == null || d == Direction.ORIGIN) {
            throw new NoDirectionGivenException();
        }
        return d;
    }

    // When order doesn't matter use a Set
    public static <K> K genericSelect(Map<K, String> map, boolean exitable, String... prompt) {
        return genericSelect(Collections.list(Collections.enumeration(map.entrySet())), exitable, prompt);
    }

    // A Esponda-esque method
    public static <K> K genericSelect(List<Entry<K, String>> xs, boolean exitable, String... prompt) {
        if (xs.size() == 0) {
            Guard.verifyState(exitable);
            return null;
        }

        int endOfScreen = frame.rows - 1;
        TermPanel f = frame.mainTerm();
        int position = 0;
        int page = 0;
        Action action = null;

        // Soviel Platz haben wir für Zeilen mit Abstand eine Zeile zwischen options
        int pageSize = (frame.rows - prompt.length - 1) / 2;

        // List von Listen fürs Paging
        ArrayList<List<Entry<K, String>>> paged = new ArrayList();

        for (int i = 0; i < xs.size(); i += pageSize) {
            if ((i + pageSize) < xs.size()) {
                paged.add(i / pageSize, xs.subList(i, i + pageSize));
            } else {
                paged.add(i / pageSize, xs.subList(i, xs.size()));
            }
        }

        do {
            f.clearBuffer();

            int drawOffset = 1;
            if (exitable) {
                String msg = "Verlassen mit q";
                f.bufferString(frame.columns - msg.length(), drawOffset, msg);
            }

            for (String line : prompt) {
                f.bufferString(2, drawOffset, line);
                drawOffset += 1;
            }


            // One line of padding between prompt and options.
            drawOffset += 1;

            // Check position sanity.
            if (position < 0) {
                position = 0;
            }
            if (position == xs.size()) {
                position = xs.size() - 1;
            }

            // Calculate current page
            page = position / pageSize;

            ZombieTools.log("Position: " + position + " Page: " + page);

            // Draw options
            for (int i = 0; i < paged.get(page).size(); i++) {
                if (position == (i + (page * pageSize))) {
                    f.bufferString(2, drawOffset + 2 * i, "[x] " + paged.get(page).get(i).getValue());
                } else {
                    f.bufferString(2, drawOffset + 2 * i, "[ ] " + paged.get(page).get(i).getValue());
                }
            }


            frame.mainTerm().refreshScreen();
            action = ZombieTools.keyToAction(ZombieGame.getSettings().keybindings, ZombieGame.askPlayerForKey());

            if (action != null) {

                switch (action) {

                    case UP:
                        position--;
                        break;

                    case DOWN:
                        position++;
                        break;

                    case PREV_WEAPON:
                        if (exitable) {
                            refreshMainFrame();
                            return null;
                        }
                        break;
                }
            }

        } while (action != Action.ATTACK);

        refreshMainFrame();
        return paged.get(page).get(position - (page * pageSize)).getKey();
    }

    public static String askPlayerForItemInInventar() {
        Map<String, ArrayList<ConsumableItem>> inventar = getPlayer().getInventar();

        ArrayList<Entry<String, String>> items = new ArrayList<Entry<String, String>>();
        for (String s : inventar.keySet()) {
            String pluralized;
            int i = inventar.get(s).size();
            if (i == 0) continue;
            if (i == 1) {
                pluralized = inventar.get(s).get(0).face() + " " + s;
            } else {
                pluralized = inventar.get(s).get(0).face() + " " + s + " (" + i + "x)";
            }
            items.add(new AbstractMap.SimpleEntry(s, pluralized));
        }

        if (inventar.isEmpty() || (items.size() == 0)) {
            ZombieGame.newMessage("Inventar ist leer.");
            return null;
        }

        return genericSelect(items, true, "Item auswählen:");
    }

    public static ItemBuilder askPlayerForItemToBuy(ShopInventar inventar) {

        if (inventar.isEmpty()) {
            ZombieGame.newMessage("Dieser Shop hat keine Artikel.");
            return null;
        }

        ArrayList<ItemBuilder> itemSet = inventar.asList();
        Collections.sort(itemSet, new Comparator<ItemBuilder>() {
            @Override
            public int compare(ItemBuilder t, ItemBuilder t1) {
                return t.getName().compareTo(t1.getName());
            }
        });

        ArrayList<Entry<ItemBuilder, String>> items = new ArrayList<Entry<ItemBuilder, String>>();

        for (ItemBuilder it : itemSet) {
            items.add(new AbstractMap.SimpleEntry(it, it.getName() + " (Preis: " + ZombieTools.getMoneyString(inventar.get(it)) + ")"));
        }

        return genericSelect(items, true, "Artikel: ");
    }

    public static Discipline askPlayerForDiscipline() {
        ArrayList<Entry<Discipline, String>> disciplines = new ArrayList<>();

        // Because order matters here we use a List which keeps its order.
        disciplines.add(new AbstractMap.SimpleEntry(Discipline.BUSINESS, "BWL"));
        disciplines.add(new AbstractMap.SimpleEntry(Discipline.CHEMISTRY, "Chemie"));
        disciplines.add(new AbstractMap.SimpleEntry(Discipline.COMPUTER_SCIENCE, "Informatik"));
        disciplines.add(new AbstractMap.SimpleEntry(Discipline.MATHEMATICS, "Mathematik"));
        disciplines.add(new AbstractMap.SimpleEntry(Discipline.MEDICINE, "Medizin"));
        disciplines.add(new AbstractMap.SimpleEntry(Discipline.PHILOSOPHY, "Philosophie"));
        disciplines.add(new AbstractMap.SimpleEntry(Discipline.POLITICAL_SCIENCE, "Politikwissenschaft"));
        disciplines.add(new AbstractMap.SimpleEntry(Discipline.PHYSICS, "Physik"));
        disciplines.add(new AbstractMap.SimpleEntry(Discipline.SPORTS, "Sportwissenschaften"));

        Discipline output = genericSelect(disciplines, false, "Wähle deinen Studiengang!");
        Guard.argumentIsNotNull(output);
        return output;
    }

    public static Attribute askPlayerForAttrbuteToRaise() {
        ArrayList<Entry<Attribute, String>> attributes = new ArrayList<Entry<Attribute, String>>();

        // Because order matters here we use a List which keeps its order.
        attributes.add(new AbstractMap.SimpleEntry(Attribute.MAXHP, "maximale Lebenspunkte (um 10)"));
        attributes.add(new AbstractMap.SimpleEntry(Attribute.ATTACK, "Angriff (um 1)"));
        attributes.add(new AbstractMap.SimpleEntry(Attribute.DEFENSE, "Verteidigung (um 1)"));
        attributes.add(new AbstractMap.SimpleEntry(Attribute.DEXTERITY, "Geschick (um 1)"));

        Attribute output = genericSelect(attributes, false,
                "     Herzlichen Glückwunsch, du hast es",
                "       ins nächste Semester geschafft!",
                "",
                "    Welches Attribut möchtest du erhöhen?",
                "");

        Guard.argumentIsNotNull(output);
        return output;
    }

    public static InputStream getResource(String identifier, String suffix) {
        String dir = settings.paths.get(identifier);
        return ZombieFU.class.getResourceAsStream(dir + suffix);
    }

    public static void endGame() {
        // TODO: Im Endscreen dynamisch Informationen anzeigen.
        showStaticImage("endscreen");
        System.exit(0);
    }
}
