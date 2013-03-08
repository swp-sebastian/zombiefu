/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.weapon;

/**
 *
 * @author tomas
 */
public class Weapon {

    private String name;
    private int damage;

    public Weapon(String n, int d) {
        name = n;
        damage = d;
    }
    
    public int getDamage() {
        return damage;
    }

    public String getName() {
        return name;
    }
}
