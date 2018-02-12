package universis.universys;

import android.content.Context;
import android.view.View;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;


@RunWith(MockitoJUnitRunner.class)
public class ProfesorMainTest {

    @Mock
    ProfesorMain profesorMain;
    private View view;
    private Context contexto;

    public void setUp() throws Exception {
        contexto = getInstrumentation().getContext();
        assertNotNull(contexto);
        view = new View(contexto);
        assertNotNull(view);
    }

    @Test
    public void enviarRequest() throws Exception {
        profesorMain.enviarRequest(null);
        profesorMain.enviarRequest(view);
    }

    @Test
    public void enviarRequest1() throws Exception {
        profesorMain.enviarRequest(0,"");
        profesorMain.enviarRequest(-1,null);
    }

    @Test
    public void requestVerSolicitudes() throws Exception {
        profesorMain.requestVerSolicitudes();
    }

    @Test
    public void requestEnviarSolicitudes() throws Exception {
        profesorMain.requestEnviarSolicitudes(null);
        profesorMain.requestEnviarSolicitudes(view);
    }

    @Test
    public void modificarDatosProfesor() throws Exception {
        profesorMain.modificarDatosProfesor(null);
        profesorMain.modificarDatosProfesor(view);
    }

    @Test
    public void enviarDatosProfesor() throws Exception {
        profesorMain.enviarDatosProfesor(null);
        profesorMain.enviarDatosProfesor(view);
    }

}