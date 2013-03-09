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
    public static char hit;
    public static char change;
    

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
            while (key == change){
            	nextItem(); //Durch das Item-Inventar zappen
            	key = term.getKey();
            }
            if (key == hit){
            	roundHouseKick(); //Mit gewähltem Item eine Aktion durchführen
            }
            else {
            switch(key)
            {
                case 'q':
                    expire();
                    break;
                case 'x':
                    roundHouseKick(); //Mit gewähltem Item eine Aktion durchführen
                    break;
                default:
                    Direction dir = Direction.keyToDir(key);
                    if(dir != null)
                        tryToMove(dir);
                    break;
            }
            }
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void nextItem(){ //Muss in die Item Klasse implementiert werden
    	System.out.println("Schluss mit Kettensäge.");
    }
    
    @Override
    public Collection<Coordinate> getViewField()
    {
        return fov.getViewField(world(), pos(), 5);
    }
}