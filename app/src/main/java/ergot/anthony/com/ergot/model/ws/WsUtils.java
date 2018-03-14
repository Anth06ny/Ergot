package ergot.anthony.com.ergot.model.ws;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.exception.TechnicalException;
import ergot.anthony.com.ergot.model.bean.CategoryBean;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.model.bean.UserBean;
import ergot.anthony.com.ergot.utils.Logger;
import ergot.anthony.com.ergot.utils.SharedPreferenceUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static ergot.anthony.com.ergot.model.ws.WSUtilsAdmin.ADMIN_PASS;

/**
 * Created by Anthony on 04/05/2017.
 */

public class WsUtils {

    static final String PING_GOOGLE = "http://www.google.fr";

    static final String URL_SERVEUR = MyApplication.getMyApplication().getString(R.string.url_server);// "http://192.168.20.10:8000/";
    private static final String URL_GET_CATALOGUE = URL_SERVEUR + "getCatalogue";
    private static final String URL_SEND_COMMAND = URL_SERVEUR + "setCommande";
    private static final String URL_GET_HISTORY = URL_SERVEUR + "getHistorique";
    private static final String URL_CANCEL_COMMAND = URL_SERVEUR + "cancelCommande";

    public static final Gson gson = new Gson();

    public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /* ---------------------------------
    // GET
    // -------------------------------- */

    /**
     * Récupère le catalogue des produits
     *
     * @return
     * @throws TechnicalException
     */
    public static ArrayList<CategoryBean> getCatalogue() throws TechnicalException {

        Log.w("TAG_URL", URL_GET_CATALOGUE);

        //Création de la requete
        Request request = new Request.Builder().url(URL_GET_CATALOGUE).build();

        //Execution de la requête
        Response response = null;
        try {
            response = getOkHttpClient().newCall(request).execute();
        }
        catch (IOException e) {
            //On test si google répond pour différencier si c'est internet ou le serveur le probleme
            throw testInternetConnexionOnGoogle(e);
        }

        //Analyse du code retour
        if (response.code() < HttpURLConnection.HTTP_OK || response.code() >= HttpURLConnection.HTTP_MULT_CHOICE) {
            throw new TechnicalException("Réponse du serveur incorrect : " + response.code() + "\nErreur:" + response.message(), R.string.generic_error);
        }
        else {

            try {
                ArrayList<CategoryBean> catalogue;
                if (MyApplication.isDebugMode()) {
                    //Résultat de la requete.

                    String jsonRecu = response.body().string();
                    Logger.logJson("TAG_JSON_RECU", jsonRecu);
                    catalogue = gson.fromJson(jsonRecu, new TypeToken<ArrayList<CategoryBean>>() {
                    }.getType());
                }
                else {
                    //JSON -> Java (Parser une ArrayList typée)
                    catalogue = gson.fromJson(new InputStreamReader(response.body().byteStream()),
                            new TypeToken<ArrayList<CategoryBean>>() {
                            }.getType());
                }
                return catalogue;
            }
            catch (Exception e) {
                throw new TechnicalException(e, "Erreur lors du parsing de la requete");
            }
        }
    }

    /**
     * Récupère la liste des commande de l'utilisateur.. Envoie email et token du téléphone
     *
     * @return
     * @throws TechnicalException
     */
    public static ArrayList<CommandeBean> getHistory() throws TechnicalException {

        UserBean userBean = new UserBean();
        userBean.setEmail(SharedPreferenceUtils.getSaveEmail());
        userBean.setToken(SharedPreferenceUtils.getUniqueToken());
        userBean.setFirebaseToken(SharedPreferenceUtils.getFireBaseToken());

        String json = gson.toJson(userBean);
        Log.w("TAG_URL", URL_GET_HISTORY);
        Logger.logJson("TAG_JSON_ENVOYER", json);
        //Création de la requete
        Request request = new Request.Builder().url(URL_GET_HISTORY).header(ADMIN_PASS, MyApplication.getMyApplication().getResources().getString(R.string.adminPass)).post(RequestBody.create(JSON, json)).build();

        //Execution de la requête
        Response response = null;
        try {
            response = getOkHttpClient().newCall(request).execute();
        }
        catch (IOException e) {
            //On test si google répond pour différencier si c'est internet ou le serveur le probleme
            throw testInternetConnexionOnGoogle(e);
        }

        //Analyse du code retour si non copmris entre 200 et 299
        if (response.code() < HttpURLConnection.HTTP_OK || response.code() >= HttpURLConnection.HTTP_MULT_CHOICE) {
            throw new TechnicalException("Réponse du serveur incorrect : " + response.code() + "\nErreur:" + response.message(), R.string.generic_error);
        }
        else {

            ArrayList<CommandeBean> historique;

            if (MyApplication.isDebugMode()) {
                //Résultat de la requete.
                try {
                    String jsonRecu = response.body().string();
                    Logger.logJson("TAG_JSON_RECU", jsonRecu);
                    historique = gson.fromJson(jsonRecu, new TypeToken<ArrayList<CommandeBean>>() {
                    }.getType());
                }
                catch (Exception e) {
                    throw new TechnicalException(e, "Erreur lors du .string dans la requete");
                }
            }
            else {
                //JSON -> Java (Parser une ArrayList typée)
                historique = gson.fromJson(new InputStreamReader(response.body().byteStream()),
                        new TypeToken<ArrayList<CommandeBean>>() {
                        }.getType());
            }

            return historique;
        }
    }





