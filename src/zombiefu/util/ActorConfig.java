/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.util;

import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombiefu.exception.ActorConfigNotFoundException;
import static zombiefu.util.ConfigHelper.getStrings;

/**
 *
 * @author tomas
 */
public class ActorConfig {

    private String name;
    private HashMap<String, String> config;

    public ActorConfig(String restype, String name) throws ActorConfigNotFoundException {
        this.name = name;
        this.config = new HashMap<String, String>();
        File file = new File(ZombieGame.getResourceDirectory(restype), name + ".cfg");
        String[] cfg = ConfigHelper.getStrings(file);
        if (!file.exists() || !file.canRead()) {
            throw new ActorConfigNotFoundException();
        }
        for (String st : cfg) {
            if (st.trim().isEmpty()) {
                continue;
            }
            String[] it = st.replaceAll("\r?\n?$", "").split("=", 2);
            config.put(it[0], it[1]);
        }
    }

    public static ActorConfig getConfig(String restype, String name) {
        try {
            return new ActorConfig(restype, name);
        } catch (ActorConfigNotFoundException ex) {
            Guard.verifyState(false);
            return null;
        }
    }

    public String get(String s, String def) {
        return config.containsKey(s) ? config.get(s) : def;
    }

    public String get(String s) {
        return config.containsKey(s) ? config.get(s) : null;
    }

    public boolean contains(String s) {
        return config.containsKey(s);
    }

    public ColoredChar getChar() {
        String ch, col;
        if (config.containsKey("tile.char")) {
            ch = config.get("tile.char");
        } else {
            ch = "X";
        }
        if (config.containsKey("tile.color")) {
            col = config.get("tile.color");
        } else {
            col = "FFFFFF";
        }
        return ColoredChar.create(ZombieTools.getCharFromString(ch), ZombieTools.getColorFromString(col));

    }

    public String getName() {
        if (config.containsKey("name")) {
            return config.get("name");
        } else {
            return name;
        }
    }

    public HashMap<String, String> getSubConfig(String s) {
        HashMap<String, String> subConfig = new HashMap<String, String>();
        Pattern pattern = Pattern.compile("^" + s + "\\.(\\w+)$");
        for (String m : config.keySet()) {
            Matcher matcher = pattern.matcher(m);
            if (matcher.matches()) {
                System.out.println(config.get(m));
                subConfig.put(matcher.group(1), config.get(m));
            }
        }
        return subConfig;
    }
}