package zombiefu.creature;

import jade.core.Actor;
import jade.util.datatype.ColoredChar;

public class Door extends NotPassableActor {

    private final String name;
    
    public Door(ColoredChar face, String name) {
        super(face);
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    
    public void act() {}

}
