package zombiefu.util;

import jade.util.Dice;
import jade.util.datatype.Direction;
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
        return getRotatedDirection(dir, 180);
    }

    public static Direction getRotatedDirection(Direction dir, int rotation) {
        if (dir == Direction.ORIGIN) {
            return dir;
        }
        Direction[] dirs = new Direction[]{Direction.NORTH, Direction.NORTHEAST, Direction.EAST, Direction.SOUTHEAST, Direction.SOUTH, Direction.SOUTHWEST, Direction.WEST, Direction.NORTHWEST};
        for (int i = 0; i < 8; i++) {
            if (dirs[i] == dir) {
                return dirs[(i + rotation / 45) % 8];
            }
        }
        return null;
    }

    public static Direction getRandomDirection() {
        return Dice.global.choose(getAllowedDirections());
    }

    public static Double getRandomDouble(double a, double b, int v) {
        double x = Math.random();
        if (v > 0) {
            return 1 - Math.pow(1 - x, v);
        }
        if (v < 0) {
            return Math.pow(x, -v);
        }
        return v * (b - a) + a;
    }

    public static Double getRandomDouble(double a, double b) {
        return getRandomDouble(a, b, 0);
    }

    public static int getRandomIndex(int... args) {
        int sum = 0;
        for (int arg : args) {
            sum += arg;
        }
        int random = Dice.global.nextInt(sum);
        int sc = 0;
        for(int i = 0; i < args.length; i++) {
            sc += args[i];
            if(random < sc)
                return i;            
        }
        throw new ArithmeticException();
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
