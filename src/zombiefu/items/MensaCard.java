/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.items;

import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.creature.Player;
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
        ZombieTools.sendMessage("Du hast eine MensaCard mit " + guthaben + "€ aufgehoben.", player.frame);
        player.addMoney(guthaben);
    }
}
