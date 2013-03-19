package test.util.datatypes;

import jade.util.datatype.ColoredChar;
import java.awt.Color;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class ColoredCharTest
{
    private char[] chars;
    private Color[] colors;

    @Before
    public void initValues()
    {
        chars = new char[]{'@', 'o', 'D', 'O', 't', 'h'};
        colors = new Color[]{Color.white, Color.red, Color.blue};
    }

    @Test
    public void constructorSetsValue()
    {
        for(char ch : chars)
            for(Color color : colors)
            {
                ColoredChar coloredChar = ColoredChar.create(ch, color);
                Assert.assertEquals(ch, coloredChar.ch());
                Assert.assertEquals(color, coloredChar.color());
            }
    }

    @Test
    public void constructorDefaultColor()
    {
        for(char ch : chars)
        {
            ColoredChar coloredChar = ColoredChar.create(ch);
            Assert.assertEquals(ch, coloredChar.ch());
            Assert.assertEquals(Color.white, coloredChar.color());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullColor()
    {
        ColoredChar ch = ColoredChar.create('@', null);
        Assert.assertNull(ch);
    }

    @Test
    public void equals()
    {
        for(char ch : chars)
            for(Color color : colors)
            {
                ColoredChar ch1 = ColoredChar.create(ch, color);
                ColoredChar ch2 = ColoredChar.create(ch, color);
                Assert.assertEquals(ch1, ch2);
            }
    }

    @Test
    public void notEquals()
    {
        ColoredChar ch1 = ColoredChar.create('@', Color.white);
        ColoredChar ch2 = ColoredChar.create('@', Color.red);
        ColoredChar ch3 = ColoredChar.create('D', Color.white);
        ColoredChar ch4 = ColoredChar.create('D', Color.red);

        Assert.assertFalse(ch1.equals(ch2));
        Assert.assertFalse(ch1.equals(ch2));
        Assert.assertFalse(ch1.equals(ch3));

        Assert.assertFalse(ch2.equals(ch3));
        Assert.assertFalse(ch2.equals(ch4));

        Assert.assertFalse(ch3.equals(ch4));
    }

    @Test
    public void notEqualOtherClass()
    {
        Assert.assertFalse(ColoredChar.create('.').equals('.'));
    }

    @Test
    public void hashEqual()
    {
        for(char ch : chars)
            for(Color color : colors)
            {
                ColoredChar ch1 = ColoredChar.create(ch, color);
                ColoredChar ch2 = ColoredChar.create(ch, color);
                Assert.assertEquals(ch1.hashCode(), ch2.hashCode());
            }
    }

    @Test
    public void toStringGetsChValue()
    {
        Assert.assertEquals("@", ColoredChar.create('@').toString());
        Assert.assertEquals("@", ColoredChar.create('@', Color.red).toString());
        Assert.assertEquals("D", ColoredChar.create('D').toString());
        Assert.assertEquals("D", ColoredChar.create('D', Color.red).toString());
    }

    @Test
    public void createReturnSame()
    {
        for(char ch : chars)
            for(Color color : colors)
                Assert.assertSame(ColoredChar.create(ch, color), ColoredChar.create(ch, color));
    }
}
