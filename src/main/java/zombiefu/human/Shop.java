package zombiefu.human;

import zombiefu.util.ZombieGame;
import jade.util.datatype.ColoredChar;
import java.util.HashMap;
import zombiefu.exception.CannotAffordException;
import zombiefu.builder.ItemBuilder;
import zombiefu.creature.AttributeSet;
import zombiefu.player.Player;

public class Shop extends Human {

    private ShopInventar inventar;

    public Shop(ColoredChar face, String name, ShopInventar inventar) {
        super(face, name, new AttributeSet());
        this.inventar = inventar;
    }

    @Override
    public void talkToPlayer(Player pl) {
        ZombieGame.newMessage("Herzlich Willkommen. Hier ist unser Angebot:");
        ZombieGame.setTopFrameContent(name);
        ItemBuilder item = ZombieGame.askPlayerForItemToBuy(inventar);
        try {
            if (item!=null){
                pl.pay(inventar.get(item));
                pl.obtainItem(item.buildItem());
            }
            ZombieGame.newMessage("Bitte beehren sie uns bald wieder.");
        } catch (CannotAffordException ex) {
            ZombieGame.newMessage("Das kannst Du Dir nicht leisten.");
        }
    }
}
