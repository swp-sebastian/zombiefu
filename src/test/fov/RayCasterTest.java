package test.fov;

import jade.fov.RayCaster;
import jade.fov.ViewField;

public class RayCasterTest extends ViewFieldImplTest
{
    @Override
    protected ViewField getInstance()
    {
        return new RayCaster();
    }
}
