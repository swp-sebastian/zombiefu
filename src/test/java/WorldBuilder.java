package test;

import jade.core.World;
import jade.util.datatype.ColoredChar;

public class WorldBuilder
{
    private static World getTestWorld(String[] map)
    {
        return new TestWorld(map);
    }

    private static class TestWorld extends World
    {
        public TestWorld(String[] map)
        {
            super(map[0].length(), map.length);
            for(int y = 0; y < height(); y++)
            {
                for(int x = 0; x < width(); x++)
                {
                    char face = map[y].charAt(x);
                    setTile(ColoredChar.create(face), face != '#', x, y);
                }
            }
        }

        public TestWorld(int width, int height)
        {
            super(width, height);
        }
    }

    public static World getEmptyMap()
    {
        return getTestWorld(EmptyMap);
    }

    private static final String[] EmptyMap = {
    // ......0123456789012
            "#############",// 0
            "#...........#",// 1
            "#...........#",// 2
            "#...........#",// 3
            "#...........#",// 4
            "#...........#",// 5
            "#...........#",// 6
            "#...........#",// 7
            "#...........#",// 8
            "#...........#",// 9
            "#...........#",// 0
            "#...........#",// 1
            "#############" // 2
    };

    public static World getWallNS()
    {
        return getTestWorld(WallNS);
    }

    private static final String[] WallNS = {
    // ......0123456789012
            "#############",// 0
            "#.....#.....#",// 1
            "#.....#.....#",// 2
            "#.....#.....#",// 3
            "#.....#.....#",// 4
            "#.....#.....#",// 5
            "#.....#.....#",// 6
            "#...........#",// 7
            "#...........#",// 8
            "#...........#",// 9
            "#...........#",// 0
            "#...........#",// 1
            "#############" // 2
    };

    public static World getWallWE()
    {
        return getTestWorld(WallWE);
    }

    private static final String[] WallWE = {
    // ......0123456789012
            "#############",// 0
            "#...........#",// 1
            "#...........#",// 2
            "#...........#",// 3
            "#...........#",// 4
            "#...........#",// 5
            "#######.....#",// 6
            "#...........#",// 7
            "#...........#",// 8
            "#...........#",// 9
            "#...........#",// 0
            "#...........#",// 1
            "#############" // 2
    };

    public static World getWallSN()
    {
        return getTestWorld(WallSN);
    }

    private static final String[] WallSN = {
    // ......123456789012
            "#############",// 0
            "#...........#",// 1
            "#...........#",// 2
            "#...........#",// 3
            "#...........#",// 4
            "#...........#",// 5
            "#.....#.....#",// 6
            "#.....#.....#",// 7
            "#.....#.....#",// 8
            "#.....#.....#",// 9
            "#.....#.....#",// 0
            "#.....#.....#",// 1
            "#############" // 2
    };

    public static World getWallEW()
    {
        return getTestWorld(WallEW);
    }

    private static final String[] WallEW = {
    // ......0123456789012
            "#############",// 0
            "#...........#",// 1
            "#...........#",// 2
            "#...........#",// 3
            "#...........#",// 4
            "#...........#",// 5
            "#.....#######",// 6
            "#...........#",// 7
            "#...........#",// 8
            "#...........#",// 9
            "#...........#",// 0
            "#...........#",// 1
            "#############" // 2
    };

    public static World getSinglePillar()
    {
        return getTestWorld(SinglePillar);
    }

    private static final String[] SinglePillar = {
    // ......0123456789012
            "#############",// 0
            "#...........#",// 1
            "#...........#",// 2
            "#...........#",// 3
            "#...........#",// 4
            "#...........#",// 5
            "#.....#.....#",// 6
            "#...........#",// 7
            "#...........#",// 8
            "#...........#",// 9
            "#...........#",// 0
            "#...........#",// 1
            "#############" // 2
    };

    public static World getMaze()
    {
        return getTestWorld(Maze);
    }

    public static World getSlitEW()
    {
        return getTestWorld(SlitEW);
    }

    private static final String[] SlitEW = {
    // ......0123456789012
            "#############",// 0
            "#...........#",// 1
            "#...........#",// 2
            "#...........#",// 3
            "#...........#",// 4
            "#...........#",// 5
            "######.######",// 6
            "#...........#",// 7
            "#...........#",// 8
            "#...........#",// 9
            "#...........#",// 0
            "#...........#",// 1
            "#############" // 2
    };

    private static String[] Maze = {
    // ......0123456789
            "##########",// 0
            "#*.......#",// 1
            "######.#.#",// 2
            "#......#.#",// 3
            "#.######.#",// 4
            "#.######.#",// 5
            "#.....*#.#",// 6
            "#......#.#",// 7
            "#........#",// 8
            "##########" // 9
    };

    public static World getDeadEnd()
    {
        return getTestWorld(DeadEnd);
    }

    private static String[] DeadEnd = {
    // ......0123456789
            "##########",// 0
            "#..#*#...#",// 1
            "#..#.#...#",// 2
            "#..#.#...#",// 3
            "#..#.....#",// 4
            "#..#..##.#",// 5
            "#..#..##.#",// 6
            "#..####..#",// 7
            "#...*....#",// 8
            "##########" // 9
    };

    public static World getBlocked()
    {
        return getTestWorld(Blocked);
    }

    private static String[] Blocked = {
    // ......0123456789
            "##########",// 0
            "#..#*#.#.#",// 1
            "#..#.#.#.#",// 2
            "#..#.#.#.#",// 3
            "##.....#.#",// 4
            "##.#.###.#",// 5
            "##.###...#",// 6
            "##.#######",// 7
            "####*....#",// 8
            "##########" // 9
    };
}
