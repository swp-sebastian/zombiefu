package zombiefu.creature;

import java.util.ArrayList;
import zombiefu.items.ConsumableItem;
import zombiefu.util.ConfigHelper;
import zombiefu.util.ZombieGame;
import jade.util.datatype.ColoredChar;
import zombiefu.util.ZombieTools;

public class Shop extends Human {

    private ArrayList<ConsumableItem> items;

    public Shop(ColoredChar face, String name, ArrayList<ConsumableItem> inventar) {
        super(face, name);
        this.items = inventar;
    }
    
    public static Shop newShop(String name) {
        ColoredChar face = null;
        ArrayList<ConsumableItem> items = new ArrayList<ConsumableItem>();
        switch(name) {
            case "Mensa-Automat":
                face = ColoredChar.create('M');
                items.add((ConsumableItem) ConfigHelper.newItemByName("Kaffee"));
                break;
            case "Cafete":
                face = ColoredChar.create('C');
                items.add((ConsumableItem) ConfigHelper.newItemByName("Kaffee"));
                break;
            case "Waffenautomat":
                face = ColoredChar.create('W');
                items.add((ConsumableItem) ConfigHelper.newItemByName("Kaffee"));
                break;     
            default:
                ZombieTools.stopWithFatalError("Shop.newShop(): Ungültiger Shop");
        }
        return new Shop(face,name,items);
    }

    @Override
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
