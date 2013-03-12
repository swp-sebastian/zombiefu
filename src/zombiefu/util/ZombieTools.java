package zombiefu.util;

import java.util.HashMap;

import jade.core.World;
import jade.ui.TermPanel;
import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;
import zombiefu.creature.Player;
import zombiefu.items.HealingItem;
import zombiefu.items.Item;
import zombiefu.items.Teleporter;
import zombiefu.items.Waffe;
import zombiefu.items.Waffentyp;
import zombiefu.level.Level;
import zombiefu.ui.ZombieFrame;

public class ZombieTools {

	public static Player activePlayer;
	private static final String srcs = "src/sources/";

	public static void createStoryForPlayer(Player player) {
		Level world = createWorld();
		world.addActor(player);
		try {
			world.fillWithEnemies();
			world.fillWithItems();
		} catch (TargetIsNotInThisWorldException e) {
		}
	}

	public static void createBidirectionalTeleporter(World world1,
			Coordinate from1, Coordinate to2, World world2, Coordinate from2,
			Coordinate to1) {
		/*
		 * fromi: Wo befindet sich der Teleporter in Welt i? toi: Wo soll der
		 * Player in Welt i hinteleportiert werden?
		 */
		Teleporter tel1 = new Teleporter(world2, to2);
		Teleporter tel2 = new Teleporter(world1, to1);
		world1.addActor(tel1, from1);
		world2.addActor(tel2, from2);
	}

	private static HashMap<String, Item> createItems() {
		HashMap<String, Item> itemMap = new HashMap<String, Item>();
		String[] waffen = getStrings(srcs + "Waffen.txt");
		String[] healingItems = getStrings(srcs + "HealingItems.txt");
		for (String s : waffen) {
			try {
				String[] st = s.split(" ");
				ColoredChar chr = ColoredChar.create(st[1].charAt(0),
						Color.decode("0x" + st[2]));
				if (st[3].equals("Nahkampf"))
					itemMap.put(st[0],
							new Waffe(chr, st[0], Integer.decode(st[4]),
									Waffentyp.NAHKAMPF));
				else if (st[3].equals("Fernkampf"))
					itemMap.put(st[0],
							new Waffe(chr, st[0], Integer.decode(st[4]),
									Integer.decode(st[5]), Waffentyp.FERNKAMPF));
				else if (st[3].equals("Umkreis"))
					itemMap.put(st[0],
							new Waffe(chr, st[0], Integer.decode(st[4]),
									Integer.decode(st[5]), Waffentyp.UMKREIS));
				else
					itemMap.put(
							st[0],
							new Waffe(chr, st[0], Integer.decode(st[4]),
									Integer.decode(st[5]), Integer
											.decode(st[6]), Waffentyp.GRANATE));
			} catch (Exception e) {
			}
		}
		for (String s : healingItems) {
			try {
				String[] st = s.split(" ");
				itemMap.put(
						st[0],
						new HealingItem(ColoredChar.create(st[2].charAt(0),
								Color.decode("0x" + st[3])), st[0], Integer
								.decode(st[1])));
			} catch (Exception e) {
			}
		}
		return itemMap;
	}

	private static HashMap<Character,String> createItemMap(){
		HashMap<Character,String> itemMap = new HashMap<Character,String>();
		return itemMap;
	}
	
	private static Level createWorld() {
		HashMap<String, Item> items = createItems();
		HashMap<Character,String> itemMap = createItemMap();
		String[] levels = getStrings(srcs + "levels.txt");
		String[] teles = getStrings(srcs + "teleporters.txt");
		HashMap<String, Level> nameOfLevels = new HashMap<String, Level>();
		for (String s : levels) {
			nameOfLevels.put(s, Level.levelFromFile(srcs + s + ".txt"));
		}
		for (String s : teles) {
			try {
				String[] d = s.split(" ");
				Level world1 = nameOfLevels.get(d[0]);
				Coordinate from1 = new Coordinate(Integer.decode(d[1]),
						Integer.decode(d[2]));
				Coordinate to2 = new Coordinate(Integer.decode(d[3]),
						Integer.decode(d[4]));
				Level world2 = nameOfLevels.get(d[5]);
				Coordinate from2 = new Coordinate(Integer.decode(d[6]),
						Integer.decode(d[7]));
				Coordinate to1 = new Coordinate(Integer.decode(d[8]),
						Integer.decode(d[9]));
				createBidirectionalTeleporter(world1, from1, to2, world2,
						from2, to1);
			} catch (Exception e) {
			}
		}
		return nameOfLevels.get(levels[0]);
	}
	
