package universis.universys;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Esta clase construye los JSON que se envían en las request al servidor.
 */
public class JSONBuilder {

    //Versión actual de la API.
    public static final String APIVER = "1.0";
    private LinkedHashMap<String,String> datos;

    /**
     * Constructor de la clase.
     */
    public JSONBuilder() {
        datos = new LinkedHashMap<>();
        datos.put("apiVer",APIVER);
        if (LoginActivity.ID_SESION != null)
            datos.put("idSesion",LoginActivity.ID_SESION);
        else
            datos.put("idSesion","000");
    }

    /**
     * Constructor que se utiliza en el login.
     * @param email String con el email del usuario.
     * @param password String con la contraseña del usuario.
     * @return JSONObject a eviar en el body de la request.
     */
    public JSONObject logIn(String email, String password) {
        datos.put("mail",email);
        datos.put("password",password);
        return new JSONObject(datos);
    }

    /**
     * Devuelve el contenido del constructor de la clase.
     * @return JSONObject a eviar en el body de la request.
     */
    public JSONObject requestBasico() {
        return new JSONObject(datos);
    }

    /**
     * Constructor usado cuando se necesita enviar cátedra,carrera y materia.
     * @param catedra Catedra a enviar.
     * @param carrera Carrera a enviar.
     * @param materia Materia a enviar.
     * @return JSONObject a eviar en el body de la request.
     */
    public JSONObject requestGenerico(String catedra, String carrera, String materia) {
        datos.put("catedra",catedra);
        datos.put("carrera",carrera);
        datos.put("materia",materia);
        return new JSONObject(datos);
    }

    /**
     * Constructor usado cuando se necesita enviar cátedra,carrera, materia, alumno y nota.
     * @param catedra Catedra a enviar.
     * @param carrera Carrera a enviar.
     * @param materia Materia a enviar.
     * @param alumno Alumno a enviar.
     * @param nota Nota a enviar.
     * @return JSONObject a eviar en el body de la request.
     */
    public JSONObject requestGenerico(String catedra, String carrera, String materia, String alumno, String nota) {
        datos.put("catedra",catedra);
        datos.put("carrera",carrera);
        datos.put("materia",materia);
        datos.put("alumno",alumno);
        datos.put("nota",nota);
        return new JSONObject(datos);
    }

    /**
     * Constructor usado cuando el profesor acepta/rechaza solicitudes de inscripcion de alumnos a sus materias.
     * @param solicitudes LinkedHashMap con el id de solicitud y el estado.(idSolicitud,estado) Estado=>"aceptada"/"rechazada"
     * @return JSONObject a eviar en el body de la request.
     */
    public JSONObject enviarSolicitudesInscripcion(LinkedHashMap<String,String> solicitudes) {
        JSONObject body = new JSONObject(datos);
        try {
            body.put("solicitudes",solicitudes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * Constructor usado cuando se envían los datos personales del usuario.
     * @param datosPersonales Vector de String que contiene el valor de cada campo.
     * @return JSONObject a eviar en el body de la request.
     */
    public  JSONObject modificarDatosPersonales(String[] datosPersonales) {
        datos.put("nombre",datosPersonales[0]);
        datos.put("apellido",datosPersonales[1]);
        datos.put("domicilio",datosPersonales[2]);
        datos.put("mail",datosPersonales[3]);
        datos.put("fNac",datosPersonales[4]);
        datos.put("telefono",datosPersonales[5]);
        return  new JSONObject(datos);
    }

    /**
     * Constructor usado cuando se envian los datos de la inscripción a una materia de un alumno.
     * @param idCursada id de la cuarsada a la cual el alumno desea inscribirse.
     * @return JSONObject a eviar en el body de la request.
     */
    public JSONObject inscripcionAMateria(String idCursada) {
        datos.put("idCursada",idCursada);
        return new JSONObject(datos);
    }

}
