package ergot.anthony.com.ergot;

import android.app.Application;
import android.util.Log;

import com.squareup.otto.Bus;

import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.transverse.utils.SharedPreferenceUtils;

/**
 * Created by Anthony on 25/10/2017.
 */

public class MyApplication extends Application {

    /***
     * AFaire
     * Mettre la permission du demande de numéro sur la page du panier pour expliquer
     * Dans pAnier Activity le numéro de téléphone n'est pas le bon
     * -Remettre CrashLytics
     *
     */

    private static CommandeBean commandeBean;
    private static MyApplication myApplication;
    private static boolean debugMode;
    private static boolean adminMode;
    private static Bus bus;

    public static boolean isAdminMode() {
        return adminMode;
    }

    public static void newCommande() {
        commandeBean = new CommandeBean();
        //par defaut on met la date de la commande à -1
        commandeBean.setDateCommande(-1);
    }

    public static Bus getBus() {
        return bus;
    }

    public static CommandeBean getCommandeBean() {
        return commandeBean;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Fabric.with(this, new Crashlytics());

        myApplication = this;
        newCommande();
        bus = new Bus();
        Log.w("TAG_FIREBASE", "firebaseToken=" + SharedPreferenceUtils.getFireBaseToken());

        debugMode = BuildConfig.DEBUG;
        adminMode = getResources().getBoolean(R.bool.adminMode);
    }
}
