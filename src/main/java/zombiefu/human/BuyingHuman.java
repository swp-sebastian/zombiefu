/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.human;

import jade.util.datatype.ColoredChar;
import java.util.Map;
import zombiefu.creature.AttributeSet;
import zombiefu.exception.DoesNotPossessThisItemException;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;

/**
 *
 * @author tomas
 */
public class BuyingHuman extends DealingHuman {

    private String requestItem;
    private int offerMoney;

    public BuyingHuman(ColoredChar face, String name, AttributeSet attSet, String requestItem, int offerMoney, Map<String, String> phraseSet) {
        super(face, name, attSet, phraseSet);
        this.requestItem = requestItem;
        this.offerMoney = offerMoney;
    }

    @Override
    public boolean dealWith(Player pl) {
        char key = ZombieGame.askPlayerForKeyWithMessage("Darf ich Dir ein/e " + requestItem + " für " + offerMoney + "€ abkaufen? [y/n]");
        if (key != 'y') {
            return false;
        }
        try {
            pl.removeItem(requestItem);
            pl.addMoney(offerMoney);
            return true;
        } catch (DoesNotPossessThisItemException ex) {
            ZombieGame.newMessage("Schade, Du hast das Item " + requestItem + " nicht, was ich haben möchte.");
            return false;
        }
    }

    @Override
    protected String parsePhrase(String s) {
        s = s.replace("$REQUESTITEM", requestItem);
        return s;
    }

    @Override
    protected String getDefaultPhrase(String key) {
        if (key.equals("runDeal") || key.equals("postDeal")) {
            return "Juhu, jetzt kann ich wieder meine Miete bezahlen!";
        } else if (key.equals("default")) {
            return "Hallo!";
        }
        return null;
    }
}
