package zombiefu.creature;

import java.util.ArrayList;
import zombiefu.items.ConsumableItem;
import zombiefu.util.ConfigHelper;
import zombiefu.util.ZombieGame;
import jade.util.datatype.ColoredChar;

public class Shop extends Human {

    private ArrayList<ConsumableItem> items;

    public Shop(ColoredChar face, ArrayList<ConsumableItem> items) {
        super(face, "Mensa-Shop");
        this.items = items;
    }

    public Shop(ColoredChar face) {
        super(face, "Mensa-Shop");
        this.items = new ArrayList<ConsumableItem>();
        items.add((ConsumableItem) ConfigHelper.newItemByName("Kaffee"));
    }

    public void talk() {
        ZombieGame.newMessage("Willkommen im Mensa-Shop. Hier unser Angebot:");
        ConsumableItem item = ZombieGame.askPlayerForItem(items);
        try {
            ZombieGame.getPlayer().obtainItem(item);
        } catch (IllegalStateException exc) {
        }
        ZombieGame.newMessage("Bitte beehren sie uns bald wieder.");
    }

}
