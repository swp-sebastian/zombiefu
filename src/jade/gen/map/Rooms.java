package jade.gen.map;

import jade.core.World;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Rooms extends MapGenerator {

    private ColoredChar wallTile;
    private ColoredChar floorTile;

    public Rooms() {
        this(ColoredChar.create('#',Color.darkGray,false), ColoredChar.create('.',Color.CYAN,true));
    }

    public Rooms(ColoredChar wallTile, ColoredChar floorTile) {
        this.floorTile = floorTile;
        this.wallTile = wallTile;
    }

    @Override
    protected void generateStep(World world, Dice dice) {
        Set<Coordinate> cells = init(world);
        int wandObenY = (world.height() - 1) * 2 / 5;
        int wandUntenY = (world.height() - 1) * 3 / 5;

        for (int x = 0; x < world.width(); x++) {
            world.setTile(wallTile, x, wandObenY);
            world.setTile(wallTile, x, wandUntenY);
        }

        int wandObenTrenner = dice.nextInt(2, world.width() - 3);
        int wandUntenTrenner = dice.nextInt(2, world.width() - 3);

        for (int y = 0; y < wandObenY; y++) {
            world.setTile(wallTile, wandObenTrenner, y);
        }

        for (int y = wandUntenY; y < world.height() - 1; y++) {
            world.setTile(wallTile, wandUntenTrenner, y);
        }

        world.setTile(floorTile, dice.nextInt(1, wandObenTrenner - 1), wandObenY);
        world.setTile(floorTile, dice.nextInt(wandObenTrenner + 1, world.width() - 2), wandObenY);
        world.setTile(floorTile, dice.nextInt(1, wandUntenTrenner - 1), wandUntenY);
        world.setTile(floorTile, dice.nextInt(wandUntenTrenner + 1, world.width() - 2), wandUntenY);
    }

    private HashSet<Coordinate> init(World world) {
        HashSet<Coordinate> cells = new HashSet<Coordinate>();
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                if (x == 0 || y == 0 || x == world.width() - 1 || y == world.height() - 1) {
                    world.setTile(wallTile, false, x, y);
                } else {
                    world.setTile(floorTile, true, x, y);
                }
            }
        }
        return cells;
    }
}
