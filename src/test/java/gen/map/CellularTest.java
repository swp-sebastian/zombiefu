package test.gen.map;

import jade.gen.map.Cellular;
import jade.gen.map.MapGenerator;

public class CellularTest extends MapGeneratorTest
{
    @Override
    protected MapGenerator getInstance()
    {
        return new Cellular();
    }
}
