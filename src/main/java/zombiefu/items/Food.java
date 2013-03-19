package zombiefu.items;

import jade.util.datatype.ColoredChar;
import zombiefu.exception.CannotBeConsumedException;
import zombiefu.exception.MaximumHealthPointException;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;

public class Food extends ConsumableItem {

    private int heilkraft;

    public Food(ColoredChar face, String name, int h) {
        super(face, name);
        heilkraft = h;
    }

    @Override
    public void getConsumedBy(Player pl) throws CannotBeConsumedException {
        try {
            pl.heal(heilkraft);
            ZombieGame.newMessage("'" + getName() + "' hat dich geheilt.");
        } catch (MaximumHealthPointException e) {
            ZombieGame.newMessage("Du brauchst dich nicht zu heilen.");
            throw new CannotBeConsumedException();
        }
    }
}
