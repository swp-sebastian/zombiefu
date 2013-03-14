package zombiefu.creature;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import zombiefu.items.Waffe;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public class Human extends Creature {

    public Human(ColoredChar face, String name) {
        super(face, name,1,1,1);
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
        if (killer==ZombieGame.getPlayer()){
            ZombieGame.newMessage("Du hast einen Menschen getötet.");
            killer.expire();
        }
    }

    @Override
    protected Direction getAttackDirection() {
        return null;
    }
    
    public void talk(){
        ZombieGame.newMessage("Hallo, ich bin ein Mensch.");
    }
}
