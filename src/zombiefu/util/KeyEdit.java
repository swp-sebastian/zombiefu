package zombiefu.util;

public class KeyEdit {
	private static String name;
	public static char up;
	public static char down;
	public static char left;
	public static char right;
	public static char hit;
	public static char nextItem;
	public static char prevItem;
	public static char showItems;
	
	public static String getPlayername(){
		if (name.equalsIgnoreCase("playername")){ //Wenn die Konfigurationsdatei nicht geändert wurde,
			name = System.getProperty("user.name"); // wird der Benutzername als Spielername gesetzt
		}
		return name;
	}
	
	public static void read(String[] config){
		for (int i =0;i<config.length;i++){
			String[] splitResult = config[i].split("=");
			config[i]= splitResult[1];
		}
		name = config[0];
		up = config[1].charAt(0);
		down = config[2].charAt(0);
		left = config[3].charAt(0);
		right = config[4].charAt(0);
		hit = config[5].charAt(0);
		nextItem = config[6].charAt(0);
		prevItem = config[7].charAt(0);
		showItems = config[8].charAt(0);
	}
}
