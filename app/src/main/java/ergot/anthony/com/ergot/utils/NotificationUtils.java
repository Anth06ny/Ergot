package ergot.anthony.com.ergot.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;

import ergot.anthony.com.ergot.MainActivity;
import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.controler.historique.CommandeAdapter;
import ergot.anthony.com.ergot.controler.panier.PanierActivity;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.model.bean.Statut;

/**
 * Created by Anthony on 14/03/2018.
 */

public class NotificationUtils {

    private static final String CHANNEL_ID = MyApplication.getMyApplication().getString(R.string.chanelId);
    private static final CharSequence CHANNEL_NAME = "Commandes";

    /**
     * Création du channel
     */
    private static void initChannel() {

        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) MyApplication.getMyApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Commandes");
        channel.enableLights(true);
        channel.setLightColor(MyApplication.getMyApplication().getColor(R.color.colorPrimaryDark));
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);
    }

    public static void updateNotificationStatut(Context context, CommandeBean commandeBean) {

        //A partir d'Oreo
        initChannel();

        switch (commandeBean.getStatut()) {
            case Statut.STATUS_DELIVERY:
                //On masque la norifiaciton
                removeNotification(context, commandeBean);
                break;

            case Statut.STATUS_SENDACCEPTED:
            case Statut.STATUS_SEND:
            case Statut.STATUS_READY:
            case Statut.STATUS_CANCEL:
                sendCommandeNotification(context, commandeBean);
                break;

            default:
                Log.w("TAG_FIREBASE", "Statut inconnu : " + commandeBean.getStatut());
        }
    }

    private static void sendCommandeNotification(Context context, CommandeBean commandeBean) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);

        //Action quand on clique sur la notification
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(PanierActivity.COMMANDE_EXTRA, commandeBean);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Image de droite sur la notification
        // Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_commande);
        contentView.setImageViewResource(R.id.iv, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.tv_num_commande, "" + commandeBean.getId());

        if (commandeBean.getDatePrevision() == 0) {
            contentView.setTextViewText(R.id.tv_time_expected, " - ");
        }
        else {
            contentView.setTextViewText(R.id.tv_time_expected, CommandeAdapter.formatheure.format(commandeBean.getDatePrevision()));
        }
        int statutID = R.string.status_attente;
        switch (commandeBean.getStatut()) {
            case Statut.STATUS_SEND:
                statutID = R.string.status_attente;
                break;

            case Statut.STATUS_SENDACCEPTED:
                statutID = R.string.status_Accepte;
                break;

            case Statut.STATUS_CANCEL:
                statutID = R.string.tv_command_canceled;
                contentView.setTextColor(R.id.bt_send, Color.RED);

                break;

            case Statut.STATUS_READY:
                statutID = R.string.status_ready;
                contentView.setTextColor(R.id.bt_send, Color.GREEN);
                contentView.setTextViewText(R.id.tv_time_expected, context.getText(R.string.status_ready));
                break;
        }

        contentView.setTextViewText(R.id.bt_send, context.getText(statutID));

        //Création de la notification
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContent(contentView)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)  //Permet d'enlever la notification quand on clique dessus
                .setPriority(Notification.PRIORITY_HIGH) // Permet un affichage de la notification à la récéption
                .setDefaults(Notification.DEFAULT_ALL);  //Affichage + son + vibration

        //Mettre une couleur au titre
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            notificationBuilder.setColor(MyApplication.getMyApplication().getResources().getColor(R.color.colorPrimaryDark)).setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        Log.w("TAG_FIREBASE", "Affiche la notification : " + commandeBean.getId());

        //On demande au système d'afficher la notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify((int) commandeBean.getId(), notificationBuilder.build());
    }

    public static void removeNotification(Context context, CommandeBean commandeBean) {
        Log.w("TAG_FIREBASE", "Retire la notification : " + commandeBean.getId());
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel((int) commandeBean.getId());
    }
}
