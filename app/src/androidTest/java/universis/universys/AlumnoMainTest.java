package universis.universys;

import android.content.Context;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(MockitoJUnitRunner.class)
public class AlumnoMainTest {

    @Mock
    AlumnoMain alumnoMain;
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
        alumnoMain.enviarRequest(null);
        alumnoMain.enviarRequest(view);
    }

    @Test
    public void modificarDatosAlumno() throws Exception {
        alumnoMain.modificarDatosAlumno(null);
        alumnoMain.modificarDatosAlumno(view);
    }

    @Test
    public void enviarDatosAlumno() throws Exception {
        alumnoMain.enviarDatosAlumno(null);
        alumnoMain.enviarDatosAlumno(view);
    }

    @Test
    public void inscripcionesDisponibles() throws Exception {
        alumnoMain.inscripcionesDisponibles();
    }

}