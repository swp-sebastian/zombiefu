package test.fov;

import jade.fov.ShadowCaster;
import jade.fov.ViewField;

public class ShadowCasterTests extends ViewFieldImplTest
{
    @Override
    protected ViewField getInstance()
    {
        return new ShadowCaster();
    }
}
