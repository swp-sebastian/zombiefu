package zombiefu.items;

import jade.util.datatype.ColoredChar;
import java.awt.Color;
import zombiefu.creature.Door;
import zombiefu.creature.Player;
import zombiefu.util.ZombieGame;

public class KeyCard extends ConsumableItem {

    private final Door door;

    public KeyCard(Door door) {
        super(ColoredChar.create('D', Color.RED), "KeyCard (" + door.getName() + ")");
        this.door = door;
    }

    public void getConsumedBy(Player pl) throws CannotBeConsumedException {
        if (pl.world() != door.world()) {
        } else if (door.pos().distance(pl.pos()) > 1) {
            ZombieGame.newMessage("Der Schlüssel kann nicht benutzt werden.");
            throw new CannotBeConsumedException();
        }
        else {
            pl.world().removeActor(door);
            ZombieGame.newMessage("Du konntest die Tür "+door.getName()+" aufschließen.");
        }

    }
}
