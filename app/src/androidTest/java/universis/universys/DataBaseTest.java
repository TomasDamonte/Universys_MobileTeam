package universis.universys;

import android.support.test.runner.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class DataBaseTest {

    @Test
    public void respuestaDB_LogIn() throws Exception {
        JSONObject test = new JSONObject().put("mail", "alumno@alumno").put("password", "alumno");
        JSONObject respuesta =(JSONObject) DataBase.respuestaDB(CHTTPRequest.postRequest(RequestTaskIds.LOGIN,URLs.LOGIN, test));
        String expected = "200";
        assertEquals(expected,respuesta.getString("errorId"));
        expected = "alumno";
        assertEquals(expected,respuesta.getString("rol"));
    }

    @Test
    public void respuestaDB_DatosPersonales_ModificarDatosPersonales() throws Exception {
        JSONObject test = new JSONObject().put("idSesion", "7541");
        JSONObject respuesta =(JSONObject) DataBase.respuestaDB(CHTTPRequest.postRequest(RequestTaskIds.DATOS_PERSONALES,URLs.DATOS_PERSONALES, test));
        JSONObject expected = new JSONObject().put("mail", "alumno@alumno").put("password", "alumno")
                .put("rol", "alumno").put("nombre", "carlos javier").put("apellido", "perez gonzalez").put("fNac", "15/10/1990")
                .put("domicilio", "Beruti 1254").put("telefono", "4215-9547").put("idSesion", "7541").put("errorId","200");
        assertEquals(expected.toString(),respuesta.toString());
        test = new JSONObject().put("idSesion", "6320");
        respuesta =(JSONObject) DataBase.respuestaDB(CHTTPRequest.postRequest(RequestTaskIds.MODIFICAR_DATOS_PERSONALES,URLs.MODIFICAR_DATOS_PERSONALES, test));
        expected = new JSONObject().put("mail", "profesor@profesor").put("password", "profesor").put("rol", "profesor")
                .put("nombre", "cacho").put("apellido", "riquelme").put("fNac", "06/03/1989").put("domicilio", "Austria 3561")
                .put("telefono", "4215-9547").put("idSesion", "6320").put("errorId","200");
        assertEquals(expected.toString(),respuesta.toString());
    }

    @Test
    public void respuestaDB_NotaAlumno_BajaMateria_CargarNotas() throws Exception {
        JSONObject test =  new JSONObject().put("catedra","a").put("carrera","b").put("materia","c");
        JSONObject respuesta =(JSONObject) DataBase.respuestaDB(CHTTPRequest.postRequest(RequestTaskIds.NOTA_ALUMNO,URLs.NOTA_ALUMNO, test));
        JSONObject expected = new JSONObject().put("catedra","a").put("carrera","b").put("materia","c").put("nota","9")
                .put("errorId","200");
        assertEquals(expected.toString(),respuesta.toString());
        test = new JSONObject().put("catedra","a").put("carrera","b").put("materia","xxxx");
        respuesta =(JSONObject) DataBase.respuestaDB(CHTTPRequest.postRequest(RequestTaskIds.BAJA_MATERIA,URLs.BAJA_MATERIA, test));
        expected = new JSONObject().put("errorId","1002");
        assertEquals(expected.toString(),respuesta.toString());
        test = new JSONObject().put("catedra","a").put("carrera","xxx").put("materia","c");
        respuesta =(JSONObject) DataBase.respuestaDB(CHTTPRequest.postRequest(RequestTaskIds.CARGAR_NOTAS,URLs.CARGAR_NOTAS, test));
        expected = new JSONObject().put("errorId","1001");
        assertEquals(expected.toString(),respuesta.toString());
    }

}
