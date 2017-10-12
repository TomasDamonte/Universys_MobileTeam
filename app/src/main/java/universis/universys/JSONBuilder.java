package universis.universys;


import org.json.JSONObject;

import java.util.HashMap;

public class JSONBuilder {

    public static final String APIVER = "1.0";
    private HashMap<String,String> datos;

    public JSONBuilder() {
        datos = new HashMap<>();
        datos.put("apiVer",APIVER);
        datos.put("idSesion",LoginActivity.idSesion);
    }

    public JSONObject logIn() {
        datos.put("mail",LoginActivity.EMAIL);
        datos.put("password",LoginActivity.PASSWORD);
        return new JSONObject(datos);
    }

    public JSONObject consDatosAlumno() {
        datos.put("mail",LoginActivity.EMAIL);
        datos.put("password",LoginActivity.PASSWORD);
        return new JSONObject(datos);
    }


}
