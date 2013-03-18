package zombiefu.util;

import jade.core.Actor;
import jade.util.Dice;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombiefu.actor.Door;
import zombiefu.human.Shop;
import zombiefu.builder.FoodBuilder;
import zombiefu.builder.ItemBuilder;
import zombiefu.builder.MonsterBuilder;
import zombiefu.builder.WeaponBuilder;
import zombiefu.items.Item;
import zombiefu.items.KeyCard;
import zombiefu.actor.Teleporter;
import zombiefu.builder.HumanBuilder;
import zombiefu.builder.ShopBuilder;
import zombiefu.exception.ActorConfigNotFoundException;
import zombiefu.human.Human;
import zombiefu.items.Food;
import zombiefu.items.MensaCard;
import zombiefu.items.Weapon;
import zombiefu.items.WeaponType;
import zombiefu.level.Level;
import zombiefu.mapgen.RoomBuilder;
import zombiefu.monster.Monster;
import zombiefu.player.Discipline;

public class ConfigHelper {

    private static final ColoredChar DEFAULT_FLOOR_CHAR = ColoredChar.create(' ');
    private static HashMap<String, WeaponBuilder> weapons;
    private static HashMap<String, FoodBuilder> food;
    private static HashMap<String, Door> doors;
    private static HashMap<String, ShopBuilder> shops;
    private static HashMap<String, Level> levels;
    private static HashMap<String, MonsterBuilder> monsters;
    private static HashMap<String, HumanBuilder> humans;
    private static HashMap<Character, Color> charSet;
    private static HashMap<Character, Boolean> passSet;
    private static HashMap<Character, Boolean> visibleSet;

    private static void initCharSet() {
        charSet = new HashMap<Character, Color>();
        passSet = new HashMap<Character, Boolean>();
        visibleSet = new HashMap<Character, Boolean>();
        String[] settings = getStrings(new File(ZombieGame.getResourceDirectory("base"), "CharSet.txt"));
        for (int i = 0; i < settings.length; i++) {
            String[] setting = settings[i].split(" ");
            charSet.put(setting[0].charAt(0), Color.decode("0x" + setting[3]));
            passSet.put(setting[0].charAt(0), setting[1].equals("passable"));
            visibleSet.put(setting[0].charAt(0), setting[2].equals("visible"));
        }
    }

    private static ItemBuilder getItemBuilderByName(String s) {
        try {
            return getWeaponBuilderByName(s);
        } catch (ActorConfigNotFoundException ex) {
            try {
                return getFoodBuilderByName(s);
            } catch (ActorConfigNotFoundException ex1) {
                return null;
            }
        }
    }

    private static FoodBuilder getFoodBuilderByName(String s) throws ActorConfigNotFoundException {
        if (food == null) {
            food = new HashMap<String, FoodBuilder>();
        }
        if (!food.containsKey(s)) {
            ZombieTools.log("getFoodBuilderByName(" + s + "): Erzeuge FoodBuilder");
            ActorConfig config = new ActorConfig("food", s);
            String name = config.getName();
            ColoredChar c = config.getChar();
            Integer hp = Integer.decode(config.get("hp", "0"));
            food.put(s, new FoodBuilder(c, name, hp));
        }
        return food.get(s);
    }

