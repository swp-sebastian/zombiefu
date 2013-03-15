package zombiefu.util;

import jade.core.Actor;
import jade.core.World;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombiefu.actor.Door;
import zombiefu.human.Shop;
import zombiefu.itembuilder.HealingItemBuilder;
import zombiefu.itembuilder.ItemBuilder;
import zombiefu.itembuilder.WaffenBuilder;
import zombiefu.items.Item;
import zombiefu.items.KeyCard;
import zombiefu.actor.Teleporter;
import zombiefu.itembuilder.ShopBuilder;
import zombiefu.items.Waffe;
import zombiefu.items.Waffentyp;
import zombiefu.level.Level;
import zombiefu.mapgen.RoomBuilder;
import zombiefu.player.Discipline;

public class ConfigHelper {

    private static HashMap<String, ItemBuilder> items;
    private static HashMap<String, Door> doors;
    private static HashMap<String, ShopBuilder> shops;
    private static HashMap<String, Level> levels;
    private static HashMap<Character, Color> charSet;
    private static HashMap<Character, Boolean> passSet;
    private static HashMap<Character, Boolean> visibleSet;
    private static Character defaultChar;
    private static Level startMap;
    private static Level globalMap;
    private static Coordinate startPosition;

    private static void initItems() {
        ZombieTools.log("initItems(): Initialisiere Items");
        items = new HashMap<String, ItemBuilder>();

        // Lade Waffen
        ZombieTools.log("initItems(): Lade Waffen");
        String[] waffen = getStrings(new File(ZombieGame.getItemDirectory(), "Waffen.txt"));
        for (String s : waffen) {
            String[] st = s.split(" ");
            if (st.length < 8) {
                ZombieTools.logError("initItems(): Ungültige Zeile in Waffen.txt: " + s);
                continue;
            }

            ColoredChar face = ColoredChar.create(st[1].charAt(0), Color.decode("0x" + st[2]));

            String name = st[0];
            int damage = Integer.decode(st[5]);

            int munition;
            if (st[4].equals("unbegrenzt")) {
                munition = -1;
            } else {
                munition = Integer.decode(st[4]);
            }

            Waffentyp wtyp = Waffentyp.getTypeFromString(st[3]);
            double radius = Double.parseDouble(st[6]);
            int range = Integer.decode(st[7]);

            Set<Discipline> experts = new HashSet<Discipline>();
            for (int i = 8; i < st.length; i++) {
                experts.add(Discipline.getTypeFromString(st[i]));
            }
            items.put(st[0], new WaffenBuilder(face, name, damage, wtyp, experts, munition, radius, range));
        }

        // Lade HealingItems
        ZombieTools.log("initItems(): Lade HealingItems");
        String[] healingItems = getStrings(new File(ZombieGame.getItemDirectory(), "HealingItems.txt"));
        for (String s : healingItems) {
            String[] st = s.split(" ");
            items.put(
                    st[0],
                    new HealingItemBuilder(ColoredChar.create(st[2].charAt(0),
                    Color.decode("0x" + st[3])), st[0], Integer
                    .decode(st[1])));
        }
    }

    private static void initCharSet() {
        charSet = new HashMap<Character, Color>();
        passSet = new HashMap<Character, Boolean>();
        visibleSet = new HashMap<Character, Boolean>();
        String[] settings = getStrings(new File(ZombieGame.getSourceDirectory(), "CharSet.txt"));
        for (int i = 0; i < settings.length; i++) {
            String[] setting = settings[i].split(" ");
            charSet.put(setting[0].charAt(0), Color.decode("0x" + setting[3]));
            passSet.put(setting[0].charAt(0), setting[1].equals("passable"));
            visibleSet.put(setting[0].charAt(0), setting[2].equals("visible"));
        }
    }

    private static void initStartInfo() {
        String[] str = getStrings(new File(ZombieGame.getSourceDirectory(), "startinfo.txt"));
        str = str[0].split(" ");
        globalMap = getLevelByName(str[0]);
        startMap = getLevelByName(str[1]);
        startPosition = new Coordinate(Integer.decode(str[2]), Integer.decode(str[3]));
    }

    public static ItemBuilder getItemBuilderByName(String s) {
        if (items == null) {
            initItems();
        }
        ItemBuilder i = items.get(s);
        Guard.argumentIsNotNull(i);
        return i;
    }

    public static Item newItemByName(String s) {
        return getItemBuilderByName(s).buildItem();
    }

    public static Waffe newWaffeByName(String s) {
        Item w = newItemByName(s);
        Guard.argumentIsNotNull(w);
        Guard.verifyState(w instanceof Waffe);
        return (Waffe) w;
    }

    public static Level getLevelByName(String s) {
        if (levels == null) {
            levels = new HashMap<String, Level>();
        }
        if (!levels.containsKey(s)) {
            levels.put(s, createLevelFromFile(s));
        }
        return levels.get(s);
    }

    private static Door getDoorByName(String s) {
        if (doors == null) {
            doors = new HashMap<String, Door>();
        }
        if (!doors.containsKey(s)) {
            doors.put(s, new Door(s));
        }
        return doors.get(s);
    }

