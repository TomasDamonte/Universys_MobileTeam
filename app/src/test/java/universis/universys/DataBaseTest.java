package universis.universys;



import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataBaseTest {

    @Test
    public void respuestaDB() throws Exception {
        String expected = "200";
        JSONObject test = new JSONObject().put("mail", "alumno@alumno").put("password", "alumno");
        JSONObject respuesta =(JSONObject) DataBase.respuestaDB(CHTTPRequest.postRequest(RequestTaskIds.LOGIN,URLs.LOGIN, test));
        assertEquals(expected,respuesta.getString("errorId"));
        expected = "alumno";
        assertEquals(expected,respuesta.getString("tipo"));
    }
}
