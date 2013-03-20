/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.creature;

import java.util.HashMap;
import zombiefu.player.Attribute;

/**
 *
 * @author tomas
 */
public class AttributeSet {

    public HashMap<Attribute, Integer> set;

    public AttributeSet(HashMap<Attribute, Integer> set) {
        if (set == null) {
            set = new HashMap<>();
        }
        for (Attribute a : Attribute.values()) {
            if (!set.containsKey(a) || set.get(a) == null) {
                set.put(a, 1);
            }
        }
        this.set = set;
    }

    public AttributeSet(int hp, int att, int def, int dex) {
        set = new HashMap<>();
        set.put(Attribute.MAXHP, hp);
        set.put(Attribute.ATTACK, att);
        set.put(Attribute.DEFENSE, def);
        set.put(Attribute.DEXTERITY, dex);
    }
    
    public AttributeSet() {
        this(null);
    }

    public int get(Attribute at) {
        return set.get(at);
    }

    public void put(Attribute att, int i) {
        set.put(att, i);
    }
}
