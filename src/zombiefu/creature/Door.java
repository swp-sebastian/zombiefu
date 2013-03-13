package zombiefu.creature;

import jade.core.Actor;
import jade.util.datatype.ColoredChar;

public class Door extends NotPassableActor {

    private final String name;
    
    public Door(String name) {
        super(ColoredChar.create('D'));
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    
    public void act() {}

}
