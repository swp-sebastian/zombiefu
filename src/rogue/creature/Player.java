package rogue.creature;

import java.util.Collection;
import jade.fov.RayCaster;
import jade.fov.ViewField;
import jade.ui.Camera;
import jade.ui.Terminal;
import jade.ui.TiledTermPanel;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import rogue.screen.Screen;

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
        super(ColoredChar.create('@'));
        this.name = "Player";
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
                        move(dir);
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
