package universis.universys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 06/10/2017.
 */

public class DataBase {

    public static JSONObject respuestaDB (JSONObject jsonTemp) throws JSONException {
        ArrayList<JSONObject> db = new ArrayList<>();
        JSONObject jsonDB = new JSONObject().put("email", "pepito@mail").put("password", "pepitokpo91").put("tipo", "alumno");
        db.add(jsonDB);
        jsonDB = new JSONObject().put("email", "rodrigato@mail").put("password", "rodrigo1234facil").put("tipo", "profesor");
        db.add(jsonDB);

        JSONObject jsonResp = new JSONObject();

        for (JSONObject item : db) {
            if (jsonTemp.get("email").equals(item.get("email"))) {
                if (jsonTemp.get("password").equals(item.get("password"))) {
                    jsonResp.put("errorId", "200").put("tipo", item.get("tipo"));
                    break;
                } else {
                    jsonResp.put("errorId", "777");
                }
            } else {
                jsonResp.put("errorId", "680");
            }
        }
        return jsonResp;
    }




}
