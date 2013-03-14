/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.player;

/**
 *
 * @author tomas
 */
public enum Attribut {
    MAXHP(10),
    ATTACK(1),
    DEFENSE(1),
    INTELLIGENCE(1);
    
    private int step;
    
    private Attribut(int step) {
        this.step = step;        
    }
    
    public int getStep() {
        return step;
    }
}
