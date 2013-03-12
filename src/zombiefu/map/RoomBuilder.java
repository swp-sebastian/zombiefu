package zombiefu.map;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import zombiefu.util.Creator;
import jade.core.World;
import jade.gen.map.MapGenerator;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;

public class RoomBuilder extends MapGenerator {

	private static HashMap<Character, Color> charSet; // Das CharSet
	private static HashMap<Character, Boolean> passSet; // Das Passable-Set
	private ColoredChar[][] screen; // Das Gelände
	private int width;
	private int height;

	public static void setCharSet(String input) {
		charSet = new HashMap<Character, Color>();
		passSet = new HashMap<Character, Boolean>();
		String[] settings = Creator.getStrings(input);
		for (int i = 0; i < settings.length; i++) {
			String[] setting = settings[i].split(" ");
			if (setting.length > 2) {
				charSet.put(setting[0].charAt(0),
						Color.decode("0x" + setting[2]));
				passSet.put(setting[0].charAt(0), setting[1].equals("passable"));
			}
		}
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public RoomBuilder(String input, String charset) {
		try {
			setCharSet(charset);
			this.screen = Creator.readLevel(input);
			this.height = screen.length;
			this.width = screen[0].length;
		} catch (IOException e) {
			System.out.println("File not found!");
		}
	}
	
	@Override
	protected void generateStep(World world, Dice dice) {
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				try {
					if (charSet.containsKey(screen[x][y].ch())) {
						world.setTile(
								ColoredChar.create(screen[x][y].ch(),
										charSet.get(screen[x][y].ch())),
								passSet.get(screen[x][y].ch()), y, x);
					} else
						world.setTile(screen[x][y], y, x);
				} catch (NullPointerException e) {
					world.setTile(ColoredChar.create(' '), y, x);
				}

			}
		}
	}
}
