/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.human;

import jade.util.datatype.ColoredChar;
import java.util.Map;
import zombiefu.exception.CanNotAffordException;
import zombiefu.exception.DoesNotPossessThisItemException;
import zombiefu.items.Item;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;

/**
 *
 * @author tomas
 */
public class TradingHuman extends DealingHuman {

    private Item offerItem;
    private String requestItem;

    public TradingHuman(ColoredChar face, String name, Item offerItem, String requestItem, Map<String, String> phraseSet) {
        super(face, name, phraseSet);
        this.offerItem = offerItem;
        this.requestItem = requestItem;
    }

    public TradingHuman(ColoredChar face, String name, Item offerItem, String requestItem) {
        this(face, name, offerItem, requestItem, null);
    }

    @Override
    public boolean dealWith(Player pl) {
        char key = ZombieGame.askPlayerForKeyWithMessage("Möchtest Du mein/e " + offerItem.getName() + " gegen ein/e/n " + requestItem + " tauschen? [y/n]");
        if (key != 'y') {
            return false;
        }
        try {
            pl.removeItem(requestItem);
            pl.obtainItem(offerItem);
            return true;
        } catch (DoesNotPossessThisItemException ex) {
            ZombieGame.newMessage("Schade, Du hast das Item \"" + requestItem + "\" nicht, was ich haben möchte.");
            return false;
        }
    }

    @Override
    protected String parsePhrase(String s) {
        s = s.replace("$OFFERITEM", offerItem.getName());
        s = s.replace("$REQUESTITEM", requestItem);
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
