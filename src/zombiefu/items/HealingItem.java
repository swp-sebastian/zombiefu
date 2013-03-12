package zombiefu.items;

import jade.util.datatype.ColoredChar;
import zombiefu.creature.Player;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public class HealingItem extends ConsumableItem {

    private int heilkraft;

    public HealingItem(ColoredChar face, String name, int h) {
        super(face, name);
        heilkraft = h;
    }

    @Override
    public void getConsumedBy(Player pl) {
        ZombieGame.newMessage("'" + getName() + "' hat dich geheilt.");
        pl.heal(heilkraft);
    }
}
