package zombiefu.items;

import jade.util.datatype.ColoredChar;
import zombiefu.creature.MaximumHealthPointException;
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
        try {
            pl.heal(heilkraft);
            ZombieGame.newMessage("'" + getName() + "' hat dich geheilt.");
        } catch (MaximumHealthPointException e) {
            ZombieGame.newMessage("Du brauchst dich nicht zu heilen.");
            
        }
    }
}
