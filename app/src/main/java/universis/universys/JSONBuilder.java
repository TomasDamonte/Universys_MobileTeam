package universis.universys;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class JSONBuilder {

    public static final String APIVER = "1.0";
    private LinkedHashMap<String,String> datos;

    public JSONBuilder() {
        datos = new LinkedHashMap<>();
        datos.put("apiVer",APIVER);
        if (LoginActivity.ID_SESION != null)
            datos.put("idSesion",LoginActivity.ID_SESION);
        else
            datos.put("idSesion","000");
    }

    public JSONObject logIn(String email, String password) {
        datos.put("mail",email);
        datos.put("password",password);
        return new JSONObject(datos);
    }

    public JSONObject requestBasico() {
        return new JSONObject(datos);
    }

    public JSONObject requestGenerico(String catedra, String carrera, String materia) {
        datos.put("catedra",catedra);
        datos.put("carrera",carrera);
        datos.put("materia",materia);
        return new JSONObject(datos);
    }

    public JSONObject requestGenerico(String catedra, String carrera, String materia, String alumno, String nota) {
        datos.put("catedra",catedra);
        datos.put("carrera",carrera);
        datos.put("materia",materia);
        datos.put("alumno",alumno);
        datos.put("nota",nota);
        return new JSONObject(datos);
    }

    public JSONObject enviarSolicitudesInscripcion(LinkedHashMap<String,String> solicitudes) {
        JSONObject body = new JSONObject(datos);
        try {
            body.put("solicitudes",solicitudes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return body;
    }

    public  JSONObject modificarDatosPersonales(String[] datosPersonales) {
        datos.put("nombre",datosPersonales[0]);
        datos.put("apellido",datosPersonales[1]);
        datos.put("domicilio",datosPersonales[2]);
        datos.put("mail",datosPersonales[3]);
        datos.put("fNac",datosPersonales[4]);
        datos.put("telefono",datosPersonales[5]);
        return  new JSONObject(datos);
    }

    public JSONObject inscripcionAMateria(String idCursada) {
        datos.put("idCursada",idCursada);
        return new JSONObject(datos);
    }

}
