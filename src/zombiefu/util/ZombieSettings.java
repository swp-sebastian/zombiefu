package zombiefu.util;

import jade.core.Actor;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import zombiefu.items.Waffe;
import zombiefu.player.Attribut;
import zombiefu.util.Action;

public class ZombieSettings {
    private Properties props;
    public final String playerName;
    public final HashMap<Attribut,Integer> playerAttributes;
    public final String playerInventar;
    public final ColoredChar playerChar;
    public final boolean debug;
    public final HashMap<String, Action> keybindings;
    public final HashMap<String, File> paths;

    public ZombieSettings(String[] args, String res) {
        props = new Properties(defaults(res));

        try {
            props.load(new FileInputStream(res + "/config.cfg"));
            System.out.println("ZombieSettings: Konfigurationsdatei "+res+"/config.cfg geladen.");
        } catch (IOException ex) {
            System.out.println("ZombieSettings: Konfigurationsdatei " + res +
                               "/config.cfg  nicht vorhanden. Benutze Defaults.");
        }

        // Spielerinfo
        playerName = props.getProperty("player.name");
        playerChar = ColoredChar.create(ZombieTools.getCharFromString(props.getProperty("player.tile.char")), ZombieTools.getColorFromString(props.getProperty("player.tile.color")));
        playerInventar = props.getProperty("player.startItems");
        playerAttributes = new HashMap<Attribut,Integer>();
        playerAttributes.put(Attribut.MAXHP, Integer.decode(props.getProperty("player.attr.hp")));
        playerAttributes.put(Attribut.ATTACK, Integer.decode(props.getProperty("player.attr.att")));
        playerAttributes.put(Attribut.DEFENSE, Integer.decode(props.getProperty("player.attr.def")));
        playerAttributes.put(Attribut.INTELLIGENCE, Integer.decode(props.getProperty("player.attr.int")));

        // Debug-Modus
        if (props.getProperty("debug").equalsIgnoreCase("true")) {
            debug = true;
        } else {
            debug = false;
        }

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
        keybindings.put(props.getProperty("controls.help"), Action.HELP);

        // Die Pfadangaben einlesen.
        paths = new HashMap<String, File>();
        paths.put("base", new File(props.getProperty("dir.base")));
        paths.put("maps", new File(props.getProperty("dir.maps")));
        paths.put("items", new File(props.getProperty("dir.items")));
        paths.put("screens", new File(props.getProperty("dir.screens")));
        paths.put("shops", new File(props.getProperty("dir.shops")));
        paths.put("monster", new File(props.getProperty("dir.monster")));
        paths.put("humans", new File(props.getProperty("dir.humans")));

        // Überprüfen, ob Pfade lesbar sind.
        Iterator itr = paths.values().iterator();
        while(itr.hasNext()) {
            File f = (File) itr.next();
            Guard.verifyState(f.canRead());
        }
    }


    private Properties defaults(String res) {
        Properties def = new Properties();

        // Default Verzeichnis-Layout
        def.setProperty("dir.base", res);
        def.setProperty("dir.maps", res + "/maps");
        def.setProperty("dir.items", res + "/items");
        def.setProperty("dir.screens", res + "/screens");
        def.setProperty("dir.shops", res + "/shops");
        def.setProperty("dir.monster", res + "/monster");
        def.setProperty("dir.humans", res + "/humans");

        // Default Debug Einstellung (aus)
        def.setProperty("debug", "false");

        // Default Playername
        def.setProperty("player.name", System.getProperty("user.name"));
        def.setProperty("player.attr.hp", "100");
        def.setProperty("player.attr.att", "5");
        def.setProperty("player.attr.def", "5");
        def.setProperty("player.attr.int", "5");
        def.setProperty("player.startItems", "item(Faust)");
        def.setProperty("player.tile.char", "263B");
        def.setProperty("player.tile.color", "7D26CD");

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
        def.setProperty("controls.help", "QUESTION MARK");

        return def;
    }
}
