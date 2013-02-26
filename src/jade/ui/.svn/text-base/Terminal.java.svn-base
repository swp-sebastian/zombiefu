package jade.ui;

import jade.core.World;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * A terminal used for text mode IO. Features a screen buffer with save and recall, the ability to
 * draw camera views on screen, and keyboard input.
 */
public abstract class Terminal
{
    private Map<Coordinate, ColoredChar> buffer;
    private Map<Coordinate, ColoredChar> saved;
    private Map<Camera, Coordinate> cameras;

    /**
     * Constructs a new {@code Terminal} with empty buffers.
     */
    public Terminal()
    {
        buffer = new HashMap<Coordinate, ColoredChar>();
        saved = new HashMap<Coordinate, ColoredChar>();
        cameras = new HashMap<Camera, Coordinate>();
    }

    /**
     * Refreshes the screen to reflect the current state of the buffer. Until this method is called,
     * no changes to the buffer should be displayed.
     * @throws InterruptedException
     */
    public abstract void refreshScreen();
    
    protected final Map<Coordinate, ColoredChar> getBuffer()
    {
        return buffer;
    }

    /**
     * Returns the next key press as a {@code char}. This method block until a key press is
     * available.
     * @return the next key press
     * @throws InterruptedException
     */
    public abstract char getKey() throws InterruptedException;

    /**
     * Places a character into the buffer at the specified coordinates.
     * @param coord the location of the character in the buffer
     * @param ch the character being buffered
     */
    public void bufferChar(Coordinate coord, ColoredChar ch)
    {
        Guard.argumentsAreNotNull(coord, ch);

        buffer.put(coord, ch);
    }

    /**
     * Places a character into the buffer at the specified coordinates.
     * @param x the x value of the location of the character in the buffer
     * @param y the y value of the location of the character in the buffer
     * @param ch the character being buffered
     */
    public final void bufferChar(int x, int y, ColoredChar ch)
    {
        bufferChar(new Coordinate(x, y), ch);
    }

    /**
     * Returns the character at the specified coordinates. Null is returned if nothing is in the
     * buffer.
     * @param coord the location in the buffer being queried
     * @return the character at the specified coordinates
     */
    public ColoredChar charAt(Coordinate coord)
    {
        return buffer.get(coord);
    }

    /**
     * Returns the character at the specified coordinates. Null is returned if nothing is in the
     * buffer.
     * @param x the x value of location in the buffer being queried
     * @param y the y value of location in the buffer being queried
     * @return the character at the specified coordinates
     */
    public final ColoredChar charAt(int x, int y)
    {
        return charAt(new Coordinate(x, y));
    }

    /**
     * Buffers a {@code String} at (x, y) on the screen with the given {@code Color}
     * @param x the x value of the location of the first character of str
     * @param y the y value of the location of the first character of str
     * @param str the {@code String} being buffered
     * @param color the {@code Color} of the {@code String} being buffered
     */
    public final void bufferString(int x, int y, String str, Color color)
    {
        Guard.argumentIsNotNull(str);

        for(int i = 0; i < str.length(); i++)
        {
            Coordinate pos = new Coordinate(x + i, y);
            ColoredChar ch = ColoredChar.create(str.charAt(i), color);
            bufferChar(pos, ch);
        }
    }

    /**
     * Buffers a {@code String} at location on the screen with the given {@code Color}
     * @param coord the location of the first character of str
     * @param str the {@code String} being buffered
     * @param color the {@code Color} of the {@code String} being buffered
     */
    public final void bufferString(Coordinate coord, String str, Color color)
    {
        Guard.argumentIsNotNull(coord);

        bufferString(coord.x(), coord.y(), str, color);
    }

    /**
     * Buffers a {@code String} at (x, y) on the screen with a default {@code Color} of white.
     * @param x the x value of the location of the first character of str
     * @param y the y value of the location of the first character of str
     * @param str the {@code String} being buffered
     */
    public final void bufferString(int x, int y, String str)
    {
        bufferString(x, y, str, Color.white);
    }

    /**
     * Buffers a {@code String} at location on the screen with a default {@code Color} of white.
     * @param coord the location of the first character of str
     * @param str the {@code String} being buffered
     */
    public final void bufferString(Coordinate coord, String str)
    {
        bufferString(coord, str, Color.white);
    }

    /**
     * Clears all contents of the buffer.
     */
    public void clearBuffer()
    {
        buffer.clear();
    }

    /**
     * Saves the current state of the buffer. The buffer can be reverted to this state by calling
     * {@code recallBuffer()}.
     */
    public void saveBuffer()
    {
        saved.clear();
        saved.putAll(buffer);
    }