    /* ---------------------------------
    // SEND
    // -------------------------------- */

    /**
     * Envoyer une commande   , recoit la liste des commandes de l'utilisateur
     *
     * @param commandeBean
     * @return l'historique des commandes
     */
    public static ArrayList<CommandeBean> sendCommande(CommandeBean commandeBean) throws TechnicalException {

        //ON ajoute le token du téléphone
        commandeBean.getUser().setToken(SharedPreferenceUtils.getUniqueToken());

        String json = gson.toJson(commandeBean);
        Log.w("TAG_REQ", URL_SEND_COMMAND);
        Logger.logJson("TAG_JSON_ENVOYER", json);
        //Création de la requete

        Request request = new Request.Builder().url(URL_SEND_COMMAND).post(RequestBody.create(JSON, json)).build();

        //Execution de la requête
        Response response = null;
        try {
            response = getOkHttpClient().newCall(request).execute();
        }
        catch (IOException e) {
            //On test si google répond pour différencier si c'est internet ou le serveur le probleme
            throw testInternetConnexionOnGoogle(e);
        }

        //Analyse du code retour si non copmris entre 200 et 299
        if (response.code() < HttpURLConnection.HTTP_OK || response.code() >= HttpURLConnection.HTTP_MULT_CHOICE) {
            throw new TechnicalException("Réponse du serveur incorrect : " + response.code() + "\nErreur:" + response.message(), R.string
                    .generic_error);
        }
        else {
            try {
                ArrayList<CommandeBean> historique;
                if (MyApplication.isDebugMode()) {
                    //Résultat de la requete.

                    String jsonRecu = response.body().string();
                    Logger.logJson("TAG_JSON_RECU", json);
                    historique = gson.fromJson(jsonRecu, new TypeToken<ArrayList<CommandeBean>>() {
                    }.getType());
                }
                else {
                    //JSON -> Java (Parser une ArrayList typée)
                    historique = gson.fromJson(new InputStreamReader(response.body().byteStream()),
                            new TypeToken<ArrayList<CommandeBean>>() {
                            }.getType());
                }
                return historique;
            }
            catch (Exception e) {
                e.printStackTrace();
                //La requete s'est bien passé mais on a une erreur sur le parsing. On ne fait pas planter l'envoie pour autant
                return new ArrayList<>();
            }
        }
    }






    /* ---------------------------------
    // private
    // -------------------------------- */

    static TechnicalException testInternetConnexionOnGoogle(IOException e) {
        //On test si google répond pour différencier si c'est internet ou le serveur le probleme
        Request request = request = new Request.Builder().url(PING_GOOGLE).build();
        try {

            new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS).build().newCall(request).execute();
            //Ca marche -> C'est le serveur le probleme
            return new TechnicalException("Le serveur ne répond pas.", e, R.string.server_error);
        }
        catch (IOException e1) {
            //Ca crash encore -> problème d'internet
            return new TechnicalException("Le serveur ne répond pas.", e1, R.string.bad_internet_connexion_error);
        }
    }

    static OkHttpClient getOkHttpClient() throws TechnicalException {
        //On test la connexion à un réseau
        if (!isNetworkAvailable()) {
            throw new TechnicalException("Non connecté à un réseau.", R.string.no_internet_error);
        }

        return new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Est ce que le téléphone est relié à un réseau ?
     *
     * @return
     */
    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getMyApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