    private static Shop newShopByName(String s) {
        if (shops == null) {
            shops = new HashMap<String, ShopBuilder>();
        }
        if (!shops.containsKey(s)) {
            String[] shop = getStrings(new File(ZombieGame.getShopDirectory(), s + ".shop"));
            String[] charInfo = shop[0].split(" ");
            HashMap<ItemBuilder, Integer> items = new HashMap<ItemBuilder, Integer>();
            for (int i = 1; i < shop.length; i++) {
                String[] it = shop[i].split(" ");
                items.put(getItemBuilderByName(it[0]), Integer.valueOf(it[1]));
            }
            shops.put(s, new ShopBuilder(ColoredChar.create(charInfo[0].charAt(0), Color.decode("0x" + charInfo[1])), s, items));

        }
        return shops.get(s).buildShop();
    }

    private static KeyCard getKeyCardByName(String s) {
        return new KeyCard(getDoorByName(s));
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

    public static Level getStartMap() {
        if (startMap == null) {
            initStartInfo();
        }
        return startMap;
    }

    public static Level getGlobalMap() {
        if (globalMap == null) {
            initStartInfo();
        }
        return globalMap;
    }

    public static Coordinate getStartPosition() {
        if (startPosition == null) {
            initStartInfo();
        }
        return startPosition;
    }

    public static boolean isValidChar(char c) {
        return getCharSet().containsKey(c);
    }

    public static char getDefaultChar() {
        if (defaultChar == null) {
            defaultChar = getFirstWordOfFile(new File(ZombieGame.getSourceDirectory(), "CharSet.txt")).charAt(0);
        }
        return defaultChar;
    }

    public static Level createLevelFromFile(String mapName) {

        ZombieTools.log("createLevelFromFile(" + mapName + ")");

        // Lese ItemMap ein
        ZombieTools.log("createLevelFromFile(" + mapName + "): Lese Itemmap ein");
        HashMap<Character, String[]> itemMap = new HashMap<Character, String[]>();
        String[] items = getStrings(new File(ZombieGame.getMapDirectory(), mapName + ".itm"));
        for (String st : items) {
            String[] it = st.split(" ", 2);
            itemMap.put(it[0].charAt(0), it[1].split(" "));
        }
        // Lese Map ein
        ZombieTools.log("createLevelFromFile(" + mapName + "): Lese Maps aus Mapfile");
        String[] level = getStrings(new File(ZombieGame.getMapDirectory(), mapName + ".map"));
        ColoredChar[][] chars = new ColoredChar[level.length][level[0].length()];
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length(); j++) {
                if (isValidChar(level[i].charAt(j))) {
                    chars[i][j] = ColoredChar.create(level[i].charAt(j));
                } else if (itemMap.containsKey(level[i].charAt(j))) {
                    chars[i][j] = ColoredChar.create(getDefaultChar());
                } else {
                    chars[i][j] = ColoredChar.create(level[i].charAt(j), Color.WHITE);
                }
            }
        }

        // Baue Level
        ZombieTools.log("createLevelFromFile(" + mapName + "): Erzeuge Level");
        RoomBuilder builder = new RoomBuilder(chars);
        Level lev = new Level(builder.width(), builder.height(), builder, mapName);

        // Lade statische Items auf Map
        ZombieTools.log("createLevelFromFile(" + mapName + "): Lade statische Items");
        for (int x = 0; x < lev.width(); x++) {
            for (int y = 0; y < lev.height(); y++) {
                char c;
                try {
                    c = level[y].charAt(x);
                } catch (StringIndexOutOfBoundsException exc) {
                    c = ' ';
                }
                if (itemMap.containsKey(c)) {
                    String[] itemNames = itemMap.get(c);
                    for (String itemName : itemNames) {
                        Actor actor = null;
                        Matcher m = Pattern.compile("^(\\w+)\\((.+)\\)$").matcher(itemName);
                        Guard.verifyState(m.matches());
                        switch (m.group(1)) {
                            case "item":
                                actor = newItemByName(m.group(2));
                                break;
                            case "door":
                                actor = getDoorByName(m.group(2));
                                break;
                            case "key":
                                actor = getKeyCardByName(m.group(2));
                                break;
                            case "shop":
                                actor = newShopByName(m.group(2));
                                break;
                            case "teleporter":
                                String[] ziel = m.group(2).split(",");
                                actor = new Teleporter(ziel[0].trim(), new Coordinate(Integer.decode(ziel[1].trim()), Integer.decode(ziel[2].trim())));
                                break;
                            default:
                                throw new IllegalArgumentException("Ungültiges Item in " + mapName + ".itm: " + itemName);
                        }
                        lev.addActor(actor, x, y);
                    }
                }
            }
        }

        return lev;
    }

    public static String[] getStrings(File input) {
        LinkedList<String> lines = new LinkedList<String>();
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(input), "UTF-8");
            BufferedReader text = new BufferedReader(reader);
            String temp;
            while ((temp = text.readLine()) != null) {
                if (!temp.startsWith("//")) {
                    // End of Line comments done right?
                    lines.add(temp.replaceFirst("\\s*\\/\\/.*$", ""));
                }
            }
            text.close();
            reader.close();
        } catch (IOException e) {
        }
        String[] erg = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            erg[i] = lines.get(i);
        }
        return erg;
    }

    // Liest eine Datei im UTF-16 Format ein und gibt das 2-dim Feld in
    // ColoredChars zurück
    private static ColoredChar[][] readFile(File input) throws IOException {
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

    private static String getFirstWordOfFile(File input) {
        String firstLine = getStrings(input)[0];
        return firstLine.split(" ")[0];
    }

    public static ColoredChar[][] getImage(String imageName) throws IOException {
        return readFile(new File(ZombieGame.getScreenDirectory(), imageName + ".scr"));
    }
}
