package ergot.anthony.com.ergot;

import android.app.Application;

import ergot.anthony.com.ergot.model.bean.CommandeBean;

/**
 * Created by Anthony on 25/10/2017.
 */

public class MyApplication extends Application {

    /***
     * AFaire
     *
     * -Continuer l'historique coté admin. L'xml est fait row_history_admin. Modifier l'adapter pour prendre en comtpe si on est coté admin
     *
     * -
     */

    private static CommandeBean commandeBean;
    private static MyApplication myApplication;
    private static boolean debugMode;

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;
        commandeBean = new CommandeBean();

        debugMode = getResources().getBoolean(R.bool.debug);
    }

    public static void newCommande() {
        commandeBean = new CommandeBean();
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
