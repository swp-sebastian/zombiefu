package zombiefu.util;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import zombiefu.util.Action;

public class ZombieSettings {
    private Properties props;
    public final String name;
    public final HashMap<String, Action> keybindings;
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

        // Die Keybindings einlesen. TODO: Einstampfen in Schleife.
        keybindings = new HashMap<String, Action>();
        keybindings.put(props.getProperty("controls.up"), Action.UP);
        keybindings.put(props.getProperty("controls.down"), Action.DOWN);
        keybindings.put(props.getProperty("controls.left"), Action.LEFT);
        keybindings.put(props.getProperty("controls.right"), Action.RIGHT);
        keybindings.put(props.getProperty("controls.attack"), Action.ATTACK);
        keybindings.put(props.getProperty("controls.nextweapon"), Action.NEXT_WEAPON);
        keybindings.put(props.getProperty("controls.prevweapon"), Action.PREV_WEAPON);
        keybindings.put(props.getProperty("controls.inventory"), Action.INVENTORY);
        keybindings.put(props.getProperty("controls.noop"), Action.NOOP);

        // Die Pfadangaben einlesen.
        paths = new HashMap<String, File>();
        paths.put("base", new File(props.getProperty("dir.base")));
        paths.put("maps", new File(props.getProperty("dir.maps")));
        paths.put("items", new File(props.getProperty("dir.items")));
        paths.put("screens", new File(props.getProperty("dir.screens")));

        // Überprüfen, ob Pfade lesbar sind.
        Iterator itr = paths.values().iterator();
        while(itr.hasNext()) {
            File f = (File) itr.next();
            if (!f.canRead()) {
                    System.out.println("Fehler: Verzeichnis "+ f + " nicht existent oder nicht lesbar.");
                    System.exit(1);
                }
        }
    }


    private Properties defaults(String res) {
        Properties def = new Properties();

        // Default Verzeichnis-Layout
        def.setProperty("dir.base", res);
        def.setProperty("dir.maps", res + "/maps");
        def.setProperty("dir.items", res + "/items");
        def.setProperty("dir.screens", res + "/screens");

        // Default Playername
        def.setProperty("player.name", System.getProperty("user.name"));

        // Default Keybindings
        def.setProperty("controls.up", "LATIN SMALL LETTER W");
        def.setProperty("controls.down", "LATIN SMALL LETTER S");
        def.setProperty("controls.left", "LATIN SMALL LETTER A");
        def.setProperty("controls.right", "LATIN SMALL LETTER D");
        def.setProperty("controls.attack", "LINE FEED (LF)");
        def.setProperty("controls.nextweapon", "LATIN SMALL LETTER E");
        def.setProperty("controls.prevweapon", "LATIN SMALL LETTER Q");
        def.setProperty("controls.inventory", "LATIN SMALL LETTER I");
        def.setProperty("controls.noop", "FULL STOP");

        return def;
    }



}
