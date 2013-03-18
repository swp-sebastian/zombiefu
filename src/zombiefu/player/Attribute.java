/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.player;

/**
 *
 * @author tomas
 */
public enum Attribute {
    MAXHP(10),
    ATTACK(1),
    DEFENSE(1),
    DEXTERITY(1);
    
    private int step;
    
    private Attribute(int step) {
        this.step = step;        
    }
    
    public int getStep() {
        return step;
    }
}
