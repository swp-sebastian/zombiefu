package zombiefu.util;

import jade.core.World;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import zombiefu.creature.Door;
import zombiefu.itembuilder.HealingItemBuilder;
import zombiefu.itembuilder.ItemBuilder;
import zombiefu.itembuilder.KeyCardBuilder;
import zombiefu.itembuilder.WaffenBuilder;
import zombiefu.items.Item;
import zombiefu.items.Teleporter;
import zombiefu.items.Waffe;
import zombiefu.items.Waffentyp;
import zombiefu.level.Level;
import zombiefu.map.RoomBuilder;

public class ConfigHelper {

    private static HashMap<String, ItemBuilder> items;
    private static HashMap<String, Door> doors;
    private static HashMap<String, Level> levels;
    private static HashMap<String, LinkedList<String[]>> levelDoorMap;
    private static HashMap<Character, Color> charSet;
    private static HashMap<Character, Boolean> passSet;
    private static HashMap<Character, Boolean> visibleSet;
    private static Character defaultChar;

    private static void createBidirectionalTeleporter(World world1,
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

    private static void initItems() {
        String baseDir = ZombieGame.getItemDirectory();
        items = new HashMap<String, ItemBuilder>();

        // Lade Waffen
        String[] waffen = getStrings(baseDir + "Waffen.txt");
        for (String s : waffen) {
            try {
                String[] st = s.split(" ");
                ColoredChar chr = ColoredChar.create(st[1].charAt(0),
                        Color.decode("0x" + st[2]));
                WaffenBuilder waffenbuilder;
                int munition;
                if (st[4].equals("unbegrenzt")) {
                    munition = -1;
                } else {
                    munition = Integer.decode(st[4]);
                }
                if (st[3].equals("Nahkampf")) {
                    waffenbuilder = new WaffenBuilder(chr, st[0],
                            Integer.decode(st[5]), Waffentyp.NAHKAMPF, munition);
                } else if (st[3].equals("Fernkampf")) {
                    waffenbuilder = new WaffenBuilder(chr, st[0],
                            Integer.decode(st[5]), Waffentyp.FERNKAMPF,
                            munition, Integer.decode(st[6]));
                } else if (st[3].equals("Umkreis")) {
                    waffenbuilder = new WaffenBuilder(chr, st[0],
                            Integer.decode(st[5]), Waffentyp.UMKREIS, munition,
                            Double.parseDouble(st[6]));
                } else {
                    waffenbuilder = new WaffenBuilder(chr, st[0],
                            Integer.decode(st[5]), Waffentyp.GRANATE, munition,
                            Double.parseDouble(st[6]), Integer.decode(st[7]));
                }
                items.put(st[0], waffenbuilder);
            } catch (Exception e) {
            }
        }

        // Lade HealingItems
        String[] healingItems = getStrings(ZombieGame.getItemDirectory()
                + "HealingItems.txt");
        for (String s : healingItems) {
            try {
                String[] st = s.split(" ");
                items.put(
                        st[0],
                        new HealingItemBuilder(ColoredChar.create(
                                st[2].charAt(0), Color.decode("0x" + st[3])),
                                st[0], Integer.decode(st[1])));
            } catch (Exception e) {
            }
        }

        // Lade KeyCards
        String[] keyCards = getStrings(ZombieGame.getItemDirectory()
                + "KeyCards.txt");
        for (String s : keyCards) {
            try {
                String[] st = s.split(" ");
                items.put(
                        st[0],
                        new KeyCardBuilder(ColoredChar.create(st[1].charAt(0),
                                Color.decode("0x" + st[2])), doors.get(st[0])));
            } catch (Exception e) {
            }
        }
    }

    private static void initLevels() {
        levels = new HashMap<String, Level>();
        doors = new HashMap<String, Door>();
        levelDoorMap = new HashMap<String, LinkedList<String[]>>();
        String[] levelStrings = getStrings(ZombieGame.getSourceDirectory()
                + "levels.txt");
        for (String s : levelStrings){
                levelDoorMap.put(s, new LinkedList<String[]>());
        }
        // Lade alle Türen
        String[] tempDoors = getStrings(ZombieGame.getSourceDirectory()
                + "doors.txt");
        for (String s : tempDoors) {
            try {
                String[] st = s.split(" ");
                ColoredChar face = ColoredChar.create(st[4].charAt(0),
                        Color.decode("0x" + st[5]));
                Door door = new Door(face, st[1]);
                doors.put(door.getName(), door);
                levelDoorMap.get(st[0]).add(new String[]{door.getName(),st[2],st[3]});
            } catch (Exception e) {
            }
        }

        // Lade alle Level
        for (String s : levelStrings) {
            Level level = createLevelFromFile(s);
            levels.put(s, level);
        }

        // Lade Teleporter
        String[] teles = getStrings(ZombieGame.getSourceDirectory()
                + "teleporters.txt");
        for (String s : teles) {
            try {
                String[] d = s.split(" ");
                Level world1 = levels.get(d[0]);
                Coordinate from1 = new Coordinate(Integer.decode(d[1]),
                        Integer.decode(d[2]));
                Coordinate to2 = new Coordinate(Integer.decode(d[3]),
                        Integer.decode(d[4]));
                Level world2 = levels.get(d[5]);
                Coordinate from2 = new Coordinate(Integer.decode(d[6]),
                        Integer.decode(d[7]));
                Coordinate to1 = new Coordinate(Integer.decode(d[8]),
                        Integer.decode(d[9]));
                createBidirectionalTeleporter(world1, from1, to2, world2,
                        from2, to1);
            } catch (Exception e) {
            }
        }
    }

    private static void initCharSet() {
        charSet = new HashMap<Character, Color>();
        passSet = new HashMap<Character, Boolean>();
        visibleSet = new HashMap<Character, Boolean>();
        String[] settings = getStrings(ZombieGame.getSourceDirectory()
                + "CharSet.txt");
        for (int i = 0; i < settings.length; i++) {
            String[] setting = settings[i].split(" ");
            try {
                charSet.put(setting[0].charAt(0),
                        Color.decode("0x" + setting[3]));
                passSet.put(setting[0].charAt(0), setting[1].equals("passable"));
                visibleSet.put(setting[0].charAt(0),
                        setting[2].equals("visible"));
            } catch (Exception e) {
            }
        }
    }

    public static Item newItemByName(String s) {
        if (items == null) {
            initItems();
        }
        ItemBuilder i = items.get(s);
        Guard.argumentIsNotNull(i);
        return items.get(s).buildItem();
    }

    public static Waffe newWaffeByName(String s) {
        Item w = newItemByName(s);
        Guard.argumentIsNotNull(w);
        Guard.verifyState(w instanceof Waffe);
        return (Waffe) w;
    }

    private static Level getLevelByName(String s) {
        if (levels == null) {
            initLevels();
        }
        return levels.get(s);
    }

    public static HashMap<Character, Color> getCharSet() {
        if (charSet == null) {
            initCharSet();
        }
        return charSet;
    }

    public static HashMap<Character, Boolean> getPassSet() {
        if (passSet == null) {
            initCharSet();
        }
        return passSet;
    }

    public static HashMap<Character, Boolean> getVisibleSet() {
        if (visibleSet == null) {
            initCharSet();
        }
        return visibleSet;
    }

    public static Level getFirstLevel() {
        return getLevelByName(getFirstWordOfFile(ZombieGame
                .getSourceDirectory() + "levels.txt"));
    }

    public static boolean isValidChar(char c) {
        return getCharSet().containsKey(c);
    }

    public static char getDefaultChar() {
        if (defaultChar == null) {
            defaultChar = getFirstWordOfFile(
                    ZombieGame.getSourceDirectory() + "CharSet.txt").charAt(0);
        }
        return defaultChar;
    }

    public static Level createLevelFromFile(String mapName) {
        String baseDir = ZombieGame.getMapDirectory();

        // Lese ItemMap ein
        HashMap<Character, String> itemMap = new HashMap<Character, String>();
        String[] items = getStrings(baseDir + mapName + ".itm");
        for (String st : items) {
            String[] s = st.split(" ");
            try {
                itemMap.put(s[0].charAt(0), s[1]);
            } catch (Exception e) {
            }
        }

        // Lese Map ein
        String[] level = getStrings(baseDir + mapName + ".map");
        ColoredChar[][] chars = new ColoredChar[level.length][level[0].length()];
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length(); j++) {
                if (isValidChar(level[i].charAt(j))) {
                    chars[i][j] = ColoredChar.create(level[i].charAt(j));
                } else {
                    chars[i][j] = ColoredChar.create(getDefaultChar());
                }
            }
        }

