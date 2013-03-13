package zombiefu.util;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import zombiefu.util.Action;

public class KeyEdit {
    private Properties props;
    public final String name;
    public final  HashMap<Character, Action> keybindings;

    public KeyEdit(String[] args, String res) {

        props = new Properties();
        try {
            props.load(new FileInputStream(res + "/config.cfg"));
            System.out.println("Konfigurationsdatei "+res+"/config.cfg geladen.");
        } catch (IOException ex) {
            System.out.println("Konfigurationsdatei "+res+"/config.cfg  nicht vorhanden.");
        }

        name = props.getProperty("name", System.getProperty("user.name"));

        keybindings = new HashMap<Character, Action>();

        keybindings.put(Character.valueOf(props.getProperty("controls.up", "w").charAt(0)), Action.UP);
        keybindings.put(Character.valueOf(props.getProperty("controls.down", "s").charAt(0)), Action.DOWN);
        keybindings.put(Character.valueOf(props.getProperty("controls.left", "a").charAt(0)), Action.LEFT);
        keybindings.put(Character.valueOf(props.getProperty("controls.right", "d").charAt(0)), Action.RIGHT);
        keybindings.put(Character.valueOf(props.getProperty("controls.attack", Character.toString(' ')).charAt(0)), Action.ATTACK);
        keybindings.put(Character.valueOf(props.getProperty("controls.nextweapon", "e").charAt(0)), Action.NEXT_WEAPON);
        keybindings.put(Character.valueOf(props.getProperty("controls.prevweapon", "q").charAt(0)), Action.PREV_WEAPON);
        keybindings.put(Character.valueOf(props.getProperty("controls.inventory", "i").charAt(0)), Action.INVENTORY);
    }

    // Spielername
    public String getPlayername(){
        return name;
    }

}
