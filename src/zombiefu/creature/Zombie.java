package zombiefu.creature;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import zombiefu.items.Item;
import zombiefu.items.MensaCard;
import zombiefu.items.Waffe;
import zombiefu.ki.Dijkstra;
import zombiefu.util.ZombieTools;

public class Zombie extends Monster {

    public Zombie() {
        super(ColoredChar.create('\u263F', ZombieTools.getRandomColor()), "Zombie", Dice.global
                .nextInt(1, 20), Dice.global.nextInt(1, 20), Dice.global
                .nextInt(1, 20), new Waffe(ColoredChar.create('|'), "Kralle", 2), new Dijkstra());
    }

    @Override
    protected Item itemDroppedOnKill() {
        if (Dice.global.chance(85)) {
            return new MensaCard(Dice.global.nextInt(1, 100));
        } else {
            return null;
        }
    }
}
