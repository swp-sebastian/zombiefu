package zombiefu.creature;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import zombiefu.items.Waffe;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public class Human extends Creature {

    public Human(ColoredChar face, String name) {
        super(face, name);
        godMode = true;
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
        // TODO: Spiel sofort beenden.
    }

    @Override
    protected Direction getAttackDirection() {
        return null;
    }
    
    public void talk(){
        ZombieGame.newMessage("Hallo, ich bin ein Mensch.");
    }
}
