package jade.path;

import jade.core.World;
import jade.util.datatype.Coordinate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * A {@code PathFinder} which uses the A* path finding algorithm to quickly calculate optimal paths.
 * The implementation uses Cartesian distance as both traversal cost and heuristic estimates. If a
 * path exists it is guaranteed to be found, and an optimal path will always be returned. If no path
 * exists, a partial path from the accessible location closest to the goal will be returned.
 */
public class AStar extends PathFinder
{
    @Override
    protected List<Coordinate> calcPath(World world, Coordinate start, Coordinate end)
    {
        NodeSet nodes = new NodeSet(world, end);
        nodes.get(start).gCost = 0;

        Set<Node> closed = new HashSet<Node>();
        NavigableSet<Node> open = new TreeSet<Node>();
        open.add(nodes.get(start));

        while(!open.isEmpty())
        {
            Node node = open.pollFirst();
            closed.add(node);

            if(node.equals(end))
                return reconstructPath(node);

            for(Node neighbor : expandNode(node, world, nodes))
            {
                if(closed.contains(neighbor))
                    continue;

                double gCost = node.gCost + gCost(node, neighbor);
                if(gCost < neighbor.gCost)
                {
                    open.remove(neighbor);
                    neighbor.updateGCost(gCost);
                    neighbor.prev = node;
                    open.add(neighbor);
                }
            }
        }

        return partialPath(closed);
    }

    private static double gCost(Coordinate start, Coordinate end)
    {
        return start.distance(end);
    }

    private static double hEstimate(Coordinate start, Coordinate end)
    {
        return start.distance(end);
    }

    private List<Coordinate> reconstructPath(Node node)
    {
        LinkedList<Coordinate> path = new LinkedList<Coordinate>();
        while(node.prev != null)
        {
            path.addFirst(node.copy());
            node = node.prev;
        }
        return path;
    }

    private List<Coordinate> partialPath(Set<Node> closed)
    {
        Node best = null;
        for(Node node : closed)
        {
            if(best == null || node.hScore < best.hScore)
                best = node;
        }
        return reconstructPath(best);
    }

    private Iterable<Node> expandNode(Node next, World world, NodeSet nodes)
    {
        List<Node> neighbors = new ArrayList<Node>();
        for(int x = next.x() - 1; x <= next.x() + 1; x++)
            for(int y = next.y() - 1; y <= next.y() + 1; y++)
                if(world.passableAt(x, y))
                    neighbors.add(nodes.get(x, y));
        return neighbors;
    }

    private class NodeSet
    {
        private Node[][] nodes;
        private Coordinate end;

        public NodeSet(World world, Coordinate end)
        {
            nodes = new Node[world.width()][world.height()];
            this.end = end;
        }

        public Node get(Coordinate pos)
        {
            return get(pos.x(), pos.y());
        }

        public Node get(int x, int y)
        {
            if(nodes[x][y] == null)
                nodes[x][y] = new Node(x, y, end);
            return nodes[x][y];
        }
    }

    private class Node extends Coordinate implements Comparable<Node>
    {
        public double gCost;
        public double hScore;
        public double fScore;
        public Node prev;

        public Node(int x, int y, Coordinate end)
        {
            super(x, y);
            hScore = hEstimate(this, end);
            updateGCost(Double.MAX_VALUE);
        }

        public void updateGCost(double gCost)
        {
            this.gCost = gCost;
            fScore = gCost + hScore;
        }

        @Override
        public int compareTo(Node other)
        {
            if(fScore < other.fScore)
                return -1;
            else if(fScore > other.fScore)
                return 1;
            else if(x() != other.x())
                return x() - other.x();
            else
                return y() - other.y();
        }
    }
}