        // Baue Level
        RoomBuilder builder = new RoomBuilder(chars);
        Level lev = new Level(builder.width(), builder.height(), builder);
        for (String[] s:levelDoorMap.get(mapName)){
            lev.addActor(doors.get(s[0]), Integer.decode(s[1]), Integer.decode(s[2]));
        }

        // Setze statische Items auf das Level:
        for (int x = 0; x < lev.width(); x++) {
            for (int y = 0; y < lev.height(); y++) {
                char c = level[y].charAt(x);
                if (itemMap.containsKey(c)) {
                    lev.addActor(newItemByName(itemMap.get(c)), x, y);
                }
            }
        }

        return lev;
    }

    public static String[] getStrings(String input) {
        LinkedList<String> lines = new LinkedList<String>();
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(input), "UTF-16");
            BufferedReader text = new BufferedReader(reader);
            String temp;
            while ((temp = text.readLine()) != null) {
                if (!temp.startsWith("#")) {
                    lines.add(temp);
                }
            }
            text.close();
            reader.close();
        } catch (Exception e) {
        }
        String[] erg = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            erg[i] = lines.get(i);
        }
        return erg;
    }

    // Liest eine Datei im UTF-16 Format ein und gibt das 2-dim Feld in
    // ColoredChars zurück
    private static ColoredChar[][] readFile(String input) throws IOException {
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

    private static String getFirstWordOfFile(String input) {
        try {
            String firstLine = getStrings(input)[0];
            return firstLine.split(" ")[0];
        } catch (Exception e) {
            return null;
        }
    }

    public static ColoredChar[][] getImage(String imageName) throws IOException {
        return readFile(ZombieGame.getScreenDirectory() + imageName + ".scr");
    }
}
