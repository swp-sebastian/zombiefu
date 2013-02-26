package test.ui;

import jade.core.World;
import jade.ui.Camera;
import jade.ui.Terminal;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import java.util.Collection;
import java.util.HashSet;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CameraTest
{
    private ConcreteTerminal term;
    private Camera camera;
    private World world;
    private ColoredChar seen;
    private ColoredChar unseen;

    @Before
    public void init()
    {
        term = Mockito.spy(new ConcreteTerminal());
        camera = Mockito.mock(Camera.class);
        world = Mockito.mock(World.class);

        seen = ColoredChar.create('.');
        unseen = ColoredChar.create('#');

        Mockito.when(world.tileAt(Mockito.anyInt(), Mockito.anyInt())).thenReturn(unseen);
    }

    @Test
    public void registerCameraAdds()
    {
        Coordinate location = new Coordinate(4, 7);
        term.registerCamera(camera, location);
        Assert.assertEquals(location, term.cameraCenter(camera));
        Assert.assertTrue(term.cameraRegistered(camera));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullCameraCenter()
    {
        term.cameraCenter(null);
    }

    @Test(expected = IllegalStateException.class)
    public void unregisteredCameraCenter()
    {
        term.cameraCenter(camera);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerNullCamera()
    {
        term.registerCamera(null, new Coordinate(4, 6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerNullLocation()
    {
        term.registerCamera(camera, null);
    }

    @Test
    public void unregisterCameraRemoves()
    {
        term.registerCamera(camera, new Coordinate(6, 3));
        term.unregisterCamera(camera);
        Assert.assertFalse(term.cameraRegistered(camera));
    }

    @Test
    public void unregisterUnregisteredNoOp()
    {
        term.unregisterCamera(camera);
    }

    @Test
    public void registerIntCallCoord()
    {
        term.registerCamera(camera, 4, 8);
        Mockito.verify(term).registerCamera(camera, new Coordinate(4, 8));
    }

    @Test
    public void bufferCameraSquare()
    {
        int cx = 5;
        int cy = 8;
        Collection<Coordinate> fov = new HashSet<Coordinate>();
        for(int x = cx - 1; x <= cx + 1; x++)
            for(int y = cy - 1; y <= cy + 1; y++)
            {
                fov.add(new Coordinate(x, y));
                Mockito.when(world.tileAt(x, y)).thenReturn(seen);
            }
        Mockito.when(camera.getViewField()).thenReturn(fov);
        Mockito.when(camera.x()).thenReturn(cx);
        Mockito.when(camera.y()).thenReturn(cy);
        Mockito.when(camera.world()).thenReturn(world);

        int sx = 3;
        int sy = 3;

        term.registerCamera(camera, sx, sy);
        term.bufferCamera(camera);

        for(int x = 0; x <= 10; x++)
            for(int y = 0; y <= 10; y++)
            {
                if(x <= 4 && x >= 2 && y <= 4 && y >= 2)
                {
                    Mockito.verify(term).bufferChar(new Coordinate(x, y), seen);
                    Assert.assertEquals(seen, term.charAt(x, y));
                }
                else
                {
                    Mockito.verify(term, Mockito.never()).bufferChar(new Coordinate(x, y), seen);
                    Assert.assertNull(term.charAt(x, y));
                }
            }
    }

    @Test
    public void bufferCameraDirectional()
    {
        int cx = 10;
        int cy = 34;
        Collection<Coordinate> fov = new HashSet<Coordinate>();
        for(int x = cx - 5; x <= cx + 5; x += 5)
            for(int y = cy - 5; y <= cy + 5; y += 5)
            {
                fov.add(new Coordinate(x, y));
                Mockito.when(world.tileAt(x, y)).thenReturn(seen);
            }
        Mockito.when(camera.getViewField()).thenReturn(fov);
        Mockito.when(camera.x()).thenReturn(cx);
        Mockito.when(camera.y()).thenReturn(cy);
        Mockito.when(camera.world()).thenReturn(world);

        int sx = 10;
        int sy = 5;

        term.registerCamera(camera, sx, sy);
        term.bufferCamera(camera);

        for(int x = sx - 5; x <= sx + 5; x += 5)
            for(int y = sy - 5; y <= sy + 5; y += 5)
            {
                Mockito.verify(term).bufferChar(new Coordinate(x, y), seen);
                Assert.assertEquals(seen, term.charAt(x, y));
            }
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferNullCamera()
    {
        term.bufferCamera(null);
    }

    @Test(expected = IllegalStateException.class)
    public void bufferUnregisteredCamera()
    {
        Assert.assertFalse(term.cameraRegistered(camera));
        term.bufferCamera(camera);
    }

    @Test
    public void bufferCamerasBuffsNone()
    {
        term.bufferCameras();
        Mockito.verify(term, Mockito.never()).bufferCamera(Mockito.any(Camera.class));
    }

    @Test
    public void bufferCamerasBuffsOne()
    {
        term.registerCamera(camera, new Coordinate(4, 6));
        term.bufferCameras();
        Mockito.verify(term).bufferCamera(camera);
    }

    @Test
    public void bufferCamerasBuffsMany()
    {
        Collection<Camera> cameras = new HashSet<Camera>();
        for(int i = 0; i < 5; i++)
            cameras.add(Mockito.mock(Camera.class));
        for(Camera cam : cameras)
            term.registerCamera(cam, new Coordinate(0, 0));
        term.bufferCameras();
        for(Camera cam : cameras)
            Mockito.verify(term).bufferCamera(cam);
    }

    @Test
    public void bufferRelativeSquare()
    {
        int cx = 5;
        int cy = 8;
        Mockito.when(camera.getViewField()).thenThrow(new RuntimeException());
        Mockito.when(camera.x()).thenReturn(cx);
        Mockito.when(camera.y()).thenReturn(cy);
        Mockito.when(camera.world()).thenThrow(new RuntimeException());

        int sx = 3;
        int sy = 3;

        term.registerCamera(camera, sx, sy);

        for(int dx = -5; dx <= 5; dx += 5)
            for(int dy = -5; dy <= 5; dy += 5)
            {
                ColoredChar ch = ColoredChar.create((char)(dx + dy));
                term.bufferRelative(camera, ch, cx + dx, cy + dy);

                Mockito.verify(term).bufferChar(new Coordinate(sx + dx, sy + dy), ch);
                Assert.assertEquals(ch, term.charAt(sx + dx, sy + dy));
            }
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferRelativeNullCamera()
    {
        term.bufferRelative(null, ColoredChar.create('*'), 5, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferRelativeNullChar()
    {
        term.registerCamera(camera, new Coordinate(4, 5));
        term.bufferRelative(camera, null, 7, 3);
    }

    @Test(expected = IllegalStateException.class)
    public void bufferRelativeUnregistered()
    {
        term.bufferRelative(camera, ColoredChar.create('*'), 5, 4);
    }

    @Test
    public void bufferRelativeCoordinate()
    {
        term.registerCamera(camera, new Coordinate(4, 6));

        ColoredChar ch = ColoredChar.create('*');
        Coordinate pos = new Coordinate(6, 3);
        term.bufferRelative(camera, ch, pos);

        Mockito.verify(term).bufferRelative(camera, ch, pos.x(), pos.y());
    }

    @Test(expected = IllegalArgumentException.class)
    public void bufferRelativeNullCoordinate()
    {
        term.registerCamera(camera, new Coordinate(4, 6));
        term.bufferRelative(camera, ColoredChar.create('*'), null);
    }

    private static class ConcreteTerminal extends Terminal
    {
        @Override
        public void refreshScreen()
        {}

        @Override
        public char getKey()
        {
            return 0;
        }
    }
}
