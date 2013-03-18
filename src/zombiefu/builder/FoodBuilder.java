package zombiefu.builder;

import jade.util.datatype.ColoredChar;
import zombiefu.items.Food;
import zombiefu.items.Item;

/**
 *
 * @author tba
 */
public class FoodBuilder extends ItemBuilder {

    private int heilkraft;

    public FoodBuilder(ColoredChar face, String name, int h) {
        this.face = face;
        this.name = name;
        this.heilkraft = h;
    }

    @Override
    public Item buildItem() {
        return new Food(face,name,heilkraft);
    }
}