	// Liest eine Datei im UTF-16 Format ein und gibt das 2-dim Feld in ColoredChars zurück
    public static ColoredChar[][] readFile(String input) throws IOException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(input),"UTF-16");
        BufferedReader text = new BufferedReader(reader);
        LinkedList<String> lines = new LinkedList<String>();
        String temp;
        while((temp = text.readLine())!=null) {
                lines.add(temp);
	}
        text.close();
        reader.close();

        ColoredChar[][] chars = new ColoredChar[lines.size()][lines.get(0).length()];
        for (int i=0;i<lines.size();i++) {
	    for (int j=0;j<lines.get(i).length();j++) {
                chars[i][j] = ColoredChar.create(lines.get(i).charAt(j), Color.white);
	    }
        }
        return chars;
    }

    public static void showImage(TermPanel term, String input) throws InterruptedException{
        try {
            ColoredChar[][] start = readFile(input);
            term.clearBuffer();
            for(int x = 0; x < term.DEFAULT_COLS; x++) {
                for(int y = 0; y < term.DEFAULT_ROWS; y++) {
                    if (y >= start.length || x >= start[0].length) {
                        term.bufferChar(x,y,ColoredChar.create(' '));
                    } else {
                        term.bufferChar(x, y, start[y][x]);
                    }
                }
            }
            term.refreshScreen();
            term.getKey();
        } catch (IOException e) {
            System.out.println("Datei nicht gefunden.");
        }
    }

    public static String[] getStrings(String input){
    	LinkedList<String> lines = new LinkedList<String>();
		try {
			InputStreamReader  reader = new InputStreamReader(new FileInputStream(input),"UTF-16");
			BufferedReader text = new BufferedReader(reader);
	        String temp;
	        while((temp = text.readLine())!=null)
	                lines.add(temp);
	        text.close();
	        reader.close();
		} catch (Exception e) {}
		String[] erg = new String[lines.size()];
        for (int i = 0;i<lines.size();i++)
        	erg[i] = lines.get(i);
        return erg;
    }

	public static List<Direction> getAllowedDirections() {
		return Arrays.asList(Direction.SOUTH, Direction.EAST, Direction.WEST,
				Direction.NORTH);
	}

	public static Direction getRandomDirection() {
		return Dice.global.choose(getAllowedDirections());
	}

	public static void setTopTermContent(String s, ZombieFrame frame) {
		frame.topTerm().clearBuffer();
		frame.topTerm().bufferString(0, 0, s);
		frame.topTerm().refreshScreen();
	}

	public static void clearTopTerm(ZombieFrame frame) {
		frame.topTerm().clearBuffer();
		frame.topTerm().refreshScreen();
	}

	public static Direction askForDirection(ZombieFrame frame) {
		setTopTermContent("Bitte gib die Richtung an.", frame);
		Direction d = null;
		try {
			while (d == null) {
				d = Direction.keyToDir(frame.mainTerm().getKey());
			}
		} catch (InterruptedException ex) {
		}
		clearTopTerm(frame);
		return d;
	}

	public static void sendMessage(String s, ZombieFrame frame) {
		activePlayer.refreshWorld();
		setTopTermContent(s, frame);
		char key = 0;
		try {
			while (key != '\n') {
				key = frame.mainTerm().getKey();
			}
		} catch (InterruptedException ex) {
		}
		clearTopTerm(frame);
	}

	public static void sendMessage(String string) {
		Guard.argumentIsNotNull(activePlayer);
		sendMessage(string, activePlayer.frame);
	}

	public static void registerPlayer(Player pl) {
		activePlayer = pl;
	}

	public static Color getRandomColor(Dice d) {
		return new Color(d.nextInt(0, 255), d.nextInt(0, 255),
				d.nextInt(0, 255));
	}

	public static Color getRandomColor() {
		return getRandomColor(Dice.global);
	}
}
