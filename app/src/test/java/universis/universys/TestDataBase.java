package universis.universys;



import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDataBase {

    @Test
    public void TestRespuestaDB() throws JSONException {
        JSONObject expected = new JSONObject().put("errorId", "200").put("tipo", "alumno");
        JSONObject test = new JSONObject().put("email", "pepito@mail").put("password", "pepitokpo91").put("tipo", "alumno");
        assertEquals(expected,DataBase.respuestaDB(test));
    }
}
