package zombiefu.util;

import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.Direction;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import zombiefu.creature.Player;
import zombiefu.ui.ZombieFrame;

public class ZombieTools {

    public static Player activePlayer;
    


    

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
            while (d == null || d == Direction.ORIGIN) {
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
            key = frame.mainTerm().getKey();
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
}
