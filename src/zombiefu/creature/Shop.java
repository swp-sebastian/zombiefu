package zombiefu.creature;

import zombiefu.items.ConsumableItem;
import zombiefu.util.ZombieGame;
import jade.util.datatype.ColoredChar;
import java.util.HashMap;
import zombiefu.itembuilder.ItemBuilder;

public class Shop extends Human {

    private HashMap<ItemBuilder, Integer> items;
    String name;

    public Shop(ColoredChar face, String name, HashMap<ItemBuilder, Integer>  inventar) {
        super(face, name);
        this.items = inventar;
        this.name = name;
    }

    @Override
    public void talk() {
        ZombieGame.newMessage("Ich bin kaputt.");
        /*
        ZombieGame.newMessage("Willkommen im Mensa-Shop. Hier unser Angebot:");
        ConsumableItem item = ZombieGame.askPlayerForItem(items);
        try {
            ZombieGame.getPlayer().obtainItem(item);
        } catch (IllegalStateException exc) {
        }
        ZombieGame.newMessage("Bitte beehren sie uns bald wieder.");
        * */
    }

}
