/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.util;

import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import java.awt.Color;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;
import zombiefu.creature.Door;
import zombiefu.creature.Player;
import zombiefu.creature.Shop;
import zombiefu.items.ConsumableItem;
import zombiefu.items.Item;
import zombiefu.items.KeyCard;
import zombiefu.items.Waffe;
import zombiefu.level.Level;
import zombiefu.ui.ZombieFrame;
import zombiefu.util.ConfigHelper;

/**
 *
 * @author tomas
 */
public class ZombieGame {

    private static ZombieSettings settings;
    private static ZombieFrame frame;
    private static Player player;

    public static void createGame(String[] args, String name) {
        settings = new ZombieSettings(args, "src/sources");
        frame = new ZombieFrame(name);
    }

    public static ZombieSettings getSettings() {
        return settings;
    }

    public static void showStaticImage(String file) {
        try {
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
            frame.mainTerm().getKey();
        } catch (IOException ex) {
        } catch (InterruptedException ex) {
        }
    }

    public static void initialize() {
        Level firstLevel = ConfigHelper.getFirstLevel();
        ArrayList<String> waffen = new ArrayList<String>();
        waffen.add("SuperFist");
        player = new Player(ColoredChar.create('\u263B', Color.decode("0x7D26CD")), settings.name, 100, 10, 10, 10, waffen);

        firstLevel.addActor(player);
        firstLevel.fillWithEnemies();
        firstLevel.addActor(new Shop(ColoredChar.create('M')));
        frame.mainTerm().registerCamera(player, 40, 17);
    }

    public static void setTopFrameContent(String s) {
        frame.topTerm().clearBuffer();
        if (s != null) {
            frame.topTerm().bufferString(0, 0, s);
        }
        frame.topTerm().refreshScreen();
    }

    public static void newMessage(String s) {
        refreshMainFrame();
        setTopFrameContent(s);
        char key = 0;
        try {
            key = frame.mainTerm().getKey();
        } catch (InterruptedException ex) {
        }
        setTopFrameContent(null);
    }

    public static void refreshMainFrame() {
        frame.mainTerm().clearBuffer();
        frame.mainTerm().bufferCameras();
        frame.mainTerm().refreshScreen();
    }

    public static void refreshBottomFrame() {
        frame.bottomTerm().clearBuffer();
        frame.bottomTerm().bufferString(
                0,
                0,
                "Waffe: " + player.getActiveWeapon().getName() + " ("
                + player.getActiveWeapon().getMunitionToString()
                + " / " + player.getActiveWeapon().getDamage() + ") "
                + " | HP: " + player.getHealthPoints() + "/"
                + player.getMaximalHealthPoints() + " | A: "
                + player.getAttackValue() + " | D: "
                + player.getDefenseValue() + " | I: "
                + player.getIntelligenceValue());
        frame.bottomTerm().bufferString(
                0,
                1,
                "Ort: " + ((Level) player.world()).getName() + "(" + player.pos().x() + "|" + player.pos().y() + ")"
                + " | € " + player.getMoney() + " | ECTS "
                + player.getECTS() + " | Sem " + player.getSemester() + " | GodMode: "
                + (player.isGod() ? "an" : "aus"));
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
        askPlayerForKey();
        refreshMainFrame();
    }

    public static Player getPlayer() {
        return player;
    }

    public static char askPlayerForKey() {
        try {
            return frame.mainTerm().getKey();
        } catch (InterruptedException ex) {
            Logger.getLogger(ZombieGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            System.exit(1);
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

    public static ConsumableItem askPlayerForItem(ArrayList<ConsumableItem> inventar) {
        ConsumableItem output = null;
        if (inventar.isEmpty()) {
            ZombieGame.newMessage("Inventar ist leer.");
            return null;
        }
        frame.mainTerm().clearBuffer();
        frame.mainTerm().bufferString(0, 0, "Inventarliste:");
        for (int i = 0; i < inventar.size(); i++) {
            Item it = inventar.get(i);
            frame.mainTerm().bufferString(
                    0,
                    2 + i,
                    "[" + ((char) (97 + i)) + "] " + it.face() + " - "
                    + it.getName());
        }
        frame.mainTerm().refreshScreen();
        int key = ((int) ZombieGame.askPlayerForKey()) - 97;
        if (key >= 0 && key <= 25 && key < inventar.size()) {
            output = inventar.get(key);
        }
        refreshMainFrame();
        return output;
    }

    public static Discipline askPlayerForDiscipline() {
        // TODO: Adrians Studiengangabfrage
        return Discipline.MATHEMATICS;
    }

    public static File getSourceDirectory() {
        return settings.paths.get("base");
    }

    public static File getItemDirectory() {
        return settings.paths.get("items");
    }

    public static File getMapDirectory() {
        return settings.paths.get("maps");
    }

    public static File getScreenDirectory() {
        return settings.paths.get("screens");
    }

    public static void endGame() {
        // TODO: Im Endscreen dynamisch Informationen anzeigen.
        showStaticImage("endscreen");
        System.exit(0);
    }
}
