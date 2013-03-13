package zombiefu.util;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import zombiefu.util.Action;

public class KeyEdit {
    private static Properties props;
    private static String name;
    private static HashMap<Character, Action> keys;

    public static String getPlayername(){
        return name;
    }

    public static Action getAction(char c) {
        // Keine Ahnung wie wir sonst <Enter> erkennen.
        if (Character.getNumericValue(c) == -1) {
            // Und das wäre <SPC>, wir binden das zu '_'
            if (c == ' ') {
                return keys.get(new Character('_'));
            }
            return keys.get(new Character(' '));
        }

        return keys.get(Character.valueOf(c));
    }

    public static void read(String config){
        props = new Properties();
        try {
            props.load(new FileInputStream(config));
            System.out.println("Konfigurationsdatei "+config+" geladen.");
        } catch (IOException ex) {
            System.out.println("Konfigurationsdatei "+config+" nicht vorhanden.");
        }

        name = props.getProperty("name", System.getProperty("user.name"));

        keys = new HashMap<Character, Action>();

        keys.put(Character.valueOf(props.getProperty("controls.up", "w").charAt(0)), Action.UP);
        keys.put(Character.valueOf(props.getProperty("controls.down", "s").charAt(0)), Action.DOWN);
        keys.put(Character.valueOf(props.getProperty("controls.left", "a").charAt(0)), Action.LEFT);
        keys.put(Character.valueOf(props.getProperty("controls.right", "d").charAt(0)), Action.RIGHT);
        keys.put(Character.valueOf(props.getProperty("controls.attack", Character.toString(' ')).charAt(0)), Action.ATTACK);
        keys.put(Character.valueOf(props.getProperty("controls.nextweapon", "e").charAt(0)), Action.NEXT_WEAPON);
        keys.put(Character.valueOf(props.getProperty("controls.prevweapon", "q").charAt(0)), Action.PREV_WEAPON);
        keys.put(Character.valueOf(props.getProperty("controls.inventory", "i").charAt(0)), Action.INVENTORY);
    }
}
