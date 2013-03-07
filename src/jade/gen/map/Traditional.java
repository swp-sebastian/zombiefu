package jade.gen.map;

import jade.core.World;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.MutableCoordinate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Uses a binary space partitioning algorithm to generate rooms, and the connect them using the
 * binary space partition tree. Cycles are then added to make the maps more interesting. This
 * algorithm yields traditional roguelike maps with rectangular rooms connected by corridors.
 */
public class Traditional extends MapGenerator
{
    private ColoredChar wallTile;
    private ColoredChar floorTile;
    private int minSize;

    /**
     * Instantiates a BSP with default parameters. Room minSize is 4. Wall and floor tiles are '#'
     * and '.' respectively.
     */
    public Traditional()
    {
        this(ColoredChar.create('#'), ColoredChar.create('.'), 4);
    }

    /**
     * Instantiates a custom BSP with provided parameters.
     * @param wallTile the tile used for impassible walls
     * @param floorTile the tile used for passable floors
     * @param minSize the minimum dimension of a room
     */
    public Traditional(ColoredChar wallTile, ColoredChar floorTile, int minSize)
    {
        this.floorTile = floorTile;
        this.wallTile = wallTile;
        this.minSize = minSize;
    }

    @Override
    protected void generateStep(World world, Dice dice)
    {
        wallFill(world);
        BSPNode head = new BSPNode(world, minSize);
        head.divide(dice);
        head.makeRooms(world, dice);
        head.connect(world, dice);
        head.addCycle(world, dice);// a plain tree is boring
    }

    private void wallFill(World world)
    {
        for(int x = 0; x < world.width(); x++)
            for(int y = 0; y < world.height(); y++)
                world.setTile(wallTile, false, x, y);
    }

    private class BSPNode
    {
        private int x1;
        private int y1;
        private int x2;
        private int y2;
        private int rx1;
        private int ry1;
        private int rx2;
        private int ry2;
        private BSPNode left;
        private BSPNode right;
        private BSPNode parent;
        private boolean connected;
        private boolean readyConnect;

        public BSPNode(World world, int minSize)
        {
            connected = true;// head node has no sibling, so can't connect
            readyConnect = true;
            x1 = 0;
            y1 = 0;
            x2 = world.width() - 1;
            y2 = world.height() - 1;
        }

        private BSPNode(BSPNode parent, int div, boolean vert, boolean left)
        {
            this.parent = parent;
            connected = false;
            readyConnect = true;
            // vert means we divide on x
            x1 = parent.x1 + (vert && !left ? div + 1 : 0);
            x2 = vert && left ? parent.x1 + div : parent.x2;
            // non vert means we divide on y
            y1 = parent.y1 + (!vert && !left ? div + 1 : 0);
            y2 = !vert && left ? parent.y1 + div : parent.y2;
        }

        public void divide(Dice dice)
        {
            boolean vert = dice.chance();
            int min = minSize + 4;// +4 so we don't get aligned grid of rooms
            if(divTooSmall(vert, min))
                vert = !vert;
            if(divTooSmall(vert, min))
                return;
            int div = dice.nextInt(min, (vert ? x2 - x1 : y2 - y1) - min);
            left = new BSPNode(this, div, vert, true);
            right = new BSPNode(this, div, vert, false);
            readyConnect = false;
            left.divide(dice);
            right.divide(dice);
        }

        public void makeRooms(World world, Dice dice)
        {
            if(leaf())
            {
                rx1 = dice.nextInt(x1 + 1, x2 - 1 - minSize);
                rx2 = dice.nextInt(rx1 + minSize, x2 - 1);
                ry1 = dice.nextInt(y1 + 1, y2 - 1 - minSize);
                ry2 = dice.nextInt(ry1 + minSize, y2 - 1);
                for(int x = rx1; x <= rx2; x++)
                    for(int y = ry1; y <= ry2; y++)
                        world.setTile(floorTile, true, x, y);
            }
            else
            {
                left.makeRooms(world, dice);
                right.makeRooms(world, dice);
            }
        }

        public void connect(World world, Dice dice)
        {
            if(!leaf())
            {
                left.connect(world, dice);
                right.connect(world, dice);
            }
            // only connect when children are connected, since me being
            // connected only says something in my bounds connects to my
            // siblings bounds
            readyConnect = true;
            if(connected)// my sibling already connected to me
                return;
            BSPNode sibling = sibling();
            if(sibling.readyConnect)
            {
                connectNeighbor(world, dice, sibling);
                sibling.connected = true;
                connected = true;
            }
        }

        public void addCycle(World world, Dice dice)
        {
            List<BSPNode> leaves = getLeaves(new ArrayList<BSPNode>());
            for(BSPNode leaf : leaves)
                if(dice.chance(30))
                {
                    BSPNode neighbor = dice.choose(leaves);
                    leaf.connectNeighbor(world, dice, neighbor);
                }
        }

        private List<BSPNode> getLeaves(List<BSPNode> leaves)
        {
            if(leaf())
                leaves.add(this);
            else
            {
                left.getLeaves(leaves);
                right.getLeaves(leaves);
            }
            return leaves;
        }

        private void connectNeighbor(World world, Dice dice, BSPNode neighbor)
        {
            Coordinate start = world.getOpenTile(dice, x1, y1, x2, y2);
            Coordinate end = world.getOpenTile(dice, neighbor.x1, neighbor.y1, neighbor.x2,
                    neighbor.y2);
            List<Coordinate> corridor = new LinkedList<Coordinate>();
            MutableCoordinate curr = start.mutableCopy();
            while(!curr.equals(end))
            {
                corridor.add(curr.copy());
                boolean digging = !world.passableAt(curr);
                // dig horizontal, then vertical
                if(curr.x() == end.x())
                    curr.translate(0, curr.y() < end.y() ? 1 : -1);
                else
                    curr.translate(curr.x() < end.x() ? 1 : -1, 0);
                if(digging && world.passableAt(curr))
                {
                    // don't actually dig the corridor if we only connected
                    // to ourselves, just restart the corridor
                    if(neighbor.inside(curr))
                        break;
                    corridor.clear();
                }
            }
            // finally we dig the connecting corridor
            for(Coordinate coord : corridor)
                world.setTile(floorTile, true, coord);
        }

        private BSPNode sibling()
        {
            return this == parent.left ? parent.right : parent.left;
        }

        private boolean divTooSmall(boolean vert, int min)
        {
            min *= 2;// need space for two rooms
            return vert ? (x2 - x1) < min : (y2 - y1) < min;
        }

        private boolean leaf()
        {
            return left == null && right == null;
        }

        private boolean inside(Coordinate pos)
        {
            int x = pos.x();
            int y = pos.y();
            return x < x2 && x > x1 && y < y2 && y > y1;
        }
    }
}
