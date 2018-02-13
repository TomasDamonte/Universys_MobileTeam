package universis.universys;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Esta clase simula la respuesta del servidor.
 */
public class DataBase {
    /**
     * Analiza la request recibida y dependiendo de su id y contenido devuelve la respuesta correspondiente.
     * @param request Request recibida por el servidor.
     * @return Respuesta del servidor.
     * @throws JSONException Por si ocurre algún error.
     */
    public static JSONObject respuestaDB (CHTTPRequest request) throws JSONException {
        JSONObject jsonReq = request.getBody();
        JSONArray db = new JSONArray();
        try{
            db = new JSONArray(CacheHelper.leer("db"));
        }catch (Exception e) {
            db = new JSONArray().put(new JSONObject().put("mail", "alumno@alumno").put("password", "alumno")
                    .put("rol", "alumno").put("nombre", "carlos javier")
                    .put("apellido", "perez gonzalez").put("fNac", "15/10/1990")
                    .put("domicilio", "Beruti 1254").put("telefono", "4215-9547")
                    .put("idSesion", "7541"));
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
            db.getJSONObject(0).put("eventos",eventos);
            db.put(new JSONObject().put("mail", "profesor@profesor").put("password", "profesor")
                    .put("rol", "profesor").put("nombre", "cacho").put("apellido", "riquelme")
                    .put("fNac", "06/03/1989").put("domicilio", "Austria 3561")
                    .put("telefono", "4215-9547").put("idSesion", "6320"));
            //Declaracion nuevo objeto JSON admin. ------------------------------------- FEDE
            db.put(new JSONObject().put("mail", "admin@admin").put("password", "admin")
                    .put("rol", "admin").put("nombre", "pepe").put("apellido", "riquelme")
                    .put("fNac", "06/03/1989").put("domicilio", "Austria 3561")
                    .put("telefono", "4215-9547").put("idSesion", "6321"));


            db.put(new JSONObject().put("mail", "profesor1@profesor1").put("password", "profesor1")
                    .put("rol", "profesor").put("nombre", "rere").put("apellido", "riquelme")
                    .put("fNac", "06/03/1989").put("domicilio", "Austria 3561")
                    .put("telefono", "4215-9547").put("idSesion", "6329"));
            db.put(new JSONObject().put("mail", "alumno1@alumno1").put("password", "alumno1")
                    .put("rol", "alumno").put("nombre", " ")
                    .put("apellido", " ").put("fNac", " ")
                    .put("domicilio", " ").put("telefono", " ")
                    .put("idSesion", "7542"));
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
            db.getJSONObject(0).put("eventos",eventos);

            CacheHelper.guardar("db",db.toString());
        }

        if(request.getTaskId() == RequestTaskIds.LOGIN) {
            JSONObject jsonResp = new JSONObject();
            int auxMail = -1;
            int auxPass = -1;
            for (int i = 0; i < db.length(); i++) {
                if (jsonReq.get("mail").equals(db.getJSONObject(i).getString("mail"))) {
                    auxMail = i;
                    if (jsonReq.get("password").equals(db.getJSONObject(i).getString("password"))) {
                        auxPass = i;
                        break;
                    }
                }
            }
            if (auxMail != -1 && auxPass != -1)
                return db.getJSONObject(auxMail).put(Error.ERROR_ID, Error.SUCCESS);
            if (auxPass == -1) jsonResp.put(Error.ERROR_ID, Error.PASSWORD_ERROR);
            if (auxMail == -1) jsonResp.put(Error.ERROR_ID, Error.EMAIL_ERROR);
            return jsonResp;
        }
        else if(request.getTaskId() == RequestTaskIds.CALENDARIO_ALUMNO) {
            return db.getJSONObject(0).put(Error.ERROR_ID,Error.SUCCESS);
        }
        else if (request.getTaskId() == RequestTaskIds.DATOS_PERSONALES) {
            int aux = -1;
            for (int i = 0; i < db.length(); i++) {
                if(jsonReq.get("idSesion").equals(db.getJSONObject(i).getString("idSesion"))) {
                    aux = i;
                    break;
                }
            }
            return db.getJSONObject(aux).put(Error.ERROR_ID, Error.SUCCESS);
        }
        else if (request.getTaskId() == RequestTaskIds.MODIFICAR_DATOS_PERSONALES) {
            int aux = -1;
            for (int i = 0; i < db.length(); i++) {
                if(jsonReq.getString("idSesion").equals(db.getJSONObject(i).getString("idSesion"))) {
                    aux = i;
                    break;
                }
            }
            db.getJSONObject(aux).put("nombre",jsonReq.getString("nombre"));
            db.getJSONObject(aux).put("apellido",jsonReq.getString("apellido"));
            db.getJSONObject(aux).put("mail",jsonReq.getString("mail"));
            db.getJSONObject(aux).put("fNac",jsonReq.getString("fNac"));
            db.getJSONObject(aux).put("domicilio",jsonReq.getString("domicilio"));
            db.getJSONObject(aux).put("telefono",jsonReq.getString("telefono"));
            CacheHelper.guardar("db",db.toString());
            return db.getJSONObject(aux).put(Error.ERROR_ID, Error.SUCCESS);
        }
        else if(request.getTaskId() == RequestTaskIds.VER_SOLICITUDES) {
            JSONObject dbResp = new JSONObject().put(Error.ERROR_ID,Error.SUCCESS);
            JSONArray solicitudes = new JSONArray().put(new JSONObject().put("idSolicitud","214").put("catedra","Didier Renard").put("carrera","Ingenieria en Sistemas").put("materia","Testing").put("alumno","Juan Perez"))
                    .put(new JSONObject().put("idSolicitud","591").put("catedra","Pandolfo").put("carrera","Ingenieria en Sistemas").put("materia","Programación III").put("alumno","Juan Perez"));
            dbResp.put("solicitudes",solicitudes);
            return dbResp;
        }
        else if(request.getTaskId() == RequestTaskIds.NOTA_ALUMNO || request.getTaskId() == RequestTaskIds.BAJA_MATERIA || request.getTaskId() == RequestTaskIds.CARGAR_NOTAS) {
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
        else if(request.getTaskId() == RequestTaskIds.FICHADA_ALUMNO) {
            JSONArray fichadas = new JSONArray();
            JSONObject db2 = new JSONObject().put("catedra","a").put("carrera","b").put("materia","c");
            JSONObject jsonResp = new JSONObject();
            if(jsonReq.getString("catedra").equals(db2.getString("catedra")) && jsonReq.getString("carrera").equals(db2.getString("carrera")) && jsonReq.getString("materia").equals(db2.getString("materia"))) {
                for (int i = 0; i < 20; i++) {
                    JSONObject temp = new JSONObject();
                    temp.put("fecha", i + 1 + "/10/2017");
                    if (i % 2 == 0) temp.put("presente", "SI");
                    else temp.put("presente", "NO");
                    fichadas.put(temp);
                }
                jsonResp.put(Error.ERROR_ID,Error.SUCCESS);
                jsonResp.put("fichadas",fichadas);
            } else {
                if(!jsonReq.getString("catedra").equals(db2.getString("catedra")))
                    jsonResp.put(Error.ERROR_ID,Error.CATEDRA_ERROR);
                if(!jsonReq.getString("carrera").equals(db2.getString("carrera")))
                    jsonResp.put(Error.ERROR_ID,Error.CARRERA_ERROR);
                if(!jsonReq.getString("materia").equals(db2.getString("materia")))
                    jsonResp.put(Error.ERROR_ID,Error.MATERIA_ERROR);
            }
            return jsonResp;
        }
        else if(request.getTaskId() == RequestTaskIds.HORARIO_ALUMNO) {
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
        else if(request.getTaskId() == RequestTaskIds.NOTAS_PROFESOR) {
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
        else if(request.getTaskId() == RequestTaskIds.VER_ASISTENCIAS) {
            JSONArray jsonArray = new JSONArray();
            for(int i=1;i<=10;i++) {
                JSONObject alumno = new JSONObject().put("nombre", "Juan Pérez").put("asistencias", (i+10)+"%");
                jsonArray.put(alumno);
            }
            JSONObject db2 = new JSONObject().put("asistencias",jsonArray);
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
        else if(request.getTaskId() == RequestTaskIds.ACEPTAR_SOLICITUDES ||request.getTaskId() == RequestTaskIds.INSCRIPCION_MATERIA) {
            return new JSONObject().put(Error.ERROR_ID,Error.SUCCESS);
        }
        else if(request.getTaskId() == RequestTaskIds.MATERIAS_DISPONIBLES) {
            JSONArray horario = new JSONArray().put(0,"4C1;30D").put(1,"13B;2F7");
            JSONObject cursadas = new JSONObject().put("idCursada","123").put("horario",horario);
            JSONObject catedras = new JSONObject().put("catedra","didier").put("cursadas",new JSONArray().put(cursadas));
            JSONObject catedras2 = new JSONObject().put("nombre","matematica").put("catedras",new JSONArray().put(catedras));
            JSONObject disponibles = new JSONObject().put(Error.ERROR_ID,Error.SUCCESS).put("inscripcionesDisponibles",new JSONArray().put(catedras2));
            return disponibles;
        }


        return null;
    }
}