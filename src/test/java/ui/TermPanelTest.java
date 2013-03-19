package test.ui;

import jade.ui.TermPanel;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JFrame;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TermPanelTest
{
    private TermPanel term;
    private int cols;
    private int rows;
    private int font;

    @Before
    public void init()
    {
        cols = 80;
        rows = 24;
        font = 12;
        term = new TermPanel(cols, rows, font);
    }

    @After
    public void disposeFrame()
    {
        Component component = term.panel();
        while(component.getParent() != null)
            component = component.getParent();
        if(component instanceof JFrame)
            ((JFrame)component).dispose();
    }

    private void getFramedTerm()
    {
        term = TermPanel.getFramedTerminal("jade.ui.TermPanel - test.ui.TermPanelTest");
    }

    @Test
    public void constructorPreferedSize()
    {
        Dimension preferedSize = term.panel().getPreferredSize();
        Assert.assertEquals(cols * font * 3 / 4, (int)preferedSize.getWidth());
        Assert.assertEquals(rows * font, (int)preferedSize.getHeight());
    }

    @Test
    public void constructorFont()
    {
        Assert.assertEquals(font, term.panel().getFont().getSize());
    }

    @Test
    public void constructorPreferedSizeDefault()
    {
        TermPanel defaultTerm = new TermPanel();
        Dimension preferedSize = defaultTerm.panel().getPreferredSize();
        Assert.assertEquals(cols * font * 3 / 4, (int)preferedSize.getWidth());
        Assert.assertEquals(rows * font, (int)preferedSize.getHeight());
    }

    @Test
    public void constructorFontDefault()
    {
        TermPanel defaultTerm = new TermPanel();
        Assert.assertEquals(font, defaultTerm.panel().getFont().getSize());
    }

    @Test
    public void getKeyInjectedKeyEvent() throws InterruptedException
    {
        getFramedTerm();
        getKeyInjectedKeyEvent(KeyEvent.VK_L, 'l');
        getKeyInjectedKeyEvent(KeyEvent.VK_H, 'h');
        getKeyInjectedKeyEvent(KeyEvent.VK_N, 'n');
        getKeyInjectedKeyEvent(KeyEvent.VK_B, 'b');
        getKeyInjectedKeyEvent(KeyEvent.VK_AT, '@');
        getKeyInjectedKeyEvent(KeyEvent.VK_LEFT_PARENTHESIS, ')');
        getKeyInjectedKeyEvent(KeyEvent.VK_PERIOD, '.');
    }

    @Test
    public void getKeyOnKeyPressOnly() throws InterruptedException
    {
        getFramedTerm();

        KeyEvent typedEvent = new KeyEvent(term.panel(), KeyEvent.KEY_TYPED, System
                .currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a', KeyEvent.KEY_LOCATION_UNKNOWN);
        ((KeyListener)term.panel()).keyTyped(typedEvent);

        KeyEvent releatedEvent = new KeyEvent(term.panel(), KeyEvent.KEY_RELEASED, System
                .currentTimeMillis(), 0, KeyEvent.VK_B, 'b', KeyEvent.KEY_LOCATION_STANDARD);
        ((KeyListener)term.panel()).keyReleased(releatedEvent);

        injectKeyEvent(KeyEvent.VK_C, 'c');

        Assert.assertEquals('c', term.getKey());
    }

    private void getKeyInjectedKeyEvent(int keyCode, char keyChar) throws InterruptedException
    {
        injectKeyEvent(keyCode, keyChar);
        Assert.assertEquals(keyChar, term.getKey());
    }

    @Test
    public void refreshScreenDrawString() throws IOException
    {
        Assert.fail();
    }

    @Test
    public void threadSafeGetKey() throws InterruptedException
    {
        getFramedTerm();
        Thread producer = new InjectKeyThread();
        Thread consumer = new ReadKeyThread();
        consumer.start();
        producer.start();
        consumer.join();
        producer.join();
    }

    @Test
    public void threadsafeBufferRefresh() throws InterruptedException
    {
        getFramedTerm();
        for(int i = 0; i < 2000; i++)
        {
            int x = Dice.global.nextInt(100);
            int y = Dice.global.nextInt(100);
            term.bufferChar(x, y, ColoredChar.create('#'));
            term.refreshScreen();
        }
    }

    private void injectKeyEvent(int keyCode, char keyChar)
    {
        KeyEvent event = new KeyEvent(term.panel(), KeyEvent.KEY_PRESSED, System
                .currentTimeMillis(), 0, keyCode, keyChar, KeyEvent.KEY_LOCATION_STANDARD);
        ((KeyListener)term.panel()).keyPressed(event);
    }

    private abstract class KeyEventThread extends Thread
    {
        protected abstract void keyAction(char keyChar);

        @Override
        public void run()
        {
            char key = 'a';
            for(int i = 0; i < 2000; i++)
            {
                keyAction(key);

                key += 1;
                if(key > 'z')
                    key = 'a';
            }
        }
    }

    private class InjectKeyThread extends KeyEventThread
    {
        @Override
        protected void keyAction(char keyChar)
        {
            injectKeyEvent(KeyEvent.VK_A, keyChar);
        }
    }

    private class ReadKeyThread extends KeyEventThread
    {
        @Override
        protected void keyAction(char keyChar)
        {
            try
            {
                Assert.assertEquals(keyChar, term.getKey());
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
