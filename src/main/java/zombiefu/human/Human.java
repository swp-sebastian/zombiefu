package zombiefu.human;

import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import java.util.HashMap;
import zombiefu.actor.Creature;
import zombiefu.actor.NonPlayer;
import zombiefu.items.Weapon;
import zombiefu.player.Attribute;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;

public abstract class Human extends NonPlayer {

    public Human(ColoredChar face, String name, HashMap<Attribute,Integer> attSet) {
        super(face, name, attSet, 0);
    }
    
    @Override
    public void pleaseAct() {
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
}