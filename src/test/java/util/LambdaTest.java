package test.util;

import jade.util.Lambda;
import jade.util.Lambda.FilterFunc;
import jade.util.Lambda.MapFunc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LambdaTest
{
    private List<Integer> integers;

    @Before
    public void initLists()
    {
        integers = new ArrayList<Integer>();
        for(int i = 0; i < 10; i++)
            integers.add(i);
    }

    @Test
    public void filterUsesLambda()
    {
        final List<Integer> evaluated = new ArrayList<Integer>();

        FilterFunc<Integer> lambda = new FilterFunc<Integer>()
        {
            @Override
            public boolean filter(Integer element)
            {
                evaluated.add(element);
                return element < 5;
            }
        };

        Iterable<Integer> filtered = Lambda.filter(integers, lambda);
        Assert.assertEquals(integers, evaluated);
        for(Integer number : filtered)
        {
            Assert.assertTrue(number < 5);
            Assert.assertTrue(integers.contains(number));
        }
    }

    @Test
    public void filterLeavesIterable()
    {
        List<Integer> copy = new ArrayList<Integer>(integers);
        FilterFunc<Integer> lambda = new FilterFunc<Integer>()
        {
            @Override
            public boolean filter(Integer element)
            {
                return element % 2 == 0;
            }
        };

        Iterable<Integer> filtered = Lambda.filter(integers, lambda);
        Assert.assertEquals(copy, integers);
        for(Integer number : filtered)
        {
            Assert.assertTrue(number % 2 == 0);
            Assert.assertTrue(integers.contains(number));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void filterNullIterable()
    {
        FilterFunc<String> lambda = new FilterFunc<String>()
        {
            @Override
            public boolean filter(String element)
            {
                return true;
            }
        };

        Lambda.filter(null, lambda);
    }

    @Test(expected = IllegalArgumentException.class)
    public void filterNullLambda()
    {
        Lambda.filter(integers, null);
    }

    @Test
    public void mapUsesLambda()
    {
        final List<Integer> evaluated = new ArrayList<Integer>();

        MapFunc<Integer, Integer> lambda = new MapFunc<Integer, Integer>()
        {
            @Override
            public Integer map(Integer element)
            {
                evaluated.add(element);
                return element * element;
            }
        };

        Iterable<Integer> mapped = Lambda.map(integers, lambda);
        Assert.assertEquals(integers, evaluated);

        Iterator<Integer> numbersIter = integers.iterator();
        Iterator<Integer> mappedIter = mapped.iterator();
        while(numbersIter.hasNext() && mappedIter.hasNext())
        {
            int number = numbersIter.next();
            int actual = mappedIter.next();
            Assert.assertEquals(number * number, actual);
        }
        Assert.assertFalse(numbersIter.hasNext());
        Assert.assertFalse(mappedIter.hasNext());
    }

    @Test
    public void mapLeavesIterable()
    {
        List<Integer> copy = new ArrayList<Integer>(integers);

        MapFunc<Integer, String> lambda = new MapFunc<Integer, String>()
        {
            @Override
            public String map(Integer element)
            {
                return element.toString();
            }
        };

        Iterable<String> mapped = Lambda.map(integers, lambda);
        Assert.assertEquals(copy, integers);

        Iterator<Integer> numbersIter = integers.iterator();
        Iterator<String> mappedIter = mapped.iterator();
        while(numbersIter.hasNext() && mappedIter.hasNext())
        {
            String expected = numbersIter.next().toString();
            String actual = mappedIter.next();
            Assert.assertEquals(expected, actual);
        }
        Assert.assertFalse(numbersIter.hasNext());
        Assert.assertFalse(mappedIter.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void mapNullIterable()
    {
        MapFunc<String, String> lambda = new MapFunc<String, String>()
        {
            @Override
            public String map(String element)
            {
                return element;
            }
        };

        Lambda.map(null, lambda);
    }

    @Test(expected = IllegalArgumentException.class)
    public void mapNullLambda()
    {
        Lambda.map(integers, null);
    }

    @Test
    public void toListConvertsSet()
    {
        Set<String> expected = new HashSet<String>();
        expected.add("hello");
        expected.add("world");
        expected.add("goodbye");
        expected.add("moon");
        List<String> actual = Lambda.toList(expected);

        Assert.assertEquals(expected.size(), actual.size());
        for(String element : actual)
            Assert.assertTrue(expected.contains(element));
    }

    @Test
    public void toListMaintainsOrder()
    {
        List<String> expected = new ArrayList<String>();
        expected.add("hello");
        expected.add("world");
        expected.add("goodbye");
        expected.add("moon");
        List<String> actual = Lambda.toList(expected);

        Assert.assertEquals(expected.size(), actual.size());
        for(int i = 0; i < expected.size(); i++)
            Assert.assertEquals(expected.get(i), actual.get(i));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toListNullIterable()
    {
        Lambda.toList(null);
    }

    @Test
    public void firstEmpty()
    {
        Assert.assertNull(Lambda.first(new ArrayList<String>()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void firstNull()
    {
        Lambda.first(null);
    }

    @Test
    public void firstNumbers()
    {
        Assert.assertEquals(integers.get(0), Lambda.first(integers));
    }

    @Test
    public void filterTypeAll()
    {
        List<Integer> actual = Lambda.toList(Lambda.filterType(integers, Integer.class));
        Assert.assertEquals(integers, actual);
    }

    @Test
    public void filterTypeSome()
    {
        List<Object> objects = new ArrayList<Object>();
        objects.add(new Object());
        objects.add(Integer.valueOf(10));
        objects.add(Double.valueOf(3.14));
        objects.add("Hello World");

        List<Object> actualObjects = Lambda.toList(Lambda.filterType(objects, Object.class));
        Assert.assertEquals(objects, actualObjects);

        List<Number> numbers = Lambda.toList(Lambda.filterType(objects, Number.class));
        Assert.assertEquals(2, numbers.size());
        Assert.assertSame(objects.get(1), numbers.get(0));
        Assert.assertSame(objects.get(2), numbers.get(1));

        List<String> strings = Lambda.toList(Lambda.filterType(objects, String.class));
        Assert.assertEquals(1, strings.size());
        Assert.assertSame(objects.get(3), strings.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void filterTypeNullIterable()
    {
        Lambda.filterType(null, Number.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void filterTypeNullClass()
    {
        Lambda.filterType(integers, null);
    }

    @Test
    public void toSetConvertsList()
    {
        List<String> expected = new ArrayList<String>();
        expected.add("hello");
        expected.add("world");
        expected.add("goodbye");
        expected.add("moon");
        Set<String> actual = Lambda.toSet(expected);

        Assert.assertEquals(expected.size(), actual.size());
        for(String element : actual)
            Assert.assertTrue(expected.contains(element));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSetNullIterable()
    {
        Lambda.toSet(null);
    }
}
