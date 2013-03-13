package zombiefu.itembuilder;

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
    
}
