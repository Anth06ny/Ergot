package ergot.anthony.com.ergot;

import android.app.Application;

import com.squareup.otto.Bus;

import ergot.anthony.com.ergot.model.bean.CommandeBean;

/**
 * Created by Anthony on 25/10/2017.
 */

public class MyApplication extends Application {

    /***
     * AFaire
     *
     *
     * Trouver comment faire en sorte que le push fonctionne même en bg
     * Gerer le clic sur la notification retourne bien MainActivity(plusieurs clcik ) et qu'il efface bien la stack trace ancienne
     */

    private static CommandeBean commandeBean;
    private static MyApplication myApplication;
    private static boolean debugMode;
    private static boolean adminMode;
    private static Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;
        newCommande();
        bus = new Bus();

        debugMode = getResources().getBoolean(R.bool.debug);
        adminMode = getResources().getBoolean(R.bool.adminMode);
    }

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
}
