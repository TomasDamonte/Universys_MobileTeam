package universis.universys;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataBase {

    public static JSONObject respuestaDB (JSONObject jsonTemp) throws JSONException {
        ArrayList<JSONObject> db = new ArrayList<>();
        db.add(new JSONObject().put("mail", "alumno@alumno").put("password", "alumno").put("tipo", "alumno"));
        db.add(new JSONObject().put("mail", "profesor@profesor").put("password", "profesor").put("tipo", "profesor"));

        JSONObject jsonResp = new JSONObject();
        int auxMail = -1;
        int auxPass = -1;
        for (int i=0;i<db.size();i++) {
            if (jsonTemp.get("mail").equals(db.get(i).get("mail"))) {
                auxMail = i;
                if (jsonTemp.get("password").equals(db.get(i).get("password"))) {
                    auxPass = i;
                    break;
                }
            }
        }
        if(auxMail!=-1 && auxPass!=-1) jsonResp.put("errorId", "200").put("tipo", db.get(auxMail).get("tipo"));
        if(auxPass == -1) jsonResp.put("errorId", "777");
        if(auxMail == -1) jsonResp.put("errorId", "680");

        return jsonResp;
    }

}
