package jade.core;

import jade.util.Guard;
import jade.util.Lambda;
import jade.util.Lambda.FilterFunc;
import jade.util.Lambda.MapFunc;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The base class of both {@code Actor} and {@code World}. Allows for easy passing and aggregation
 * of {@code String} messages.
 */
public abstract class Messenger
{
    private List<Message> cache;

    /**
     * Creates a new {@code Messenger}
     */
    public Messenger()
    {
        cache = new ArrayList<Message>();
    }

    /**
     * Appends a message, which will have this {@code Messenger} as a source.
     * 
     * @param message the message to append
     */
    public void appendMessage(String message)
    {
        Guard.argumentIsNotNull(message);

        cache.add(new Message(message, this));
    }

    /**
     * Retrieves and clears all messages held by the {@code Messenger}.
     * 
     * @return all messages held by the {@code Messenger}
     */
    public Iterable<String> retrieveMessages()
    {
        Iterable<String> messages = Lambda.map(cache, SelectTextLambda());
        cache.clear();
        return messages;
    }

    /**
     * Moves the messages stored by the given {@code Messenger} into this one. The source of each
     * message is preserved.
     * 
     * @param messenger the messenger who's messages will be retrieved
     */
    public void aggregateMessages(Messenger messenger)
    {
        Guard.argumentIsNotNull(messenger);

        cache.addAll(messenger.cache);
        messenger.clearMessages();
    }

    /**
     * Filters out messages whose source {@code Messenger} is not in the provided filter.
     * 
     * @param filter the group of {@code Messenger} whose messages will be preserved
     */
    public void filterMessages(Collection<Messenger> filter)
    {
        Guard.argumentIsNotNull(filter);

        Iterable<Message> filtered = Lambda.filter(cache, FilterSourceLambda(filter));
        cache.clear();
        for(Message message : filtered)
            cache.add(message);
    }

    /**
     * Removes all messages held by the {@code Messenger}
     */
    public void clearMessages()
    {
        cache.clear();
    }

    private class Message
    {
        public final String text;
        public final Messenger source;

        public Message(String text, Messenger source)
        {
            this.text = text;
            this.source = source;
        }
    }

    private static MapFunc<Message, String> SelectTextLambda()
    {
        return new MapFunc<Messenger.Message, String>()
        {
            @Override
            public String map(Message element)
            {
                return element.text;
            }
        };
    }

    private FilterFunc<Message> FilterSourceLambda(final Collection<Messenger> filter)
    {
        return new FilterFunc<Message>()
        {
            @Override
            public boolean filter(Message element)
            {
                return filter.contains(element.source);
            }
        };
    }
}
