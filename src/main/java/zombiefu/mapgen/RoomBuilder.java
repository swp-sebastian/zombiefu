package zombiefu.mapgen;

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
    private ColoredChar floorTile;

    public RoomBuilder(ColoredChar[][] screen, ColoredChar floorTile) {
        this.screen = screen;
        this.height = screen.length;
        this.width = screen[0].length;
        this.floorTile = floorTile;
    }


    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    @Override
    protected void generateStep(World world, Dice dice) {
        HashMap<Character, Boolean> passSet = ConfigHelper.getPassSet();
        HashMap<Character, Boolean> visibleSet = ConfigHelper.getVisibleSet();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                boolean passable, visible;
                ColoredChar tile;
                tile = screen[x][y];
                if (tile == null) {
                    tile = floorTile;
                    passable = visible = true;
                } else if (tile.ch() == floorTile.ch()) {
                    passable = visible = true;
                } else if (passSet.containsKey(tile.ch()) && visibleSet.containsKey(tile.ch())) {
                    passable = passSet.get(tile.ch());
                    visible = visibleSet.get(tile.ch());
                } else {
                    passable = visible = false;
                }
                world.setTile(tile, passable, visible, y, x);
            }
        }
    }
}
