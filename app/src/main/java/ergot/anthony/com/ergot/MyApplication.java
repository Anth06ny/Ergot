package ergot.anthony.com.ergot;

import android.app.Application;

import ergot.anthony.com.ergot.model.bean.Commande;

/**
 * Created by Anthony on 25/10/2017.
 */

public class MyApplication extends Application {

    private static Commande commande;

    public static Commande getCommande() {
        return commande;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        commande = new Commande();
    }
}
