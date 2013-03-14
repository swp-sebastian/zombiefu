/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.items;

/**
 *
 * @author tomas
 */
public enum Waffentyp {

    NAHKAMPF(false, true),
    FERNKAMPF(true, true),
    UMKREIS(false, false),
    GRANATE(true, false);
    private boolean ranged;
    private boolean directed;

    private Waffentyp(boolean r, boolean d) {
        this.ranged = r;
        this.directed = d;
    }

    public boolean isRanged() {
        return ranged;
    }

    public boolean isDirected() {
        return directed;
    }

    public static Waffentyp getTypeFromString(String string) {
        for (Waffentyp w : Waffentyp.values()) {
            String s = w.toString().toLowerCase();
            s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
            if (s.equals(string)) {
                return w;
            }
        }
        throw new IllegalArgumentException("Ungültige Waffentypname: " + string);
    }
}
