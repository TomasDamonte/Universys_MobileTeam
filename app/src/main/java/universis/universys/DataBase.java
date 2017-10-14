package universis.universys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class DataBase {

    public static Object respuestaDB (CHTTPRequest request) throws JSONException {

        if(request.getTaskId() == RequestTaskIds.LOGIN || request.getTaskId() == RequestTaskIds.DATOS_PERSONALES) {
            JSONObject jsonResp = new JSONObject();
            JSONObject jsonTemp = request.getBody();
            ArrayList<JSONObject> db = new ArrayList<>();
            db.add(new JSONObject().put("mail", "alumno@alumno").put("password", "alumno").put("tipo", "alumno").put("nombre", "carlos javier").put("apellido", "perez gonzalez").put("fNac", "15/10/1990").put("domicilio", "Beruti 1254").put("telefono", "4215-9547"));
            db.add(new JSONObject().put("mail", "profesor@profesor").put("password", "profesor").put("tipo", "profesor").put("nombre", "cacho").put("apellido", "riquelme").put("fNac", "06/03/1989").put("domicilio", "Austria 3561").put("telefono", "4215-9547"));

            int auxMail = -1;
            int auxPass = -1;
            for (int i = 0; i < db.size(); i++) {
                if (jsonTemp.get("mail").equals(db.get(i).get("mail"))) {
                    auxMail = i;
                    if (jsonTemp.get("password").equals(db.get(i).get("password"))) {
                        auxPass = i;
                        break;
                    }
                }
            }
            if (auxMail != -1 && auxPass != -1)
                return db.get(auxMail).put("errorId", "200").put("idSesion", "123");
            if (auxPass == -1) jsonResp.put("errorId", "777");
            if (auxMail == -1) jsonResp.put("errorId", "680");
            return jsonResp;
        }

        if(request.getTaskId() == RequestTaskIds.FICHADA_ALUMNO) {
            JSONArray jsonResp = new JSONArray();
            for(int i=0;i<20;i++){
                JSONObject temp = new JSONObject();
                temp.put("fecha",i+1+"/10/2017");
                if(i%2 == 0) temp.put("presente","SI");
                else temp.put("presente","NO");
                jsonResp.put(temp);
            }
            return jsonResp;
        }

        return null;
    }
}
