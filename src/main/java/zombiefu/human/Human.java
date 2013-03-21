package zombiefu.human;

import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombiefu.creature.AttributeSet;
import zombiefu.creature.Creature;
import zombiefu.creature.NonPlayer;
import zombiefu.exception.NoPlaceToMoveException;
import zombiefu.items.Weapon;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;

public abstract class Human extends NonPlayer {

    public Human(ColoredChar face, String name, AttributeSet attSet, double maxDistance) {
        super(face, name, attSet, maxDistance);
    }
    
    @Override
    public void pleaseAct() {
        try {
            moveRandomly();
        } catch (NoPlaceToMoveException ex) {
        }
    }

    @Override
    public Weapon getActiveWeapon() {
        return null;
    }

    @Override
    public void kill(Creature killer) {
        if (killer == ZombieGame.getPlayer()) {
            ZombieGame.newMessage("Du hast einen Menschen getötet. Exmatrikulation!");
            killer.kill(null);
        }
    }

    @Override
    protected Direction getAttackDirection() {
        return null;
    }

    public abstract void talkToPlayer(Player pl);
    
    @Override
    public boolean isEnemy(Creature c) {
        return false;
    }
    
    @Override
    public boolean hasUnlimitedMunition() {
        return true;
    }
}
