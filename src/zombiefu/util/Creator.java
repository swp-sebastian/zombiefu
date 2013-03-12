package zombiefu.util;

import jade.core.World;
import jade.ui.TermPanel;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import zombiefu.creature.Player;
import zombiefu.items.HealingItem;
import zombiefu.items.Item;
import zombiefu.items.Teleporter;
import zombiefu.items.Waffe;
import zombiefu.items.Waffentyp;
import zombiefu.level.Level;

public class Creator {

    private static final String srcs = "src/sources/";
    public static final HashMap<String, Item> items = createItems();
    public static final HashMap<Character, String> itemMap = createItemMap();
    public static final HashMap<String, Level> world = createWorld();

    public static void createStoryForPlayer(Player player) {
        Level level = world.get(getFirstWordOfFile(srcs + "levels.txt"));
        level.addActor(player);
        level.fillWithEnemies();
    }

    public static void createBidirectionalTeleporter(World world1,
            Coordinate from1, Coordinate to2, World world2, Coordinate from2,
            Coordinate to1) {
        /*
         * fromi: Wo befindet sich der Teleporter in Welt i? toi: Wo soll der
         * Player in Welt i hinteleportiert werden?
         */
        Teleporter tel1 = new Teleporter(world2, to2);
        Teleporter tel2 = new Teleporter(world1, to1);
        world1.addActor(tel1, from1);
        world2.addActor(tel2, from2);
    }

    private static HashMap<String, Item> createItems() {
        HashMap<String, Item> itemMap = new HashMap<String, Item>();
        String[] waffen = getStrings(srcs + "Waffen.txt");
        String[] healingItems = getStrings(srcs + "HealingItems.txt");
        for (String s : waffen) {
            try {
                String[] st = s.split(" ");
                ColoredChar chr = ColoredChar.create(st[1].charAt(0),
                        Color.decode("0x" + st[2]));
                if (st[3].equals("Nahkampf"))
                    itemMap.put(st[0],
                            new Waffe(chr, st[0], Integer.decode(st[4]),
                                    Waffentyp.NAHKAMPF));
                else if (st[3].equals("Fernkampf"))
                    itemMap.put(st[0],
                            new Waffe(chr, st[0], Integer.decode(st[4]),
                                    Waffentyp.FERNKAMPF, Integer.decode(st[5])));
                else if (st[3].equals("Umkreis"))
                    itemMap.put(st[0],
                            new Waffe(chr, st[0], Integer.decode(st[4]),
                                    Waffentyp.UMKREIS, Double.parseDouble(st[5])));
                else
                    itemMap.put(st[0],
                            new Waffe(chr, st[0], Integer.decode(st[4]),
                                    Waffentyp.GRANATE, Double.parseDouble(st[5]),
                                    Integer.decode(st[6])));
            } catch (Exception e) {
            }
        }
        for (String s : healingItems) {
            try {
                String[] st = s.split(" ");
                itemMap.put(
                        st[0],
                        new HealingItem(ColoredChar.create(st[2].charAt(0),
                                Color.decode("0x" + st[3])), st[0], Integer
                                .decode(st[1])));
            } catch (Exception e) {
            }
        }
        return itemMap;
    }

    private static HashMap<Character, String> createItemMap() {
        String[] items = getStrings(srcs + "Items.txt");
        HashMap<Character, String> itemMap = new HashMap<Character, String>();
        for (String st : items) {
            String[] s = st.split(" ");
            try {
                itemMap.put(s[0].charAt(0), s[1]);
            } catch (Exception e) {
            }
        }
        return itemMap;
    }

    private static void addItems(Level lev, String[] s) {
        for (int y = 0; y < s.length; y++) {
            for (int x = 0; x < s[y].length(); x++) {
                char c = s[y].charAt(x);
                if (itemMap.containsKey(c)) {
                    lev.addActor(items.get(itemMap.get(c)), x, y);
                }
            }
        }
    }

    private static String getFirstWordOfFile(String input) {
        try {
            String firstLine = getStrings(input)[0];
            return firstLine.split(" ")[0];
        } catch (Exception e) {
            return null;
        }
    }

    private static HashMap<String, Level> createWorld() {
        String[] levels = getStrings(srcs + "levels.txt");
        String[] teles = getStrings(srcs + "teleporters.txt");
        HashMap<String, Level> nameOfLevels = new HashMap<String, Level>();
        for (String s : levels) {
            Level level = Level.levelFromFile(srcs + s + ".txt");
            addItems(level, getStrings(srcs + s + ".txt"));
            nameOfLevels.put(s, level);
        }
        for (String s : teles) {
            try {
                String[] d = s.split(" ");
                Level world1 = nameOfLevels.get(d[0]);
                Coordinate from1 = new Coordinate(Integer.decode(d[1]),
                        Integer.decode(d[2]));
                Coordinate to2 = new Coordinate(Integer.decode(d[3]),
                        Integer.decode(d[4]));
                Level world2 = nameOfLevels.get(d[5]);
                Coordinate from2 = new Coordinate(Integer.decode(d[6]),
                        Integer.decode(d[7]));
                Coordinate to1 = new Coordinate(Integer.decode(d[8]),
                        Integer.decode(d[9]));
                createBidirectionalTeleporter(world1, from1, to2, world2,
                        from2, to1);
            } catch (Exception e) {
            }
        }
        return nameOfLevels;
    }

    public static ColoredChar[][] readLevel(String input) throws IOException {
        String[] level = getStrings(input);
        HashMap<Character, String> itemMap = createItemMap();
        ColoredChar[][] chars = new ColoredChar[level.length][level[0].length()];
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length(); j++) {
                if (!itemMap.containsKey(level[i].charAt(j)))
                    chars[i][j] = ColoredChar.create(level[i].charAt(j));
                else
                    chars[i][j] = ColoredChar.create(getFirstWordOfFile(
                            srcs + "CharSet.txt").charAt(0));
            }
        }
        return chars;
    }

    public static String[] getStrings(String input) {
        LinkedList<String> lines = new LinkedList<String>();
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(input), "UTF-16");
            BufferedReader text = new BufferedReader(reader);
            String temp;
            while ((temp = text.readLine()) != null)
                lines.add(temp);
            text.close();
            reader.close();
        } catch (Exception e) {
        }
        String[] erg = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++)
            erg[i] = lines.get(i);
        return erg;
    }

    // Liest eine Datei im UTF-16 Format ein und gibt das 2-dim Feld in
    // ColoredChars zurück
    public static ColoredChar[][] readFile(String input) throws IOException {
        String[] level = getStrings(input);
        ColoredChar[][] chars = new ColoredChar[level.length][level[0].length()];
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length(); j++) {
                chars[i][j] = ColoredChar.create(level[i].charAt(j),
                        Color.white);
            }
        }
        return chars;
    }

    public static void showImage(TermPanel term, String input)
            throws InterruptedException {
        try {
            ColoredChar[][] start = readFile(input);
            term.clearBuffer();
            for (int x = 0; x < term.DEFAULT_COLS; x++) {
                for (int y = 0; y < term.DEFAULT_ROWS; y++) {
                    if (y >= start.length || x >= start[0].length) {
                        term.bufferChar(x, y, ColoredChar.create(' '));
                    } else {
                        term.bufferChar(x, y, start[y][x]);
                    }
                }
            }
            term.refreshScreen();
            term.getKey();
        } catch (IOException e) {
            System.out.println("Datei nicht gefunden.");
        }
    }

}
