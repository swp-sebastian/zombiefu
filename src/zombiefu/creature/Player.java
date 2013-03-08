package zombiefu.creature;

import java.util.Collection;
import jade.fov.RayCaster;
import jade.fov.ViewField;
import jade.ui.Camera;
import jade.ui.Terminal;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import zombiefu.weapon.Weapon;

public class Player extends Creature implements Camera
{
    private Terminal term;
    private ViewField fov;
    private int intelligenceValue;
    private int money;
    private int ects;
    private int semester;
    private int maximalHealthPoints;

    public Player(Terminal term)
    {
        super(ColoredChar.create('@'),"Player", 10,1,1,new Weapon("Kettensäge",1));
        this.term = term;
        fov = new RayCaster();
    }

    @Override
    public void act()
    {
        try
        {
            char key;
            key = term.getKey();
            switch(key)
            {
                case 'q':
                    expire();
                    break;
                case 'x':
                    roundHouseKick();
                    break;
                default:
                    Direction dir = Direction.keyToDir(key);
                    if(dir != null)
                        tryToMove(dir);
                    break;
            }
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Coordinate> getViewField()
    {
        return fov.getViewField(world(), pos(), 5);
    }
}