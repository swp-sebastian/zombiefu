package zombiefu.map;

import java.awt.Color;
import java.util.HashMap;
import zombiefu.util.ConfigHelper;
import jade.core.World;
import jade.gen.map.MapGenerator;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;

public class RoomBuilder extends MapGenerator {

    private ColoredChar[][] screen; // Das Gelände
    private int width;
    private int height;

    public RoomBuilder(ColoredChar[][] screen) {
        this.screen = screen;
        this.height = screen.length;
        this.width = screen[0].length;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    @Override
    protected void generateStep(World world, Dice dice) {
        HashMap<Character, Color> charSet = ConfigHelper.getCharSet();
        HashMap<Character, Boolean> passSet = ConfigHelper.getPassSet();
        HashMap<Character, Boolean> visibleSet = ConfigHelper.getVisibleSet();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                try {
                    if (charSet.containsKey(screen[x][y].ch())) {
                        world.setTile(
                                ColoredChar.create(screen[x][y].ch(),
                                charSet.get(screen[x][y].ch())),
                                passSet.get(screen[x][y].ch()), visibleSet.get(screen[x][y].ch()), y, x);
                    } else {
                        world.setTile(screen[x][y], y, x);
                    }
                } catch (NullPointerException e) {
                    world.setTile(ColoredChar.create(' '), y, x);
                }

            }
        }
    }
}
