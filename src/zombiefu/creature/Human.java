package zombiefu.creature;

import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import zombiefu.items.Waffe;
import zombiefu.util.ZombieGame;

public abstract class Human extends Creature {

    public Human(ColoredChar face, String name) {
        super(face, name, 1, 1, 1);
    }

    @Override
    public void act() {
    }

    @Override
    public Waffe getActiveWeapon() {
        return null;
    }

    @Override
    protected void killed(Creature killer) {
        if (killer == ZombieGame.getPlayer()) {
            ZombieGame.newMessage("Du hast einen Menschen getötet. Exmatrikulation!");
            killer.expire();
        }
    }

    @Override
    protected Direction getAttackDirection() {
        return null;
    }

    public abstract void talkToPlayer(Player pl);
    
}
