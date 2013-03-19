/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.human;

import jade.util.datatype.ColoredChar;
import java.util.Map;
import zombiefu.items.Item;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;

/**
 *
 * @author tomas
 */
public class GivingHuman extends DealingHuman {

    private Item offerItem;

    public GivingHuman(ColoredChar face, String name, Item offerItem, Map<String,String> phraseSet) {
        super(face, name, phraseSet);
        this.offerItem = offerItem;
    }

    public GivingHuman(ColoredChar face, String name, Item offerItem) {
        this(face, name, offerItem, null);
    }

    @Override
    public boolean dealWith(Player pl) {
        pl.obtainItem(offerItem);
        return true;
    }

    @Override
    protected String parsePhrase(String s) {
        s = s.replace("$OFFERITEM", offerItem.getName());
        return s;
    }

    @Override
    protected String getDefaultPhrase(String key) {
        if (key.equals("runDeal")) {
            return "Viel Spaß damit, ich brauche eh kein $OFFERITEM!";
        } else if (key.equals("postDeal")) {
            return "Na, wie gefällt dir $OFFERITEM?";
        } else if (key.equals("default")) {
            return "Hallo!";
        }
        return null;
    }
}
