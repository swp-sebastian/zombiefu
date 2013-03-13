package zombiefu.itembuilder;

import jade.util.datatype.ColoredChar;
import zombiefu.items.HealingItem;
import zombiefu.items.Item;

/**
 *
 * @author tba
 */
public class HealingItemBuilder extends ItemBuilder {

    private int heilkraft;

    public HealingItemBuilder(ColoredChar face, String name, int h) {
        this.face = face;
        this.name = name;
        this.heilkraft = h;
    }

    @Override
    public Item buildItem() {
        return new HealingItem(face,name,heilkraft);
    }
}
