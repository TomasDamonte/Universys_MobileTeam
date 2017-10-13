package universis.universys;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;


public class CacheHelper {

    private static final String CACHE_CONFIGURATION_NAME = "cache";
    public static Context CONTEXT;

    public static String getStringProperty(Integer key) {
        SharedPreferences sharedPreferences = CONTEXT.getSharedPreferences(CACHE_CONFIGURATION_NAME,Activity.MODE_PRIVATE);
        String res = null;
        if(!sharedPreferences.contains(key.toString())) {
            try {
                return new JSONObject().put(Error.CACHE_ERROR_NAME, Error.CACHE_ERROR).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (sharedPreferences != null) {
            res = sharedPreferences.getString(key.toString(), null);
        }
        return res;
    }

    public static void setStringProperty(Integer key, String value) {
        SharedPreferences sharedPreferences = CONTEXT.getSharedPreferences(CACHE_CONFIGURATION_NAME, Activity.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key.toString(), value);
            editor.commit();
        }
    }

}

