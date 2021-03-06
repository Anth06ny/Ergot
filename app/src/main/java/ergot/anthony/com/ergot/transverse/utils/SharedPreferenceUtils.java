package ergot.anthony.com.ergot.transverse.utils;

import android.content.SharedPreferences;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

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
    //Sauvegarde EMAIL
    // -------------------------------- */
    private static final String USER_EMAIL_KEY = "USER_EMAIL_KEY";

    public static String getSaveEmail() {
        return getSharedPreference().getString(USER_EMAIL_KEY, "");
    }

    public static void saveEmail(String token) {
        getSharedPreference().edit().putString(USER_EMAIL_KEY, token).apply();
    }

    /* ---------------------------------
   //Gestion Token   : Token du téléphone
   // -------------------------------- */
    private static final String TOKEN_KEY = "TOKEN_KEY";

    /**
     * retourne un token unique et toujours le même sauf si réistlation de l'appli
     *
     * @return
     */
    public static String getUniqueToken() {

        String uniqueToken = getSharedPreference().getString(TOKEN_KEY, "");
        if (StringUtils.isBlank(uniqueToken)) {
            uniqueToken = UUID.randomUUID().toString();
            getSharedPreference().edit().putString(TOKEN_KEY, uniqueToken).apply();
        }

        return uniqueToken;
    }

    /* ---------------------------------
  //Gestion FireBAseToken
  // -------------------------------- */
    private static final String FIREBASE_TOKEN_KEY = "FIREBASE_TOKEN_KEY";

    public static String getFireBaseToken() {
        return getSharedPreference().getString(FIREBASE_TOKEN_KEY, "");
    }

    public static void saveFireBaseToken(String token) {
        getSharedPreference().edit().putString(FIREBASE_TOKEN_KEY, token).apply();
    }
}
