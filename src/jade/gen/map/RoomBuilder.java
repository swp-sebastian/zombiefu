package jade.gen.map;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import rogue.screen.Screen;
import jade.core.World;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;

public class RoomBuilder extends MapGenerator {
	
	private static HashMap<Character,ColoredChar> charSet; 	// Das CharSet
	private String input;									// Eingabe-File des Geländes
	
	public static void setCharSet(String input){
		charSet = new HashMap<Character,ColoredChar>();
		String[] settings = Screen.getStrings(input);
		for (int i=0;i<settings.length;i++){
			String[] setting = settings[i].split(" ");
			if (setting.length>2)
				charSet.put(setting[0].charAt(0),(ColoredChar.create(setting[0].charAt(0), 
						Color.decode("0x"+setting[2]), setting[1].equals("passable"))));
		}
	}
	
	public RoomBuilder(String input){
		this.input = input;
	}
	
	protected void generateStep(World world, Dice dice){
		try {
			ColoredChar[][] screen = Screen.readFile(input);
			for (int x=0;x<screen.length;x++){
				for (int y=0;y<screen[x].length;y++){
					if (charSet.containsKey(screen[x][y].ch()))
						world.setTile(charSet.get(screen[x][y].ch()),y,x);
					else
						world.setTile(screen[x][y],y,x);
				}
			}
		} catch (IOException e) {System.out.println("File not found!");}
		
	}

}
