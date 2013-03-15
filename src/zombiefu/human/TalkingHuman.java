/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.human;

import jade.util.datatype.ColoredChar;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;

/**
 *
 * @author tomas
 */
public class TalkingHuman extends Human {
    
    private String aussage;

    public TalkingHuman(ColoredChar face, String name, String aussage) {
        super(face, name);
        this.aussage = aussage;
    }

    @Override
    public void talkToPlayer(Player pl) {
        ZombieGame.newMessage(getName() + " sagt: " + aussage);
    }
}
