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
    
    private String phrase;

    public TalkingHuman(ColoredChar face, String name, String phrase) {
        super(face, name);
        this.phrase = phrase;
    }

    @Override
    public void talkToPlayer(Player pl) {
        ZombieGame.newMessage(getName() + " sagt: " + phrase);
    }
}
