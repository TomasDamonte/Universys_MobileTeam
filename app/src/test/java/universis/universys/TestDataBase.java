package universis.universys;



import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDataBase {

    @Test
    public void testRespuestaDB() throws Exception {
        JSONObject expected = new JSONObject().put("errorId", "200").put("tipo", "alumno");
        JSONObject test = new JSONObject().put("mail", "alumno@alumno").put("password", "alumno");
        assertEquals(expected,DataBase.respuestaDB(test));
    }
}
