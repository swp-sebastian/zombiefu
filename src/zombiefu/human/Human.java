package zombiefu.human;

import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import zombiefu.actor.Creature;
import zombiefu.items.Waffe;
import zombiefu.player.Player;
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
    public void killed(Creature killer) {
        if (killer == ZombieGame.getPlayer()) {
            ZombieGame.newMessage("Du hast einen Menschen getötet. Exmatrikulation!");
            killer.killed(null);
            
        }
    }

    @Override
    protected Direction getAttackDirection() {
        return null;
    }

    public abstract void talkToPlayer(Player pl);
    
}
