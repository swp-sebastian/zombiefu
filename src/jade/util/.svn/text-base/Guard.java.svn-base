package jade.util;

/**
 * A collection of methods meant to help check method preconditions and postconditions.
 */
public final class Guard
{
    /**
     * Throws an {@code IllegalArgumentException} if the supplied argument is null.
     * @param argument the argument being tested
     */
    public static void argumentIsNotNull(Object argument)
    {
        if(argument == null)
            throw new IllegalArgumentException();
    }

    /**
     * Throws an {@code IllegalArgumentException} if any of the supplied arguments is null.
     * @param arguments the arguments being tested
     */
    public static void argumentsAreNotNull(Object...arguments)
    {
        for(Object argument : arguments)
            argumentIsNotNull(argument);
    }

    /**
     * Throws an {@code IllegalArgumentException} if the supplied argument is not a positive
     * integer. In other words, the argument must be in the set {1, 2,3, ...}, or the set of all
     * natural numbers.
     * @param argument the argument being tested
     */
    public static void argumentIsPositive(int argument)
    {
        if(argument <= 0)
            throw new IllegalArgumentException();
    }

    /**
     * Throws an {@code IllegalArgumentException} if any of the supplied arguments are not a
     * positive integer. In other words, each argument must be in the set {1, 2,3, ...}, or the set
     * of all natural numbers.
     * @param arguments the arguments being tested
     */
    public static void argumentsArePositive(int...arguments)
    {
        for(int argument : arguments)
            argumentIsPositive(argument);
    }

    /**
     * Throws an {@code IllegalArgumentException} if the supplied argument is negative integer. In
     * other words, the argument must be in the set {0, 1, 2,3, ...}, or the set of all positive
     * integers.
     * @param argument the argument being tested
     */
    public static void argumentIsNonNegative(int argument)
    {
        if(argument < 0)
            throw new IllegalArgumentException();
    }

    /**
     * Throws an {@code IllegalArgumentException} if any of the supplied arguments are a negative
     * integer. In other words, each argument must be in the set {0, 1, 2,3, ...}, or the set of all
     * positive integer.
     * @param arguments the arguments being tested
     */
    public static void argumentsAreNonNegative(int...arguments)
    {
        for(int argument : arguments)
            argumentIsNonNegative(argument);
    }

    /**
     * Throws an {@code IndexOutOfBoundsException} if the supplied bound is not between 0
     * (inclusive) and max (exclusive).
     * @param argument the argument being tested
     * @param max the upper bound (exclusive) of the argument
     */
    public static void argumentInsideBound(int argument, int max)
    {
        if(argument < 0 || argument >= max)
            throw new IndexOutOfBoundsException();
    }

    /**
     * Throws an {@code IndexOutOfBoundsException} if the supplied (x, y) coordinate is not between
     * inside the given bounds. In other words, x and y must be non-negative integers, with x less
     * than width, and y less than height.
     * @param x the x location being tested
     * @param y the y location being tested
     * @param width the upper bound (exclusive) of x
     * @param height the upper bound (exclusive) of y.
     */
    public static void argumentsInsideBounds(int x, int y, int width, int height)
    {
        argumentInsideBound(x, width);
        argumentInsideBound(y, height);
    }

    /**
     * Throws an {@code IllegalStateException} if the given condition is not true.
     * @param condition the state being tested
     */
    public static void verifyState(boolean condition)
    {
        if(!condition)
            throw new IllegalStateException();
    }

    /**
     * Throws an {@code IllegalArgumentException} if the given condition is not true.
     * @param condition the state being tested
     */
    public static void validateArgument(boolean condition)
    {
        if(!condition)
            throw new IllegalArgumentException();
    }

    /**
     * Returns the given return value if it is not null. Otherwise, an {@code IllegalStateException}
     * is thrown. The typical usage would be as the return statement in another method (eg {@code
     * return Guard.returnNonNull(value);}).
     * @param <T> the type of the value being tested for non-null
     * @param value the value being returned and tested for non-null
     * @return value, if value is not null
     */
    public static <T> T returnNonNull(T value)
    {
        if(value == null)
            throw new IllegalStateException();

        return value;
    }
}
