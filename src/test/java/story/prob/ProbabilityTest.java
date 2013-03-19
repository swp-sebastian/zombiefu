package test.story.prob;

public class ProbabilityTest
{
    protected int[] uniform()
    {
        int[] expected = new int[10];
        for(int i = 0; i < expected.length; i++)
            expected[i] = 1;
        return expected;
    }

    protected int[] stair()
    {
        int[] expected = new int[10];
        for(int i = 0; i < expected.length; i++)
            expected[i] = i;
        return expected;
    }
    
    protected int[] power()
    {
        int[] expected = new int[10];
        expected[0] = 1;
        for(int i = 1; i < expected.length; i++)
            expected[i] = expected[i - 1] * 2;
        return expected;
    }
}
