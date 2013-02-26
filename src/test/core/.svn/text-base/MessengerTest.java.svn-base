package test.core;

import jade.core.Messenger;
import jade.util.Lambda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessengerTest
{
    private Messenger messenger;
    private List<String> messages;

    private Messenger other1;
    private Messenger other2;
    private Set<String> otherMessages1;
    private Set<String> otherMessages2;

    @Before
    public void init()
    {
        messenger = instantiateMessenger();
        messages = new ArrayList<String>();
        messages.add("Hello");
        messages.add("World");
        messages.add("Goodbye");
        messages.add("Moon");
        for(String message : messages)
            messenger.appendMessage(message);
    }

    @Test
    public void appendRetrieveMessages()
    {
        List<String> actual = Lambda.toList(messenger.retrieveMessages());
        Assert.assertEquals(messages, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void appendNullMessage()
    {
        messenger.appendMessage(null);
    }

    @Test
    public void clearRetrieveNone()
    {
        messenger.clearMessages();
        List<String> actual = Lambda.toList(messenger.retrieveMessages());
        Assert.assertEquals(0, actual.size());
    }

    @Test
    public void retrieveClears()
    {
        messenger.retrieveMessages();
        List<String> actual = Lambda.toList(messenger.retrieveMessages());
        Assert.assertEquals(0, actual.size());
    }

    @Test
    public void aggregateAppends()
    {
        setupOther1();
        messages.addAll(otherMessages1);
        messenger.aggregateMessages(other1);

        List<String> actual = Lambda.toList(messenger.retrieveMessages());
        Assert.assertEquals(messages.size(), actual.size());
        for(String message : actual)
            Assert.assertTrue(messages.contains(message));
    }

    @Test
    public void aggregateClears()
    {
        Messenger other = instantiateMessenger();
        other.aggregateMessages(messenger);
        List<String> actual = Lambda.toList(messenger.retrieveMessages());
        Assert.assertEquals(0, actual.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void aggregateNull()
    {
        messenger.aggregateMessages(null);
    }

    @Test
    public void filterKeepAll()
    {
        setupOther1();
        setupOther2();

        messenger.aggregateMessages(other1);
        messenger.aggregateMessages(other2);
        messages.addAll(otherMessages1);
        messages.addAll(otherMessages2);
        Collection<Messenger> filter = new HashSet<Messenger>();
        filter.add(messenger);
        filter.add(other1);
        filter.add(other2);
        messenger.filterMessages(filter);

        List<String> actual = Lambda.toList(messenger.retrieveMessages());
        Assert.assertEquals(messages.size(), actual.size());
        for(String message : actual)
            Assert.assertTrue(messages.contains(message));
    }

    @Test
    public void filterKeepOne()
    {
        setupOther1();
        setupOther2();

        messenger.aggregateMessages(other1);
        messenger.aggregateMessages(other2);
        messages.clear();
        messages.addAll(otherMessages1);
        Collection<Messenger> filter = new HashSet<Messenger>();
        filter.add(other1);
        messenger.filterMessages(filter);

        List<String> actual = Lambda.toList(messenger.retrieveMessages());
        Assert.assertEquals(messages.size(), actual.size());
        for(String message : actual)
            Assert.assertTrue(messages.contains(message));
    }

    @Test
    public void filterKeepNone()
    {
        setupOther1();
        setupOther2();

        messenger.aggregateMessages(other1);
        messenger.aggregateMessages(other2);
        Collection<Messenger> filter = new HashSet<Messenger>();
        messenger.filterMessages(filter);

        List<String> actual = Lambda.toList(messenger.retrieveMessages());
        Assert.assertEquals(0, actual.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void filterNull()
    {
        messenger.filterMessages(null);
    }

    private void setupOther1()
    {
        otherMessages1 = new HashSet<String>();
        otherMessages1.add("Foo");
        otherMessages1.add("Bar");
        otherMessages1.add("Baz");
        other1 = instantiateMessenger();
        for(String message : otherMessages1)
            other1.appendMessage(message);
    }

    private void setupOther2()
    {
        otherMessages2 = new HashSet<String>();
        otherMessages2.add("asdf");
        otherMessages2.add("poi");
        otherMessages2.add("qwerty");
        otherMessages2.add("jkl;");
        other2 = instantiateMessenger();
        for(String message : otherMessages2)
            other2.appendMessage(message);
    }

    private Messenger instantiateMessenger()
    {
        return new Messenger()
        {};
    }
}
