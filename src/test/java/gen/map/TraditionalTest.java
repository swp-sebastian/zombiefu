package test.gen.map;

import jade.gen.map.MapGenerator;
import jade.gen.map.Traditional;

public class TraditionalTest extends MapGeneratorTest
{
    @Override
    protected MapGenerator getInstance()
    {
        return new Traditional();
    }
}
