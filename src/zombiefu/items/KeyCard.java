package zombiefu.items;

import jade.util.datatype.ColoredChar;
import zombiefu.creature.Door;
import zombiefu.creature.Player;
import zombiefu.util.ZombieGame;

public class KeyCard extends ConsumableItem {

    private final Door door;

    public KeyCard(Door door) {
        super(ColoredChar.create('S'), "KeyCard (" + door.getName() + ")");
        this.door = door;
    }

    public void getConsumedBy(Player pl) throws CannotBeConsumedException {
        if (pl.world()==door.world()){
            
        }
        else{
            ZombieGame.newMessage("Der Schlüssel kann nicht benutzt werden.");
            throw new CannotBeConsumedException();
        }

    }
}
