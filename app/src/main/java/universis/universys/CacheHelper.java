package universis.universys;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CacheHelper {
    public static Context context;

    public static String getStringProperty(Integer key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        String res = null;
        if (sharedPreferences != null) {
            res = sharedPreferences.getString(key.toString(), null);
        }
        return res;
    }

    public static void setStringProperty(Integer key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key.toString(), value);
            editor.commit();
        }
    }

}

