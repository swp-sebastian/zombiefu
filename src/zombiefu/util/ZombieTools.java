package zombiefu.util;

import jade.util.Dice;
import jade.util.datatype.Direction;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import zombiefu.util.ZombieGame;

public class ZombieTools {

    public static List<Direction> getAllowedDirections() {
        return Arrays.asList(Direction.SOUTH, Direction.EAST, Direction.WEST,
                Direction.NORTH);
    }

    public static Direction getReversedDirection(Direction dir) {
        switch (dir) {
            case NORTH:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.NORTH;
            case EAST:
                return Direction.WEST;
            case WEST:
                return Direction.EAST;
            case ORIGIN:
                return Direction.ORIGIN;
        }
        return null;
    }

    public static Direction getRandomDirection() {
        return Dice.global.choose(getAllowedDirections());
    }

    public static Color getRandomColor(Dice d) {
        return new Color(d.nextInt(0, 255), d.nextInt(0, 255),
                d.nextInt(0, 255));
    }

    public static Color getRandomColor() {
        return getRandomColor(Dice.global);
    }

    public static Color getColorFromString(String s) {
        return Color.decode("0x" + s);
    }

    public static char getCharFromString(String s) {
        if (s.length() == 1) {
            return s.charAt(0);
        } else {
            return (char) Integer.parseInt(s, 16);
        }
    }

    // getAction :: char -> Action
    // Konvertiert key press in konfigurierte Action
    public static Action keyToAction(HashMap<String, Action> keybindings, char c) {
        return keybindings.get(Character.getName(c));
    }

    public static void log(String s) {
        if (ZombieGame.getSettings().debug) {
            System.out.println(s);
        }
    }

    public static void logError(String s) {
        System.out.println(s);
    }

}
