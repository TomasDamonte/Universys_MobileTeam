package universis.universys;

import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class JSONBuilderTest {

    private JSONBuilder jsonBuilder = new JSONBuilder();
    private JSONObject expected;

    public void setUp() throws Exception {
        expected = new JSONObject().put("apiVer","1.0").put("idSesion","754");
    }
    @Test
    public void logIn() throws Exception {
        expected.put("mail","alumno@alumno").put("password","alumno");
        assertEquals(expected,jsonBuilder.logIn("alumno@alumno","alumno"));
    }

    @Test
    public void requestBasico() throws Exception {
        assertEquals(expected,jsonBuilder.requestBasico());
    }

    @Test
    public void requestGenerico() throws Exception {
        expected.put("catedra","didier").put("carrera","Tecnicatura en Programacion").put("materia","testing");
        assertEquals(expected,jsonBuilder.requestGenerico("didier","Tecnicatura en Programacion","testing"));
    }

    @Test
    public void requestGenerico1() throws Exception {
        expected.put("catedra","didier").put("carrera","Tecnicatura en Programacion").put("materia","testing").put("alumno","pepe").put("nota","7");
        assertEquals(expected,jsonBuilder.requestGenerico("didier","Tecnicatura en Programacion","testing","pepe","7"));
    }

    @Test
    public void enviarSolicitudesInscripcion() throws Exception {
        HashMap<String,String> test = new HashMap();
        test.put("741","aceptada");
        test.put("469","rechazada");
        expected.put("solicitudes",test);
        assertEquals(expected,jsonBuilder.enviarSolicitudesInscripcion(test));
    }

    @Test
    public void modificarDatosPersonales() throws Exception {
        String[] datos = {"juan","perez","corrientes 451","juanperez@gmail.com","04/09/1990","4157-6201"};
        expected.put("nombre","juan").put("apellido","perez").put("domicilio","corrientes 451").put("mail","juanperez@gmail.com").put("fNac","04/09/1990")
                .put("telefono","4157-6201");
        assertEquals(expected,jsonBuilder.modificarDatosPersonales(datos));
    }

    @Test
    public void inscripcionAMateria() throws Exception {
        expected.put("idCursada","123");
        assertEquals(expected,jsonBuilder.inscripcionAMateria("123"));
    }

}