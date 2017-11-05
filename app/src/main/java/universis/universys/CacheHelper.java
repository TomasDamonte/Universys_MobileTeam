package universis.universys;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Esta clase se encarga del manejo de Cache. *
 * Se utiliza la clase SharedPreferences la cual tiene un formato [Clave,Valor].
 * Se guardan todas las respuestas del servidor para su uso en caso de que no haya conexión a internet o no responda el servidor.
 * Como [Clave] se asigna el id de la request concatenado con el body de la request.
 * @CONTEXT Contexto ejecutandose actualmente, se actualiza en el onCreate de cada Activity.
 * @CACHE_CONFIGURATION_NAME Nombre de la Cache.
 */
public class CacheHelper {

    private static final String CACHE_CONFIGURATION_NAME = "cache";
    public static Context CONTEXT;

    /**
     * Lee un dato de la cache.
     * Si no hay datos guardados con la clave recibida devuelve un mensaje de error.
     * @param clave Clave del dato.
     * @return Valor del dato.
     */
    public static String leer(String clave) {
        SharedPreferences sharedPreferences = CONTEXT.getSharedPreferences(CACHE_CONFIGURATION_NAME,Activity.MODE_PRIVATE);
        String res = null;
        if(!sharedPreferences.contains(clave.toString())) {
            try {
                return new JSONObject().put(Error.ERROR_ID, Error.CACHE_ERROR).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (sharedPreferences != null) {
            res = sharedPreferences.getString(clave.toString(), null);
        }
        return res;
    }

    /**
     * Guarda en cache un dato.
     * @param clave Clave del dato.
     * @param valor Valor del dato.
     */
    public static void guardar(String clave, String valor) {
        SharedPreferences sharedPreferences = CONTEXT.getSharedPreferences(CACHE_CONFIGURATION_NAME, Activity.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(clave.toString(), valor);
            editor.commit();
        }
    }

}

