package universis.universys;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;

import java.util.LinkedHashMap;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class JSONBuilderTest {

    public JSONBuilder jsonBuilder = new JSONBuilder();
    public JSONObject expected = new JSONObject().put("apiVer","1.0").put("idSesion","000");

    public JSONBuilderTest() throws JSONException {
    }

    public void setUp() throws Exception {
        expected = new JSONObject().put("idSesion","000").put("apiVer","1.0");
    }

    @Test
    public void testlogIn() throws Exception {
        expected.put("mail","alumno@alumno").put("password","alumno");
        assertEquals(expected.toString(),jsonBuilder.logIn("alumno@alumno","alumno").toString());
    }

    @Test
    public void requestBasico() throws Exception {
        assertEquals(expected.toString(),jsonBuilder.requestBasico().toString());
    }

    @Test
    public void requestGenerico() throws Exception {
        expected.put("catedra","didier").put("carrera","Tecnicatura en Programacion").put("materia","testing");
        assertEquals(expected.toString(),jsonBuilder.requestGenerico("didier","Tecnicatura en Programacion","testing").toString());
    }

    @Test
    public void requestGenerico1() throws Exception {
        expected.put("catedra","didier").put("carrera","Tecnicatura en Programacion").put("materia","testing").put("alumno","pepe").put("nota","7");
        assertEquals(expected.toString(),jsonBuilder.requestGenerico("didier","Tecnicatura en Programacion","testing","pepe","7").toString());
    }

    @Test
    public void enviarSolicitudesInscripcion() throws Exception {
        LinkedHashMap<String,String> test = new LinkedHashMap();
        test.put("741","aceptada");
        test.put("469","rechazada");
        expected.put("solicitudes",test);
        assertEquals(expected.toString(),jsonBuilder.enviarSolicitudesInscripcion(test).toString());
    }

    @Test
    public void modificarDatosPersonales() throws Exception {
        String[] datos = {"juan","perez","corrientes 451","juanperez@gmail.com","04/09/1990","4157-6201"};
        expected.put("nombre","juan").put("apellido","perez").put("domicilio","corrientes 451").put("mail","juanperez@gmail.com").put("fNac","04/09/1990")
                .put("telefono","4157-6201");
        assertEquals(expected.toString(),jsonBuilder.modificarDatosPersonales(datos).toString());
    }

    @Test
    public void inscripcionAMateria() throws Exception {
        expected.put("idCursada","123");
        assertEquals(expected.toString(),jsonBuilder.inscripcionAMateria("123").toString());
    }

}