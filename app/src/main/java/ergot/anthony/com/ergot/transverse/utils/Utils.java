package ergot.anthony.com.ergot.transverse.utils;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import ergot.anthony.com.ergot.MyApplication;

/**
 * Created by Anthony on 24/10/2017.
 */

public class Utils {

    public static String longToStringPrice(long price) {
        long euro = price / 100;
        long ct = price % 100;

        String ctString = ct < 10 ? "0" + ct : "" + ct;

        return euro + "." + ctString + " €";
    }

    public static void vibrate() {
        Vibrator v = (Vibrator) MyApplication.getMyApplication().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(800);
    }

    public static void sound() {

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(MyApplication.getMyApplication(), notification);
            r.play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
