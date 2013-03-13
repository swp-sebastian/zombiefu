
package zombiefu.itembuilder;

import zombiefu.creature.Door;
import zombiefu.items.Item;
import zombiefu.items.KeyCard;


public class KeyCardBuilder extends ItemBuilder {

    private Door door;
    
    public KeyCardBuilder(Door door) {
        this.door = door;
    }
    
    @Override
    public Item buildItem() {
        return new KeyCard(door);
    }
    
}
