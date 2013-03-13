
package zombiefu.itembuilder;

import jade.util.datatype.ColoredChar;
import zombiefu.creature.Door;
import zombiefu.items.Item;
import zombiefu.items.KeyCard;


public class KeyCardBuilder extends ItemBuilder {

    private Door door;
    
    public KeyCardBuilder(ColoredChar face, Door door) {
        this.face = face;
        this.door = door;
    }
    
    @Override
    public Item buildItem() {
        return new KeyCard(face, door);
    }
    
}
