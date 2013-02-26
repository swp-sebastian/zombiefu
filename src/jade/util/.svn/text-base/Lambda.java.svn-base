package jade.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * {@code Lambda} contains methods for manipulating {@code Iterable<T>}. Many of these concepts,
 * such as map and reduce, were borrowed from functional programming.
 */
public final class Lambda
{
    /**
     * Represents a lambda used in {@code filter}.
     * @param <T> the type of the elements being filtered
     */
    public interface FilterFunc<T>
    {
        /**
         * Returns a boolean indicating whether the given element should be retained or removed.
         * @param element the element being tested
         * @return a boolean indicating whether the given element should be retained
         */
        public boolean filter(T element);
    }

    /**
     * Returns a new {@code Iterable<T>} which is all the elements of the given {@code Iterable<T>}
     * for which the provided {@code FilterFunc<T>} returns true.
     * @param <T> the type of the elements being filtered
     * @param iterable the {@code Iterable<T>} being filtered
     * @param lambda returns true for any element of iterable to be retained
     * @return a filtered copy of iterable
     */
    public static <T> Iterable<T> filter(Iterable<T> iterable, FilterFunc<T> lambda)
    {
        Guard.argumentsAreNotNull(iterable, lambda);

        List<T> filtered = new ArrayList<T>();
        for(T element : iterable)
            if(lambda.filter(element))
                filtered.add(element);
        return filtered;
    }

    /**
     * Represents a lambda used in {@code map}.
     * @param <T> the type of the elements being mapped
     * @param <R> the type the elements are being mapped to
     */
    public interface MapFunc<T, R>
    {
        /**
         * Returns the element mapped to it's new value.
         * @param element the element being mapped
         * @return the value of the element mapped to its new value
         */
        public R map(T element);
    }

    /**
     * Returns a new {@code Iterable<R>} which contains every element of the given {@code
     * Iterable<T>} mapped by the provided {@code MapFunc<T, R>}.
     * @param <T> the type of the elements being mapped
     * @param <R> the type the elements are being mapped to
     * @param iterable the iterable being mapped
     * @param lambda the mapping function
     * @return a new {@code Iterable<R>} which is the result of mapping iterable
     */
    public static <T, R> Iterable<R> map(Iterable<T> iterable, MapFunc<T, R> lambda)
    {
        Guard.argumentsAreNotNull(iterable, lambda);

        List<R> mapped = new ArrayList<R>();
        for(T element : iterable)
            mapped.add(lambda.map(element));
        return mapped;
    }

    /**
     * Returns a new {@code Iterable<R>} containing only the elements of the given {@code
     * Iterable<T>} which are of type {@code R}.
     * @param <T> the type of the elements being filtered
     * @param <R> the type of the elements to be retained
     * @param iterable the iterable being queried
     * @param cls the class of {@code R}
     * @return the elements of iterable which are of type {@code R}
     */
    public static <T, R extends T> Iterable<R> filterType(Iterable<T> iterable, final Class<R> cls)
    {
        Guard.argumentsAreNotNull(iterable, cls);

        FilterFunc<T> filterR = new FilterFunc<T>()
        {
            @Override
            public boolean filter(T element)
            {
                return cls.isInstance(element);
            }
        };

        MapFunc<T, R> castR = new MapFunc<T, R>()
        {
            @Override
            @SuppressWarnings("unchecked")
            public R map(T element)
            {
                return (R)element;
            }
        };

        return map(filter(iterable, filterR), castR);
    }

    /**
     * Returns the first element of the given {@code Iterable<T>}.
     * @param <T> the type of the element being selected
     * @param iterable the {@code Iterable<T>} being queried
     * @return the first element of the iterable
     */
    public static <T> T first(Iterable<T> iterable)
    {
        Guard.argumentIsNotNull(iterable);

        Iterator<T> iterator = iterable.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * Converts an {@code Iterable<T>} to a new {@code List<T>}.
     * @param <T> the type of the elements being converted to a list
     * @param iterable the {@code Iterable<T>} to be converted
     * @return a new {@code List<T>} whose contents are that of iterable
     */
    public static <T> List<T> toList(Iterable<T> iterable)
    {
        Guard.argumentIsNotNull(iterable);

        List<T> list = new ArrayList<T>();
        for(T element : iterable)
            list.add(element);
        return list;
    }

    /**
     * Converts an {@code Iterable<T>} to a new {@code Set<T>}.
     * @param <T> the type of the elements being converted to a set
     * @param iterable the {@code Iterable<T>} to be converted
     * @return a new {@code Set<T>} whose contents are that of iterable
     */
    public static <T> Set<T> toSet(Iterable<T> iterable)
    {
        Guard.argumentIsNotNull(iterable);

        Set<T> set = new HashSet<T>();
        for(T element : iterable)
            set.add(element);
        return set;
    }
}
