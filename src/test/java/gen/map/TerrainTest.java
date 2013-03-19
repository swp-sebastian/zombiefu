package test.gen.map;

import jade.gen.map.MapGenerator;
import jade.gen.map.Terrain;

public class TerrainTest extends MapGeneratorTest
{
    @Override
    protected MapGenerator getInstance()
    {
        return new Terrain();
    }
}
