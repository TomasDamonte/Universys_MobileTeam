package universis.universys;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class DataBase {

    public static Object respuestaDB (CHTTPRequest request) throws JSONException {
        JSONObject jsonReq = request.getBody();
        if(request.getTaskId() == RequestTaskIds.LOGIN || request.getTaskId() == RequestTaskIds.DATOS_PERSONALES) {
            JSONObject jsonResp = new JSONObject();
            ArrayList<JSONObject> db = new ArrayList<>();
            db.add(new JSONObject().put("mail", "alumno@alumno").put("password", "alumno").put("tipo", "alumno").put("nombre", "carlos javier").put("apellido", "perez gonzalez").put("fNac", "15/10/1990").put("domicilio", "Beruti 1254").put("telefono", "4215-9547"));
            db.add(new JSONObject().put("mail", "profesor@profesor").put("password", "profesor").put("tipo", "profesor").put("nombre", "cacho").put("apellido", "riquelme").put("fNac", "06/03/1989").put("domicilio", "Austria 3561").put("telefono", "4215-9547"));

            int auxMail = -1;
            int auxPass = -1;
            for (int i = 0; i < db.size(); i++) {
                if (jsonReq.get("mail").equals(db.get(i).get("mail"))) {
                    auxMail = i;
                    if (jsonReq.get("password").equals(db.get(i).get("password"))) {
                        auxPass = i;
                        break;
                    }
                }
            }
            if (auxMail != -1 && auxPass != -1)
                return db.get(auxMail).put(Error.ERROR_ID, Error.SUCCESS).put("idSesion", "123");
            if (auxPass == -1) jsonResp.put(Error.ERROR_ID, Error.PASSWORD_ERROR);
            if (auxMail == -1) jsonResp.put(Error.ERROR_ID, Error.EMAIL_ERROR);
            return jsonResp;
        }

        if(request.getTaskId() == RequestTaskIds.FICHADA_ALUMNO) {
            JSONArray jsonResp = new JSONArray();
            JSONObject db = new JSONObject().put("catedra","a").put("carrera","b").put("materia","c");

            if(jsonReq.getString("catedra").equals(db.getString("catedra")) && jsonReq.getString("carrera").equals(db.getString("carrera")) && jsonReq.getString("materia").equals(db.getString("materia"))) {
                jsonResp.put(new JSONObject().put(Error.ERROR_ID,Error.SUCCESS));
                for (int i = 0; i < 20; i++) {
                    JSONObject temp = new JSONObject();
                    temp.put("fecha", i + 1 + "/10/2017");
                    if (i % 2 == 0) temp.put("presente", "SI");
                    else temp.put("presente", "NO");
                    jsonResp.put(temp);
                }
            } else {
                if(!jsonReq.getString("catedra").equals(db.getString("catedra")))
                    jsonResp.put(new JSONObject().put(Error.ERROR_ID,Error.CATEDRA_ERROR));
                if(!jsonReq.getString("carrera").equals(db.getString("carrera")))
                    jsonResp.put(new JSONObject().put(Error.ERROR_ID,Error.CARRERA_ERROR));
                if(!jsonReq.getString("materia").equals(db.getString("materia")))
                    jsonResp.put(new JSONObject().put(Error.ERROR_ID,Error.MATERIA_ERROR));
            }
            return jsonResp;
        }
        return null;
    }
}
