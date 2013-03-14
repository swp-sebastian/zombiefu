/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.player;

import zombiefu.items.Waffentyp;

/**
 *
 * @author tomas
 */
public enum Discipline {
    
    POLITICAL_SCIENCE,
    COMPUTER_SCIENCE,
    MEDICINE,
    PHILOSOPHY,
    PHYSICS,
    BUSINESS,
    CHEMISTRY,
    SPORTS,
    MATHEMATICS;
    
    
    public static Discipline getTypeFromString(String string) {
        for (Discipline d : Discipline.values()) {
            if (d.toString().equals(string)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Ungültige Discipline-Name");
    }
    
}
