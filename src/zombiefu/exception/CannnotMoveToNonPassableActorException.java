/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.exception;

import zombiefu.actor.NotPassableActor;
        
/**
 *
 * @author tomas
 */
public class CannnotMoveToNonPassableActorException extends Exception {
    
    private NotPassableActor actor;

    public CannnotMoveToNonPassableActorException(NotPassableActor actor) {
        this.actor = actor;
    }
    
    public NotPassableActor getActor() {
        return actor;
    }
    
}
