/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.items;

import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;

/**
 *
 * @author tomas
 */
public class MensaCard extends Item {

    private double guthaben;

    public MensaCard(double g) {
        super(ColoredChar.create('€', new Color(255, 215, 0)));
        guthaben = g;
    }

    @Override
    protected void pickedUpByPlayer(Player player) {
        ZombieGame.newMessage("Du hast eine MensaCard mit " + guthaben + "€ aufgehoben.");
        player.addMoney(guthaben);
        expire();
    }
}