    /**
     * Returns the buffer to the last saved state. A saved state can be made by calling {@code
     * saveBuffer()}.
     */
    public void recallBuffer()
    {
        buffer.clear();
        buffer.putAll(saved);
    }

    /**
     * Registers a {@code Camera} so that when {@code bufferCamera} is called its view field is
     * drawn at the provided screen center.
     * @param camera the {@code Camera} being registered
     * @param screenCenter the location on screen where the {@code Camera} center will be drawn
     */
    public void registerCamera(Camera camera, Coordinate screenCenter)
    {
        Guard.argumentsAreNotNull(camera, screenCenter);

        cameras.put(camera, screenCenter);
    }

    /**
     * Registers a {@code Camera} so that when {@code bufferCamera} is called its view field is
     * drawn at the provided screen center.
     * @param camera the {@code Camera} being registered
     * @param screenCenterX the x value of the location on screen where the {@code Camera} center
     *        will be drawn
     * @param screenCenterY the y value of the location on screen where the {@code Camera} center
     *        will be drawn
     */
    public final void registerCamera(Camera camera, int screenCenterX, int screenCenterY)
    {
        registerCamera(camera, new Coordinate(screenCenterX, screenCenterY));
    }

    /**
     * Returns the registered screen center for the provided {@code Camera}
     * @param camera the {@code Camera} whose screen center is being queried
     * @return the registered screen center for the provided {@code Camera}
     */
    public Coordinate cameraCenter(Camera camera)
    {
        Guard.argumentIsNotNull(camera);
        Guard.verifyState(cameraRegistered(camera));

        return cameras.get(camera);
    }

    /**
     * Returns true if the {@code Camera} is registered.
     * @param camera the {@code Camera} being queried
     * @return true if the {@code Camera} is registered.
     */
    public boolean cameraRegistered(Camera camera)
    {
        return cameras.containsKey(camera);
    }

    /**
     * Unregisters the {@code Camera} so that it cannot be drawn on screen.
     * @param camera the {@code Camera} being unregistered
     */
    public void unregisterCamera(Camera camera)
    {
        cameras.remove(camera);
    }

    /**
     * Buffers the view field of the camera at its registered screen center. For each location in
     * the view field, the tile on {@code World} of the {@code Camera} is buffered relative to the
     * registered screen center. The view field will be buffered regardless of the size on screen,
     * so care should be taken with the way the view field is calculated if a certain size on screen
     * is needed.
     * @param camera the {@code Camera} whose fiew view is being buffered
     */
    public void bufferCamera(Camera camera)
    {
        Guard.argumentIsNotNull(camera);
        Guard.verifyState(cameraRegistered(camera));

        Coordinate screenCenter = cameras.get(camera);
        int offX = screenCenter.x() - camera.x();
        int offY = screenCenter.y() - camera.y();
        World world = camera.world();
        for(Coordinate coord : camera.getViewField())
            bufferChar(coord.getTranslated(offX, offY), world.look(coord));
    }

    /**
     * Calls {@code bufferCamera} for every {@code Camera} registered with the {@code Terminal}.
     */
    public final void bufferCameras()
    {
        for(Camera camera : cameras.keySet())
            bufferCamera(camera);
    }

    /**
     * Buffers the given {@code ColoredChar} relative to the registered screen center of the {@code
     * Camera}.
     * @param camera the {@code Camera} relative to which the {@code ColoredChar} will be buffered
     * @param ch the {@code} ColoredChar to buffer
     * @param x the x value of the location of the {@code ColoredChar} (on the {@code} World}, not
     *        the screen)
     * @param y the y value of the location of the {@code ColoredChar} (on the {@code} World}, not
     *        the screen)
     */
    public void bufferRelative(Camera camera, ColoredChar ch, int x, int y)
    {
        Guard.argumentIsNotNull(camera);
        Guard.verifyState(cameraRegistered(camera));

        Coordinate screenCenter = cameras.get(camera);
        int offX = screenCenter.x() - camera.x();
        int offY = screenCenter.y() - camera.y();
        bufferChar(x + offX, y + offY, ch);
    }

    /**
     * Buffers the given {@code ColoredChar} relative to the registered screen center of the {@code
     * Camera}.
     * @param camera the {@code Camera} relative to which the {@code ColoredChar} will be buffered
     * @param ch the {@code} ColoredChar to buffer
     * @param pos the location of the {@code ColoredChar} (on the {@code} World}, not the screen)
     */
    public final void bufferRelative(Camera camera, ColoredChar ch, Coordinate pos)
    {
        Guard.argumentIsNotNull(pos);

        bufferRelative(camera, ch, pos.x(), pos.y());
    }
}
