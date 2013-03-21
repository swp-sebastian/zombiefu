/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.human;

import jade.util.datatype.ColoredChar;
import java.util.Map;
import zombiefu.creature.AttributeSet;
import zombiefu.exception.CannotAffordException;
import zombiefu.items.Item;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;

/**
 *
 * @author tomas
 */
public class SellingHuman extends DealingHuman {

    private Item offerItem;
    private double requestMoney;

    public SellingHuman(ColoredChar face, String name, AttributeSet attSet, Item offerItem, double requestMoney, Map<String,String> phraseSet) {
        super(face, name, attSet, phraseSet);
        this.offerItem = offerItem;
        this.requestMoney = requestMoney;
    }

    @Override
    public boolean dealWith(Player pl) {
        char key = ZombieGame.askPlayerForKeyWithMessage("Möchtest Du mein " + offerItem.getName() + " für " + requestMoney + "€ kaufen? [y/n]");
        if (key != 'y') {
            return false;
        }
        try {
            pl.pay(requestMoney);
            pl.obtainItem(offerItem);
            return true;
        } catch (CannotAffordException ex) {
            ZombieGame.newMessage("Das kannst Du Dir nicht leisten.");
            return false;
        }
    }

    @Override
    protected String parsePhrase(String s) {
        s = s.replace("$OFFERITEM", offerItem.getName());
        return s;
    }

    @Override
    protected String getDefaultPhrase(String key) {
        if (key.equals("runDeal")) {
            return "Es war mir eine Freude, Geschäfte mit Dir zu machen.";
        } else if (key.equals("postDeal")) {
            return "Na, wie gefällt dir $OFFERITEM?";
        } else if (key.equals("default")) {
            return "Hallo!";
        }
        return null;
    }
    
}
