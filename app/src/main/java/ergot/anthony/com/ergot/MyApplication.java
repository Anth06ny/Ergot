package ergot.anthony.com.ergot;

import android.app.Application;

import ergot.anthony.com.ergot.model.bean.CommandeBean;

/**
 * Created by Anthony on 25/10/2017.
 */

public class MyApplication extends Application {

    private static CommandeBean commandeBean;

    public static CommandeBean getCommandeBean() {
        return commandeBean;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        commandeBean = new CommandeBean();
    }
}
