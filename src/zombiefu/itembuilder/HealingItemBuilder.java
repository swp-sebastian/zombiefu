/* vim: ts=4 sw=4 et
 *
 * Copyright © 2012 ENDA GmbH & Co. KG,
 * Schillerstr. 106, 10625 Berlin, Germany
 *
 * All rights reserved.
 *
 * Any redistribution or reproduction of part or all of the contents in any form is
 * prohibited.
 *
 * You may not, except with our express written permission, distribute or commercially
 * exploit the content. Nor may you transmit it or store it in any other form of
 * electronic storage system.
 */
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