    private static WeaponBuilder getWeaponBuilderByName(String s) throws ActorConfigNotFoundException {
        if (weapons == null) {
            weapons = new HashMap<String, WeaponBuilder>();
        }
        if (!weapons.containsKey(s)) {
            ZombieTools.log("getWeaponBuilderByName(" + s + "): Erzeuge WeaponBuilder");
            ActorConfig config = new ActorConfig("weapons", s);
            String name = config.getName();
            ColoredChar c = config.getChar();
            WeaponType type = WeaponType.getTypeFromString(config.get("type", "Nahkampf"));
            String munitionStr = config.get("munition", "-1");
            Integer munition = munitionStr.equals("unbegrenzt") ? -1 : Integer.decode(munitionStr);
            Integer damage = Integer.decode(config.get("damage", "1"));
            Integer range = Integer.decode(config.get("range", "1"));
            Double radius = Double.valueOf(config.get("radius", "1.0"));
            String expertsStr = config.get("experts");
            Set<Discipline> experts = new HashSet<Discipline>();
            if (expertsStr != null) {
                for (String exp : expertsStr.split(" ")) {
                    experts.add(Discipline.getTypeFromString(exp));
                }
            }
            weapons.put(s, new WeaponBuilder(c, name, damage, type, experts, munition, radius, range));
        }
        return weapons.get(s);
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
            String[] shop = getStrings(new File(ZombieGame.getResourceDirectory("shops"), s + ".shop"));
            String[] charInfo = shop[0].split(" ");
            HashMap<ItemBuilder, Integer> items = new HashMap<ItemBuilder, Integer>();
            for (int i = 1; i < shop.length; i++) {
                String[] it = shop[i].split(" ");
                ItemBuilder itb = getItemBuilderByName(it[0]);
                Guard.argumentIsNotNull(itb);
                items.put(itb, Integer.valueOf(it[1]));
            }
            shops.put(s, new ShopBuilder(ColoredChar.create(charInfo[0].charAt(0), Color.decode("0x" + charInfo[1])), s, items));

        }
        return shops.get(s).buildShop();
    }

    public static Monster newMonsterByName(String s) {
        if (monsters == null) {
            monsters = new HashMap<String, MonsterBuilder>();
        }
        if (!monsters.containsKey(s)) {
            ZombieTools.log("newMonsterByName(" + s + "): Erzeuge MonsterBuilder");
            ActorConfig config = ActorConfig.getConfig("monsters", s);
            String name = config.getName();
            ColoredChar c = config.getChar();
            int hp = Integer.decode(config.get("baseAttr.hp"));
            int attack = Integer.decode(config.get("baseAttr.att"));
            int defense = Integer.decode(config.get("baseAttr.def"));
            Weapon w = newWeaponByName(config.get("weapon"));
            int ects = Integer.decode(config.get("ects"));
            Set<Actor> m = decodeITM(config.get("drop"));
            monsters.put(s, new MonsterBuilder(c, name, hp, attack, defense, w, ects, m));
        }
        return monsters.get(s).buildMonster();
    }

    public static Human newHumanByName(String s) {
        if (humans == null) {
            humans = new HashMap<String, HumanBuilder>();
        }
        if (!humans.containsKey(s)) {
            ZombieTools.log("newHumanByName(" + s + "): Erzeuge HumanBuilder");
            ActorConfig config = ActorConfig.getConfig("humans", s);
            String name = config.getName();
            ColoredChar c = config.getChar();
            Item offerItem = config.contains("deal.offerItem") ? (Item) decodeITM(config.get("deal.offerItem")).iterator().next() : null;
            Integer offerMoney = config.contains("deal.offerMoney") ? Integer.decode(config.get("deal.offerMoney")) : null;
            Integer requestMoney = config.contains("deal.requestMoney") ? Integer.decode(config.get("deal.requestMoney")) : null;
            String requestItem = config.get("deal.requestItem");
            Map<String, String> phraseSet = config.getSubConfig("phrase");
            humans.put(s, new HumanBuilder(c, name, phraseSet, offerItem, offerMoney, requestItem, requestMoney));
        }
        return humans.get(s).buildHuman();
    }

    public static Weapon newWeaponByName(String s) {
        try {
            return (Weapon) getWeaponBuilderByName(s).buildItem();
        } catch (ActorConfigNotFoundException ex) {
            Guard.verifyState(false);
            return null;
        }
    }

    public static Food newFoodByName(String s) {
        try {
            return (Food) getFoodBuilderByName(s).buildItem();
        } catch (ActorConfigNotFoundException ex) {
            System.out.println(s);
            Guard.verifyState(false);
            return null;
        }
    }

    public static KeyCard getKeyCardByName(String s) {
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

    public static boolean isValidChar(char c) {
        return getCharSet().containsKey(c);
    }

    public static Set<Actor> decodeITM(String entry) {
        Set<Actor> ret = new HashSet<Actor>();
        String[] strings = entry.split(" ");
        for (String s : strings) {
            Matcher m = Pattern.compile("^(\\w+)\\((.+)\\)x?([0-9]*)$").matcher(s);
            Guard.verifyState(m.matches());
            String key = m.group(1);
            String[] arguments = m.group(2).split("\\s?,\\s?");
            int anzahl = m.group(3).isEmpty() ? 1 : Integer.decode(m.group(3));
            // Tomas: Ich möchte hier eigentlich switch benutzen, aber ich 
            // darf nicht, weil Java 6 das nicht kann. Grrrrrr!
            for (int i = 1; i <= anzahl; i++) {
                if (key.equals("food")) {
                    ret.add(newFoodByName(arguments[0]));
                } else if (key.equals("weapon")) {
                    ret.add(newWeaponByName(arguments[0]));
                } else if (key.equals("door")) {
                    ret.add(getDoorByName(arguments[0]));
                } else if (key.equals("key")) {
                    ret.add(getKeyCardByName(arguments[0]));
                } else if (key.equals("mensacard")) {
                    ret.add(new MensaCard(arguments.length > 1 ? Dice.global.nextInt(Integer.decode(arguments[0]), Integer.decode(arguments[1])) : Integer.decode(arguments[0])));
                } else if (key.equals("shop")) {
                    ret.add(newShopByName(arguments[0]));
                } else if (key.equals("human")) {
                    ret.add(newHumanByName(arguments[0]));
                } else if (key.equals("monster")) {
                    ret.add(newMonsterByName(arguments[0]));
                } else if (key.equals("teleporter")) {
                    ret.add(new Teleporter(arguments[0], new Coordinate(Integer.decode(arguments[1]), Integer.decode(arguments[2]))));
                } else {
                    throw new IllegalArgumentException("decodeITMEntry(" + s + "): Ungültiges Item");
                }
            }
        }
        return ret;
    }

    public static Level createLevelFromFile(String mapName) {

        ZombieTools.log("createLevelFromFile(" + mapName + ")");

        // Lese Metadaten ein
        ZombieTools.log("createLevelFromFile(" + mapName + "): Lese Metadaten ein");
        ActorConfig config;
        try {
            config = new ActorConfig("maps", mapName);
        } catch (ActorConfigNotFoundException ex) {
            Guard.verifyState(false);
            return null;
        }

        // Suche floorChar und bgChar
        ColoredChar floorChar, bgChar;
        if (config.contains("defaulttile.char") && config.contains("defaulttile.color")) {
            floorChar = ColoredChar.create(ZombieTools.getCharFromString(config.get("defaulttile.char")), ZombieTools.getColorFromString(config.get("defaulttile.color")));
        } else {
            floorChar = DEFAULT_FLOOR_CHAR;
        }

        // Lese ItemMap ein
        ZombieTools.log("createLevelFromFile(" + mapName + "): Lese Itemmap ein");
        HashMap<Character, String> itemMap = new HashMap<Character, String>();
        String[] items = getStrings(new File(ZombieGame.getResourceDirectory("maps"), mapName + ".itm"));
        for (String st : items) {
            String[] it = st.split(" ", 2);
            itemMap.put(it[0].charAt(0), it[1]);
        }

        // Lese Map ein
        ZombieTools.log("createLevelFromFile(" + mapName + "): Lese Maps aus Mapfile");
        String[] level = getStrings(new File(ZombieGame.getResourceDirectory("maps"), mapName + ".map"));
        ColoredChar[][] chars = new ColoredChar[level.length][level[0].length()];
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length(); j++) {
                if (level[i].charAt(j) == '.') {
                    chars[i][j] = floorChar;
                } else if (isValidChar(level[i].charAt(j))) {
                    char c = level[i].charAt(j);
                    chars[i][j] = ColoredChar.create(c, getCharSet().get(c));
                } else if (itemMap.containsKey(level[i].charAt(j))) {
                    chars[i][j] = floorChar;
                } else {
                    chars[i][j] = ColoredChar.create(level[i].charAt(j), Color.WHITE);
                }
            }
        }

        // Baue Level
        ZombieTools.log("createLevelFromFile(" + mapName + "): Erzeuge Level");
        RoomBuilder builder = new RoomBuilder(chars, floorChar);
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
                    Set<Actor> actors = decodeITM(itemMap.get(c));
                    for (Actor a : actors) {
                        lev.addActor(a, x, y);
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
        return readFile(new File(ZombieGame.getResourceDirectory("screens"), imageName + ".scr"));
    }
}
