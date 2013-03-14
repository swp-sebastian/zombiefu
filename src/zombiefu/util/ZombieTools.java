package zombiefu.util;

import jade.util.Dice;
import jade.util.datatype.Direction;
import static jade.util.datatype.Direction.EAST;
import static jade.util.datatype.Direction.NORTH;
import static jade.util.datatype.Direction.ORIGIN;
import static jade.util.datatype.Direction.SOUTH;
import static jade.util.datatype.Direction.WEST;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

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

    // getAction :: char -> Action
    // Konvertiert key press in konfigurierte Action
    public static Action keyToAction(HashMap<String, Action> keybindings, char c) {
        return keybindings.get(Character.getName(c));
    }

    public static void log(String s) {
        System.out.println(s);
    }

    public static void logError(String s) {
        System.out.println(s);
    }

    public static void stopWithFatalError(String s) {
        logError(s);
        System.exit(1);
    }
}
