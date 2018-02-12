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
     * -Vcorriger json recu en retour de l'envoie de commande. Et tester l'historique
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
