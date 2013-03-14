package zombiefu.util;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import zombiefu.util.Action;

public class ZombieSettings {
    private Properties props;
    public final String name;
    public final HashMap<Character, Action> keybindings;
    public final HashMap<String, File> paths;

    public ZombieSettings(String[] args, String res) {
        props = new Properties(defaults(res));

        try {
            props.load(new FileInputStream(res + "/config.cfg"));
            System.out.println("Konfigurationsdatei "+res+"/config.cfg geladen.");
        } catch (IOException ex) {
            System.out.println("Konfigurationsdatei "+res+"/config.cfg  nicht vorhanden.");
        }

        // Den Spielernamen öffentlich machen.
        name = props.getProperty("player.name");

        // Die Keybindings einlesen.
        keybindings = new HashMap<Character, Action>();
        keybindings.put(Character.valueOf(props.getProperty("controls.up").charAt(0)), Action.UP);
        keybindings.put(Character.valueOf(props.getProperty("controls.down").charAt(0)), Action.DOWN);
        keybindings.put(Character.valueOf(props.getProperty("controls.left").charAt(0)), Action.LEFT);
        keybindings.put(Character.valueOf(props.getProperty("controls.right").charAt(0)), Action.RIGHT);
        keybindings.put(Character.valueOf(props.getProperty("controls.attack").charAt(0)), Action.ATTACK);
        keybindings.put(Character.valueOf(props.getProperty("controls.nextweapon").charAt(0)), Action.NEXT_WEAPON);
        keybindings.put(Character.valueOf(props.getProperty("controls.prevweapon").charAt(0)), Action.PREV_WEAPON);
        keybindings.put(Character.valueOf(props.getProperty("controls.inventory").charAt(0)), Action.INVENTORY);

        // Die Pfadangaben einlesen.
        paths = new HashMap<String, File>();
        paths.put("base", new File(props.getProperty("dir.base")));
        paths.put("maps", new File(props.getProperty("dir.maps")));
        paths.put("items", new File(props.getProperty("dir.items")));
        paths.put("screens", new File(props.getProperty("dir.screens")));

        System.out.println(paths.toString());
    }


    private Properties defaults(String res) {
        Properties def = new Properties();

        def.setProperty("dir.base", res);
        def.setProperty("dir.maps", res + "/maps");
        def.setProperty("dir.items", res + "/items");
        def.setProperty("dir.screens", res + "/screens");

        def.setProperty("player.name", System.getProperty("user.name"));
        def.setProperty("controls.up", "w");
        def.setProperty("controls.down", "s");
        def.setProperty("controls.left", "a");
        def.setProperty("controls.right", "d");
        def.setProperty("controls.attack", " ");
        def.setProperty("controls.nextweapon", "e");
        def.setProperty("controls.prevweapon", "q");
        def.setProperty("controls.inventory", "i");

        return def;
    }



}
