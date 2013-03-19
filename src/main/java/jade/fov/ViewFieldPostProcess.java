package jade.fov;

import jade.core.World;
import jade.util.datatype.Coordinate;
import java.util.Collection;

/**
 * Represents a post processing of a view field. This can be used to add or remove artifacts from
 * the view field, or filter the view field into a specific shape.
 */
public abstract class ViewFieldPostProcess
{
    /**
     * Performs the post processing on the calculated view field. The provided {@code
     * Collection<Coordinate>} will be modified in place. The post processing should be done with
     * respect to the parameters with which the view field was generated. In other words, the
     * processed view field should have a radius of r and be centered at (x, y).
     * @param field the view field being processed
     * @param world the {@code World} on which the view field was generated
     * @param x the x value of the location from which the view field was generated
     * @param y the y value of the location from which the view field was generated
     * @param r the radius with which the view field was generated
     */
    public abstract void process(Collection<Coordinate> field, World world, int x, int y, int r);
}
