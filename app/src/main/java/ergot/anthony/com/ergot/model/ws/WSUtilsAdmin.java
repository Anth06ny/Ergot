package ergot.anthony.com.ergot.model.ws;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.exception.TechnicalException;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.utils.Logger;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static ergot.anthony.com.ergot.model.ws.WsUtils.JSON;
import static ergot.anthony.com.ergot.model.ws.WsUtils.URL_SERVEUR;
import static ergot.anthony.com.ergot.model.ws.WsUtils.getOkHttpClient;
import static ergot.anthony.com.ergot.model.ws.WsUtils.gson;
import static ergot.anthony.com.ergot.model.ws.WsUtils.testInternetConnexionOnGoogle;

/**
 * Created by Anthony on 26/02/2018.
 */

public class WSUtilsAdmin {

    //ADMIN
    private static final String URL_UPDATE_COMMAND = URL_SERVEUR + "updateCommande";

    public static CommandeBean updateCommandStatut(CommandeBean commandeBean, int newStatut, long datePrevision) throws TechnicalException {
        //ON garde l'ancien statut en cas d'erreur
        int oldStatut = commandeBean.getStatut();
        long oldPrevisionDate = commandeBean.getDatePrevision();

        commandeBean.setStatut(newStatut);
        commandeBean.setDatePrevision(datePrevision);

        String json = gson.toJson(commandeBean);
        //On remet l'ancien, le serveur devra normalement retourner la comande avec le staut actualisé
        commandeBean.setStatut(oldStatut);
        commandeBean.setDatePrevision(oldPrevisionDate);

        Log.w("TAG_REQ", URL_UPDATE_COMMAND);
        Logger.logJson("TAG_JSON_ENVOYER", json);
        //Création de la requete

        Request request = new Request.Builder().url(URL_UPDATE_COMMAND).post(RequestBody.create(JSON, json)).build();

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
                CommandeBean returnCommand;
                if (MyApplication.isDebugMode()) {
                    //Résultat de la requete.

                    String jsonRecu = response.body().string();
                    Log.w("TAG_JSON_RECU", "json=" + json);
                    returnCommand = gson.fromJson(jsonRecu, CommandeBean.class);
                }
                else {
                    //JSON -> Java (Parser une ArrayList typée)
                    returnCommand = gson.fromJson(new InputStreamReader(response.body().byteStream()), CommandeBean.class);
                }
                return returnCommand;
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new TechnicalException(e, "Erreur lors du parsing de la réponse");
            }
        }
    }
}
