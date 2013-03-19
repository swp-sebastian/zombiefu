package zombiefu.builder;

import jade.util.datatype.ColoredChar;
import zombiefu.items.Item;

/**
 *
 * @author tomas
 */
public abstract class ItemBuilder {

    protected ColoredChar face;
    protected String name;

    public abstract Item buildItem();

    public ColoredChar face() {
        return face;
    }

    public String getName() {
        return name;
    }
}
