package test.ui;

import jade.ui.Terminal;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.awt.Color;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TerminalTest
{
    private Terminal term;

    @Before
    public void initConcreteTerm()
    {
        term = new Terminal()
        {
            @Override
            public char getKey()
            {
                return 0;
            }

            @Override
            public void refreshScreen()
            {}
        };
        term = Mockito.spy(term);
    }

    @Test
    public void bufferGetChar()
    {
        ColoredChar[] tiles = {ColoredChar.create('#'), ColoredChar.create('.'),
                ColoredChar.create('@')};
        for(ColoredChar tile : tiles)
        {
            for(int x = 0; x < 10; x++)
                for(int y = 0; y < 10; y++)
                    term.bufferChar(new Coordinate(x, y), tile);
            for(int x = 0; x < 10; x++)
                for(int y = 0; y < 10; y++)
                    Assert.assertEquals(tile, term.charAt(new Coordinate(x, y)));
        }
    }

    @Test
    public void bufferGetCharInt()
    {
        ColoredChar[] tiles = {ColoredChar.create('#'), ColoredChar.create('.'),
                ColoredChar.create('@')};
        for(ColoredChar tile : tiles)
        {
            for(int x = 0; x < 10; x++)
                for(int y = 0; y < 10; y++)
                    term.bufferChar(new Coordinate(x, y), tile);
            for(int x = 0; x < 10; x++)
                for(int y = 0; y < 10; y++)
                    Assert.assertEquals(tile, term.charAt(x, y));
        }
    }

    @Test
    public void charAtIntCallsCoord()
    {
        int x = 3;
        int y = 5;
        Coordinate pos = new Coordinate(x, y);
        ColoredChar expected = ColoredChar.create('@');

        term = Mockito.mock(Terminal.class);
        Mockito.doReturn(expected).when(term).charAt(pos);
        Assert.assertSame(expected, term.charAt(x, y));
        Mockito.verify(term).charAt(pos);
    }

    @Test
    public void getUnbufferedChar()
    {
        for(int x = 0; x < 10; x++)
            for(int y = 0; y < 10; y++)
                Assert.assertNull(term.charAt(new Coordinate(x, y)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferNullCoord()
    {
        term.bufferChar(null, ColoredChar.create('@'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferNullTile()
    {
        term.bufferChar(new Coordinate(4, 5), null);
    }

    @Test
    public void bufferCharInt()
    {
        int x = 4;
        int y = 6;
        ColoredChar ch = ColoredChar.create('@');

        term = Mockito.mock(Terminal.class);
        term.bufferChar(x, y, ch);
        Mockito.verify(term).bufferChar(new Coordinate(x, y), ch);
    }

    @Test
    public void clearBufferNulls()
    {
        for(int x = 0; x < 10; x++)
            for(int y = 0; y < 10; y++)
                term.bufferChar(new Coordinate(x, y), ColoredChar.create('#'));
        term.clearBuffer();
        for(int x = 0; x < 10; x++)
            for(int y = 0; y < 10; y++)
                Assert.assertNull(term.charAt(new Coordinate(x, y)));
    }

    @Test
    public void saveRecallBuffer()
    {
        ColoredChar saved = ColoredChar.create('@');
        ColoredChar overwrite = ColoredChar.create('#');

        for(int x = 0; x < 10; x++)
            for(int y = 0; y < 10; y++)
                term.bufferChar(new Coordinate(x, y), saved);
        term.saveBuffer();
        for(int x = 0; x < 10; x++)
            for(int y = 0; y < 10; y++)
                term.bufferChar(new Coordinate(x, y), overwrite);
        for(int x = 0; x < 10; x++)
            for(int y = 0; y < 10; y++)
                Assert.assertEquals(overwrite, term.charAt(new Coordinate(x, y)));
        term.recallBuffer();
        for(int x = 0; x < 10; x++)
            for(int y = 0; y < 10; y++)
                Assert.assertEquals(saved, term.charAt(new Coordinate(x, y)));
    }

    @Test
    public void saveRecallOverwritesOld()
    {
        ColoredChar tile = ColoredChar.create('@');
        Coordinate coord0 = new Coordinate(0, 0);
        Coordinate coord1 = new Coordinate(1, 1);
        Coordinate coord2 = new Coordinate(2, 2);

        term.bufferChar(coord0, tile);
        term.saveBuffer();

        term.clearBuffer();
        term.bufferChar(coord1, tile);
        term.saveBuffer();

        term.clearBuffer();
        term.bufferChar(coord2, tile);
        term.recallBuffer();

        Assert.assertNull(term.charAt(coord0));
        Assert.assertEquals(tile, term.charAt(coord1));
        Assert.assertNull(term.charAt(coord2));
    }

    @Test
    public void bufferStringIntColor()
    {
        int x = 10;
        int y = 3;
        String str = "Hello World!";
        Color color = Color.red;
        term.bufferString(x, y, str, color);
        for(int i = 0; i < str.length(); i++)
        {
            Coordinate pos = new Coordinate(x + i, y);
            ColoredChar ch = ColoredChar.create(str.charAt(i), color);
            Mockito.verify(term).bufferChar(pos, ch);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferNullStringIntColor()
    {
        term.bufferString(6, 3, null, Color.red);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferStringIntNullColor()
    {
        term.bufferString(6, 3, "asdf", null);
    }

    @Test
    public void bufferStringCoordColor()
    {
        int x = 6;
        int y = 3;
        String str = "This is Jade!";
        Color color = Color.red;
        term.bufferString(new Coordinate(x, y), str, color);
        Mockito.verify(term).bufferString(x, y, str, color);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferStringNullCoordColor()
    {
        term.bufferString(null, "asdf", Color.red);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferNullStringCoordColor()
    {
        term.bufferString(new Coordinate(5, 6), null, Color.red);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferStringCoordNullColor()
    {
        term.bufferString(new Coordinate(5, 6), "qwerty", null);
    }

    @Test
    public void bufferStringInt()
    {
        int x = 10;
        int y = 7;
        String str = "Dragon flight";
        term.bufferString(x, y, str);
        Mockito.verify(term).bufferString(x, y, str, Color.white);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferNullStringInt()
    {
        term.bufferString(3, 6, null);
    }

    @Test
    public void bufferStringCoord()
    {
        int x = 3;
        int y = 1;
        String str = "YASD";
        term.bufferString(new Coordinate(x, y), str);
        Mockito.verify(term).bufferString(x, y, str, Color.white);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferNullStringCoord()
    {
        term.bufferString(new Coordinate(3, 4), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferStringCoordNull()
    {
        term.bufferString(null, "YAVP");
    }
}
