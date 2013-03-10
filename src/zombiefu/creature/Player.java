package zombiefu.creature;

import jade.core.World;
import java.awt.Color;
import java.util.Collection;

import zombiefu.items.Waffe;
import jade.fov.RayCaster;
import jade.fov.ViewField;
import jade.ui.Camera;
import jade.ui.TermPanel;
import jade.ui.Terminal;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import zombiefu.items.Teleporter;
import zombiefu.level.Level;
import zombiefu.map.RoomBuilder;

public class Player extends Creature implements Camera
{
    private TermPanel term;
    private ViewField fov;
    //private int intelligenceValue;
    //private int money;
    //private int ects;
    //private int semester;
    //private int maximalHealthPoints;

    public Player(TermPanel term)
    {
        super(ColoredChar.create('\u263B',Color.decode("0x7D26CD")),"John Dorian", 10,1,1,new Waffe("Kettensäge",1,ColoredChar.create('|')));
        this.term = term;
        this.godMode = true;
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
                case 'g':
                    changeWorld("src/sources/TestRaumZ.txt");
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
    
    public void changeWorld(World world) {
        world().removeActor(this);
        world.addActor(this);
    }
    public void changeWorld(String level) {
       changeWorld(Level.levelFromFile(level));
    }
}
