package universis.universys;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class DataBase {

    public static Object respuestaDB (CHTTPRequest request) throws JSONException {
        JSONObject jsonReq = request.getBody();
        ArrayList<JSONObject> db = new ArrayList<>();
        db.add(new JSONObject().put("mail", "alumno@alumno").put("password", "alumno").put("rol", "alumno").put("nombre", "carlos javier").put("apellido", "perez gonzalez").put("fNac", "15/10/1990").put("domicilio", "Beruti 1254").put("telefono", "4215-9547").put("idSesion", "7541"));
        db.add(new JSONObject().put("mail", "profesor@profesor").put("password", "profesor").put("rol", "profesor").put("nombre", "cacho").put("apellido", "riquelme").put("fNac", "06/03/1989").put("domicilio", "Austria 3561").put("telefono", "4215-9547").put("idSesion", "6320"));

        if(request.getTaskId() == RequestTaskIds.LOGIN) {
            JSONObject jsonResp = new JSONObject();
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
                return db.get(auxMail).put(Error.ERROR_ID, Error.SUCCESS);
            if (auxPass == -1) jsonResp.put(Error.ERROR_ID, Error.PASSWORD_ERROR);
            if (auxMail == -1) jsonResp.put(Error.ERROR_ID, Error.EMAIL_ERROR);
            return jsonResp;
        }

        if(request.getTaskId() == RequestTaskIds.DATOS_PERSONALES) {
            int aux = -1;
            for (int i = 0; i < db.size(); i++) {
                if(jsonReq.get("idSesion").equals(db.get(i).get("idSesion"))) {
                    aux = i;
                    break;
                }
            }
            return db.get(aux).put(Error.ERROR_ID, Error.SUCCESS);
        }

        if(request.getTaskId() == RequestTaskIds.NOTA_ALUMNO) {
            JSONObject db2 = new JSONObject().put("catedra","a").put("carrera","b").put("materia","c").put("nota","9");

            if(jsonReq.getString("catedra").equals(db2.getString("catedra")) && jsonReq.getString("carrera").equals(db2.getString("carrera")) && jsonReq.getString("materia").equals(db2.getString("materia")))
                return db2.put(Error.ERROR_ID,Error.SUCCESS);
            else {
                    if(!jsonReq.getString("catedra").equals(db2.getString("catedra")))
                        return new JSONObject().put(Error.ERROR_ID,Error.CATEDRA_ERROR);
                    if(!jsonReq.getString("carrera").equals(db2.getString("carrera")))
                        return new JSONObject().put(Error.ERROR_ID,Error.CARRERA_ERROR);
                    if(!jsonReq.getString("materia").equals(db2.getString("materia")))
                        return new JSONObject().put(Error.ERROR_ID,Error.MATERIA_ERROR);
                }
        }

        if(request.getTaskId() == RequestTaskIds.FICHADA_ALUMNO) {
            JSONArray jsonResp = new JSONArray();
            JSONObject db2 = new JSONObject().put("catedra","a").put("carrera","b").put("materia","c");

            if(jsonReq.getString("catedra").equals(db2.getString("catedra")) && jsonReq.getString("carrera").equals(db2.getString("carrera")) && jsonReq.getString("materia").equals(db2.getString("materia"))) {
                jsonResp.put(new JSONObject().put(Error.ERROR_ID,Error.SUCCESS));
                for (int i = 0; i < 20; i++) {
                    JSONObject temp = new JSONObject();
                    temp.put("fecha", i + 1 + "/10/2017");
                    if (i % 2 == 0) temp.put("presente", "SI");
                    else temp.put("presente", "NO");
                    jsonResp.put(temp);
                }
            } else {
                if(!jsonReq.getString("catedra").equals(db2.getString("catedra")))
                    jsonResp.put(new JSONObject().put(Error.ERROR_ID,Error.CATEDRA_ERROR));
                if(!jsonReq.getString("carrera").equals(db2.getString("carrera")))
                    jsonResp.put(new JSONObject().put(Error.ERROR_ID,Error.CARRERA_ERROR));
                if(!jsonReq.getString("materia").equals(db2.getString("materia")))
                    jsonResp.put(new JSONObject().put(Error.ERROR_ID,Error.MATERIA_ERROR));
            }
            return jsonResp;
        }

        if(request.getTaskId() == RequestTaskIds.HORARIO_ALUMNO) {
            JSONArray horario = new JSONArray().put(0,"4C1;30D").put(1,"13B;2F7");
            JSONObject db2 = new JSONObject().put("catedra","a").put("carrera","b").put("materia","c").put("horario",horario);

            if(jsonReq.getString("catedra").equals(db2.getString("catedra")) && jsonReq.getString("carrera").equals(db2.getString("carrera")) && jsonReq.getString("materia").equals(db2.getString("materia")))
                return db2.put(Error.ERROR_ID,Error.SUCCESS);
            else {
                if(!jsonReq.getString("catedra").equals(db2.getString("catedra")))
                    return new JSONObject().put(Error.ERROR_ID,Error.CATEDRA_ERROR);
                if(!jsonReq.getString("carrera").equals(db2.getString("carrera")))
                    return new JSONObject().put(Error.ERROR_ID,Error.CARRERA_ERROR);
                if(!jsonReq.getString("materia").equals(db2.getString("materia")))
                    return new JSONObject().put(Error.ERROR_ID,Error.MATERIA_ERROR);
            }
        }

        if(request.getTaskId() == RequestTaskIds.CALENDARIO_ALUMNO) {
            JSONObject evento = new JSONObject().put("evento","final proyecto 1").put("fecha","2017/12/01");
            JSONArray eventos = new JSONArray().put(evento);
            evento = new JSONObject().put("evento","final proyecto 2").put("fecha","2017/12/15");
            eventos.put(evento);
            evento = new JSONObject().put("evento","final BD 1").put("fecha","2017/12/05");
            eventos.put(evento);
            evento = new JSONObject().put("evento","final BD 2").put("fecha","2017/12/26");
            eventos.put(evento);
            evento = new JSONObject().put("evento","final WEB 1").put("fecha","2017/12/06");
            eventos.put(evento);
            evento = new JSONObject().put("evento","final WEB 2").put("fecha","2017/12/27");
            eventos.put(evento);
            evento = new JSONObject().put("evento","final seguridad informatica 1").put("fecha","2017/12/07");
            eventos.put(evento);
            evento = new JSONObject().put("evento","final seguridad informatica 2").put("fecha","2017/12/21");
            eventos.put(evento);
            evento = new JSONObject().put("evento","entrega 60hs/300hs").put("fecha","2017/12/21");
            eventos.put(evento);
            return new JSONObject().put(Error.ERROR_ID,Error.SUCCESS).put("eventos",eventos);
        }

        if(request.getTaskId() == RequestTaskIds.NOTAS_PROFESOR) {
            JSONArray jsonArray = new JSONArray();
            for(int i=1;i<=10;i++) {
                JSONObject alumno = new JSONObject().put("nombre", "Juan Pérez").put("nota", i+"");
                jsonArray.put(alumno);
            }
            JSONObject db2 = new JSONObject().put("alumnos",jsonArray);
            JSONObject catedras = new JSONObject().put("catedra","a").put("carrera","b").put("materia","c");

            if(jsonReq.getString("catedra").equals(catedras.getString("catedra")) && jsonReq.getString("carrera").equals(catedras.getString("carrera")) && jsonReq.getString("materia").equals(catedras.getString("materia")))
                return db2.put(Error.ERROR_ID,Error.SUCCESS);
            else {
                if(!jsonReq.getString("catedra").equals(catedras.getString("catedra")))
                    return new JSONObject().put(Error.ERROR_ID,Error.CATEDRA_ERROR);
                if(!jsonReq.getString("carrera").equals(catedras.getString("carrera")))
                    return new JSONObject().put(Error.ERROR_ID,Error.CARRERA_ERROR);
                if(!jsonReq.getString("materia").equals(catedras.getString("materia")))
                    return new JSONObject().put(Error.ERROR_ID,Error.MATERIA_ERROR);
            }
        }
        return null;
    }
}
