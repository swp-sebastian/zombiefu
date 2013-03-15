/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.human;

import jade.util.datatype.ColoredChar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import zombiefu.exception.CanNotAffordException;
import zombiefu.exception.DoesNotPossessThisItemException;
import zombiefu.items.MensaCard;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;

/**
 *
 * @author tomas
 */
public abstract class DealingHuman extends Human {

    protected final String[] PHRASE_KEYS = new String[]{"default", "runDeal", "postDeal"};
    protected boolean dealt;
    private Map<String, String> phraseSet;

    public DealingHuman(ColoredChar face, String name, Map<String, String> ps) {
        super(face, name);
        this.dealt = false;
        this.phraseSet = ps;

        if (phraseSet == null) {
            phraseSet = new HashMap<String, String>();
        }
        for (String key : PHRASE_KEYS) {
            if (!phraseSet.containsKey(key)) {
                phraseSet.put(key, getDefaultPhrase(key));
            }
        }
    }

    protected abstract String getDefaultPhrase(String key);

    protected abstract boolean dealWith(Player pl);

    protected abstract String parsePhrase(String s);

    @Override
    public void talkToPlayer(Player pl) {
        if (dealt) {
            ZombieGame.newMessage(getName() + ": " + parsePhrase(phraseSet.get("postDeal")));
        } else {
            ZombieGame.newMessage(getName() + ": " + parsePhrase(phraseSet.get("default")));
            if (dealWith(pl)) {
                dealt = true;
                ZombieGame.newMessage(getName() + ": " + parsePhrase(phraseSet.get("runDeal")));
            }
        }
    }
}