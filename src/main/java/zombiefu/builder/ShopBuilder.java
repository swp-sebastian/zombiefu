/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.builder;

import jade.util.datatype.ColoredChar;
import java.util.HashMap;
import zombiefu.human.Shop;
import zombiefu.human.ShopInventar;

/**
 *
 * @author tomas
 */
public class ShopBuilder {

    private ColoredChar face;
    private String name;
    private ShopInventar items;

    public ShopBuilder(ColoredChar face, String name, ShopInventar items) {
        this.face = face;
        this.name = name;
        this.items = items;
    }

    public Shop buildShop() {
        return new Shop(face, name, items);
    }
}
