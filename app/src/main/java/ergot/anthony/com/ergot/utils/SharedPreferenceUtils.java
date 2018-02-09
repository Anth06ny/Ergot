package ergot.anthony.com.ergot.utils;

import android.content.SharedPreferences;

import ergot.anthony.com.ergot.MyApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Anthony on 29/06/2017.
 */

public class SharedPreferenceUtils {

    private static SharedPreferences getSharedPreference() {
        return MyApplication.getMyApplication().getSharedPreferences("Register", MODE_PRIVATE);
    }

    /* ---------------------------------
    //Sauvegarde TOKEN
    // -------------------------------- */
    private static final String USER_EMAIL_KEY = "USER_EMAIL_KEY";

    public static String getSaveEmail() {
        return getSharedPreference().getString(USER_EMAIL_KEY, "");
    }

    public static void saveEmail(String token) {
        getSharedPreference().edit().putString(USER_EMAIL_KEY, token).apply();
    }
}
