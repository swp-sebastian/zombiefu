package jade.ui;

import jade.core.World;
import jade.util.datatype.Coordinate;
import java.util.Collection;

/**
 * An interface used by {@code Terminal} to draw the cells visible on a {@code World} at a specific
 * location. Notice that for an {@code Actor} to implement the interface, only {@code
 * getViewField()} is needed. Thus it would be easy to always show what a specific {@code Actor} can
 * see.
 */
public interface Camera
{
    /**
     * Returns the x location of the {@code Camera}.
     * @return the x location of the {@code Camera}
     */
    public int x();

    /**
     * Returns the y location of the {@code Camera}.
     * @return the y location of the {@code Camera}
     */
    public int y();

    /**
     * Returns the {@code World} the {@code Camera} is observing.
     * @return the {@code World} the {@code Camera} is observing
     */
    public World world();

    /**
     * Returns the current view field of the {@code Camera}
     * @return the current view field of the {@code Camera}
     */
    public Collection<Coordinate> getViewField();
}
