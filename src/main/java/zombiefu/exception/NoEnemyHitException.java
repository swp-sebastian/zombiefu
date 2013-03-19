/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.exception;

import zombiefu.fight.Attack;

/**
 *
 * @author tomas
 */
public class NoEnemyHitException extends Exception {

    Attack at;
    
    public NoEnemyHitException(Attack at) {
        this.at = at;
    }
    
    public void close() {
        at.close();
    }
    
}
