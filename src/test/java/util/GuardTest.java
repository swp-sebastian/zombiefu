package test.util;

import jade.util.Guard;
import junit.framework.Assert;
import org.junit.Test;

public class GuardTest
{
    @Test(expected = IllegalArgumentException.class)
    public void argumentNotNullThrows()
    {
        Guard.argumentIsNotNull(null);
    }

    @Test
    public void argumentNotNullSafe()
    {
        Guard.argumentIsNotNull(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void argumentsNullOne()
    {
        Guard.argumentsAreNotNull(new Object(), null, new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void argumentsNullAll()
    {
        Guard.argumentsAreNotNull(null, null, null);
    }

    @Test
    public void argumentsNullNone()
    {
        Guard.argumentsAreNotNull(new Object(), new Object(), new Object());
    }

    @Test
    public void argumentNonNegativeZero()
    {
        Guard.argumentIsNonNegative(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void argumentNonNegativeNegative()
    {
        Guard.argumentIsNonNegative(-1);
    }

    @Test
    public void argumentNonNegativeSafe()
    {
        Guard.argumentIsNonNegative(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void argumentsNonNegativeOneNeg()
    {
        Guard.argumentsAreNonNegative(1, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void argumentsNonNegativeAllNeg()
    {
        Guard.argumentsAreNonNegative(-1, -1, -1);
    }

    @Test
    public void argumentsNonNegativeAllPos()
    {
        Guard.argumentsAreNonNegative(1, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void argumentNotPositiveZero()
    {
        Guard.argumentIsPositive(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void argumentNotPositiveNegative()
    {
        Guard.argumentIsPositive(-1);
    }

    @Test
    public void argumentPositiveSafe()
    {
        Guard.argumentIsPositive(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void argumentsPositiveOneNeg()
    {
        Guard.argumentsArePositive(1, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void argumentsPositiveAllNeg()
    {
        Guard.argumentsArePositive(-1, -1, -1);
    }

    @Test
    public void argumentsPositiveAllPos()
    {
        Guard.argumentsArePositive(1, 1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void argumentBoundNegThrows()
    {
        Guard.argumentInsideBound(-1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void argumentBoundPosThrows()
    {
        Guard.argumentInsideBound(11, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void argumentBoundMaxThrows()
    {
        Guard.argumentInsideBound(10, 10);
    }

    @Test
    public void argumentBoundZeroSafe()
    {
        Guard.argumentInsideBound(0, 10);
    }

    @Test
    public void argumentBoundPosSafe()
    {
        Guard.argumentInsideBound(5, 10);
    }

    @Test
    public void argumentBoundCloseMaxSafe()
    {
        Guard.argumentInsideBound(9, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void argumentPosBoundNegXThrows()
    {
        Guard.argumentsInsideBounds(-1, 0, 10, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void argumentPosBoundNegYThrows()
    {
        Guard.argumentsInsideBounds(0, -1, 10, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void argumentPosBoundPosXThrows()
    {
        Guard.argumentsInsideBounds(11, 0, 10, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void argumentPosBoundPosYThrows()
    {
        Guard.argumentsInsideBounds(0, 11, 10, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void argumentPosBoundMaxXThrows()
    {
        Guard.argumentsInsideBounds(10, 0, 10, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void argumentPosBoundMaxYThrows()
    {
        Guard.argumentsInsideBounds(0, 10, 10, 10);
    }

    @Test
    public void argumentPosBoundZeroSafe()
    {
        Guard.argumentsInsideBounds(0, 0, 10, 10);
    }

    @Test
    public void argumentPosBoundPosThrows()
    {
        Guard.argumentsInsideBounds(5, 5, 10, 10);
    }

    @Test
    public void validateTrue()
    {
        Guard.verifyState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void validateFalse()
    {
        Guard.verifyState(false);
    }

    @Test
    public void validateArgumentTrue()
    {
        Guard.validateArgument(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateArgumentFalse()
    {
        Guard.validateArgument(false);
    }

    @Test
    public void returnNonNull()
    {
        String expected = "expected";
        String actual = Guard.returnNonNull(expected);
        Assert.assertSame(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void returnNull()
    {
        Guard.returnNonNull((String)null);
    }
}
