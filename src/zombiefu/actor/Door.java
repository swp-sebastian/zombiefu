package zombiefu.actor;

import jade.util.datatype.ColoredChar;
import java.awt.Color;

public class Door extends NotPassableActor {

    private final String name;
    
    public Door(String name) {
        super(ColoredChar.create('\u9911', Color.yellow));
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    
    public void act() {}

}
