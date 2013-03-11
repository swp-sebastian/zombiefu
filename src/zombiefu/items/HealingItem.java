package zombiefu.items;

import jade.util.datatype.ColoredChar;
import zombiefu.creature.Creature;
import zombiefu.creature.Player;

public class HealingItem extends ConsumableItem {

    private int heilkraft;

    public HealingItem(ColoredChar face, String name, int h) {
        super(face, name);
        heilkraft = h;
    }

    @Override
    public void getConsumedBy(Player pl) {
        pl.heal(heilkraft);
    }
}
