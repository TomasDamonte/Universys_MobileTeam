package universis.universys;


import org.json.JSONObject;

import java.util.HashMap;

public class JSONBuilder {

    public static final String APIVER = "1.0";
    private HashMap<String,String> datos;

    public JSONBuilder() {
        datos = new HashMap<>();
        datos.put("apiVer",APIVER);
        datos.put("idSesion",LoginActivity.ID_SESION);
    }

    public JSONObject logIn() {
        datos.put("mail",LoginActivity.EMAIL);
        datos.put("password",LoginActivity.PASSWORD);
        return new JSONObject(datos);
    }

    public JSONObject consultaDatosPersonales() {
        datos.put("mail",LoginActivity.EMAIL);
        datos.put("password",LoginActivity.PASSWORD);
        return new JSONObject(datos);
    }

    public JSONObject fichadaAlumno(String catedra, String carrera, String materia) {
        datos.put("mail",LoginActivity.EMAIL);
        datos.put("catedra",catedra);
        datos.put("carrera",carrera);
        datos.put("materia",materia);
        return new JSONObject(datos);
    }

}
