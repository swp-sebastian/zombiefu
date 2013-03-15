/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.human;

import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombiefu.exception.CanNotAffordException;
import zombiefu.exception.DoesNotPossessThisItemException;
import zombiefu.items.Item;
import zombiefu.items.MensaCard;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;

/**
 *
 * @author tomas
 */
public class ItemGivingHuman extends TalkingHuman {

    private Item giftItem;
    private String wishItem;
    private int cost;
    private boolean done;
    private String sayWhenDone;

    public ItemGivingHuman(ColoredChar face, String name, String aussage, Item giftItem, int cost, String wishItem) {
        super(face, name, aussage);
        Guard.validateArgument(cost == 0 || wishItem == null);
        this.giftItem = giftItem;
        this.wishItem = wishItem;
        this.cost = cost;
        this.done = false;
    }

    public ItemGivingHuman(ColoredChar face, String name, String aussage, Item giftItem, int cost) {
        this(face, name, aussage, giftItem, cost, null);

    }

    public ItemGivingHuman(ColoredChar face, String name, String aussage, Item giftItem, String wishItem) {
        this(face, name, aussage, giftItem, 0, wishItem);

    }

    public ItemGivingHuman(ColoredChar face, String name, String aussage, Item giftItem) {
        this(face, name, aussage, giftItem, 0, null);
    }

    @Override
    public void talkToPlayer(Player pl) {
        if (done) {
            ZombieGame.newMessage(sayWhenDone);
            return;
        }

        super.talkToPlayer(pl);

        if (cost == 0 && wishItem == null) {
            pl.obtainItem(giftItem);
            sayWhenDone = "Na, wie gefällt dir " + giftItem.getName() + "?";
            done = true;
            ZombieGame.newMessage("Viel Spaß damit, ich brauche eh kein " + giftItem.getName() + "!");
        } else if (wishItem == null) {
            char key = ZombieGame.askPlayerForKeyWithMessage("Möchtest Du mein " + giftItem.getName() + " für " + cost + "€ kaufen? [y/n]");
            if (key != 'y') {
                return;
            }
            try {
                pl.pay(cost);
                pl.obtainItem(giftItem);
                sayWhenDone = "Na, wie gefällt dir " + giftItem.getName() + "?";
                done = true;
                ZombieGame.newMessage("Es war mir eine Freude, Geschäfte mit Dir zu machen.");
            } catch (CanNotAffordException ex) {
                ZombieGame.newMessage("Das kannst Du Dir nicht leisten.");
            }
        } else if (cost == 0) {
            if (giftItem instanceof MensaCard) {
                MensaCard card = (MensaCard) giftItem;
                char key = ZombieGame.askPlayerForKeyWithMessage("Darf ich Dir ein/e " + wishItem + " für " + card.getValue() + "€ abkaufen? [y/n]");
                if (key != 'y') {
                    return;
                }
                try {
                    pl.removeItem(wishItem);
                    pl.addMoney(card.getValue());
                    sayWhenDone = "Juhu, jetzt kann ich wieder meine Miete bezahlen!";
                    card.expire();
                    done = true;
                    ZombieGame.newMessage("Juhu, jetzt kann ich wieder meine Miete bezahlen!");
                } catch (DoesNotPossessThisItemException ex) {
                    ZombieGame.newMessage("Schade, Du hast das Item \"" + giftItem + "\" nicht, was ich haben möchte.");
                }
            } else {
                char key = ZombieGame.askPlayerForKeyWithMessage("Möchtest Du mein/e " + giftItem.getName() + " gegen ein/e/n " + wishItem + " tauschen? [y/n]");
                if (key != 'y') {
                    return;
                }
                try {
                    pl.removeItem(wishItem);
                    pl.obtainItem(giftItem);
                    done = true;
                    sayWhenDone = "Na, wie gefällt dir " + giftItem.getName() + "?";
                    ZombieGame.newMessage("Super, ich wollte schon immer mal ein/e " + wishItem + " haben!");
                } catch (DoesNotPossessThisItemException ex) {
                    ZombieGame.newMessage("Schade, Du hast das Item \"" + giftItem + "\" nicht, was ich haben möchte.");
                }
            }
        }
    }
}
