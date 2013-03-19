package test.gen.map;

import jade.gen.map.MapGenerator;
import jade.gen.map.Maze;

public class MazeTest extends MapGeneratorTest
{
    @Override
    protected MapGenerator getInstance()
    {
        return new Maze();
    }
}
