/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.items;

import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

/**
 *
 * @author tomas
 */
public class MensaCard extends Item {

    private int guthaben;

    public MensaCard(int g) {
        super(ColoredChar.create('€', new Color(255, 215, 0)));
        guthaben = g;
    }

    @Override
    protected void pickedUpByPlayer(Player player) {
        ZombieGame.newMessage("Du hast eine MensaCard mit " + ZombieTools.getMoneyString(guthaben) + " aufgehoben.");
        player.addMoney(guthaben);
        expire();
    }
}
