package ergot.anthony.com.ergot.model.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.apache.commons.lang3.StringUtils;

import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.exception.TechnicalException;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.model.ws.WsUtils;
import ergot.anthony.com.ergot.utils.NotificationUtils;

/* Attention si pas dans data ca ne sera pas captéé par firebase et affichera une notif par defaut si l'application est en background
  url : https://fcm.googleapis.com/fcm/send

  HEADER

  (Server key)
  Authorization : key=AAAAiR-3EhU:APA91bGgLSgOkaWwd0P1aoNXITcT3Q3FOgNQPTRmwnoJRfOSGNcqiAfItiJHcbcrLWBIfyK9V7zBoV5i1MiSr_v1UEfLjMlMiG_r5zjwQv7ATBK1sIjQJSKucL7tg0H0H5pcl8zGiX3k

  Content-Type : application/json

  BODY
 {
   "to":"cCDUOK_IPZs:APA91bG57f3p6_rnleA3hmjE3I3i1vOwpxO0AYfX1EARy416cJj1x1RqbNCfUoCthQvIgbabzFeiLOW8RkGOXPgfwgOq3CIlTmTUyD6_1qJ8cX8ORFAavFu6LJ9kPMgBZjkFhlZxQD5S",
   "data" : {
     "body" : "This is an FCM notification message!",
     "title" : "FCM Message"
   }
 }

 */
public class FireBaseNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Si on decommente, affichera la notification si on ne le fait pas nous
        //super.onMessageReceived(remoteMessage);

        Log.w("TAG_FIREBASE", "from" + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.w("TAG_FIREBASE", "Message body payload: " + remoteMessage.getData());
            try {
                CommandeBean commandeBean = WsUtils.gson.fromJson(remoteMessage.getData().get("body"), CommandeBean.class);
                if (commandeBean == null || commandeBean.getId() == 0) {
                    throw new TechnicalException("Le json reçu n'est pas une commande");
                }
                else {
                    //On rcréer ou met à jour la notification
                    NotificationUtils.updateNotificationStatut(MyApplication.getMyApplication(), commandeBean);
                    //On la poste dans le bus
                    MyApplication.getBus().post(commandeBean);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Log.w("TAG_FIREBASE", "getData vide");
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null && StringUtils.isNotBlank(remoteMessage.getNotification().getBody())) {
            Log.w("TAG_FIREBASE", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
