/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.human;

import jade.util.datatype.ColoredChar;
import java.util.Map;
import zombiefu.creature.AttributeSet;
import zombiefu.items.Item;
import zombiefu.player.Player;

/**
 *
 * @author tomas
 */
public class GivingHuman extends DealingHuman {

    private Item offerItem;

    public GivingHuman(ColoredChar face, String name, AttributeSet attSet, Item offerItem, Map<String,String> phraseSet, double maxDistance) {
        super(face, name, attSet, phraseSet, maxDistance);
        this.offerItem = offerItem;
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
