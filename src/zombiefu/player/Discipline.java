/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.player;

import java.util.HashMap;

/**
 *
 * @author tomas
 */
public enum Discipline {
//HP, a, v, geschick, geld, waffe
    POLITICAL_SCIENCE(100, 5, 5, 5, 0, "weapon(Totquatschen)"),
    COMPUTER_SCIENCE(100, 5, 7, 5, 0, "weapon(Laptop)"),
    MEDICINE(120, 5, 5, 5, 0, "weapon(Faust)"),
    PHILOSOPHY(100, 5, 5, 5, 0, "weapon(Suizid)"),
    PHYSICS(100, 5, 5, 5, 0, "weapon(Faust)"),
    BUSINESS(100, 3, 3, 3, 100, "weapon(Besen)"),
    CHEMISTRY(100, 5, 5, 5, 0, "weapon(Faust)"),
    SPORTS(100, 7, 5, 5, 0, "weapon(Faust)"),
    MATHEMATICS(100, 5, 5, 5, 0, "weapon(Differenzierer)");
    
    private HashMap<Attribute, Integer> baseAttributes;
    private String baseItems;
    private int baseMoney;

    private Discipline(int hp, int att, int def, int dex, int money, String items) {
        baseMoney = money;
        baseItems = items;
        baseAttributes = new HashMap<>();
        baseAttributes.put(Attribute.MAXHP, hp);
        baseAttributes.put(Attribute.ATTACK, att);
        baseAttributes.put(Attribute.DEFENSE, def);
        baseAttributes.put(Attribute.DEXTERITY, dex);
    }

    public static Discipline getTypeFromString(String string) {
        for (Discipline d : Discipline.values()) {
            if (d.toString().equals(string)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Ungültige Discipline-Name: " + string);
    }

    public int getBaseAttribute(Attribute att) {
        return baseAttributes.get(att);
    }
    
    public String getItems() {
        return baseItems;
    }
    
    public int getMoney() {
        return baseMoney;
    }
}
