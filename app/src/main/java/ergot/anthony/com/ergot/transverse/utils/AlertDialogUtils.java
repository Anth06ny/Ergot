package ergot.anthony.com.ergot.transverse.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.model.bean.ComplementBean;
import ergot.anthony.com.ergot.model.bean.ComplementchoixBean;
import ergot.anthony.com.ergot.model.bean.StatutAnnulation;

/**
 * Created by Anthony on 25/10/2017.
 */

public class AlertDialogUtils {

    /**
     * Dialog pour séléctionner un supplément
     *
     * @param context
     * @param selectComplementDialogListener
     */
    public static void showSelectSuppDialog(final Context context, final ComplementBean complementBean, final SelectComplementDialogListener selectComplementDialogListener) {
        ArrayList<String> strings = new ArrayList<>();


        for (ComplementchoixBean p : complementBean.getComplementchoixes()) {

            if (p.getSuppPrix() > 0) {
                strings.add(p.getProduit().getNom() + " (" + Utils.longToStringPrice(p.getSuppPrix()) + ")");
            }
            else {
                strings.add(p.getProduit().getNom());
            }
        }

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle(complementBean.getQuestion());
        alt_bld.setSingleChoiceItems(strings.toArray(new String[0]), -1, new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();// dismiss the alertbox after chose option
                if (selectComplementDialogListener != null) {
                    selectComplementDialogListener.onComplementSelected(complementBean.getComplementchoixes().get(item));
                }
            }
        });
        alt_bld.setCancelable(true);
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    /**
     * Dialog pour séléctionner une raison d'annulation
     */
    public static void showSelectCancelReasonDialog(final Context context, final SelectCancelReasonListener cancelReasonListener) {

        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        alt_bld.setTitle(R.string.cancel_reason_lib);
        alt_bld.setSingleChoiceItems(StatutAnnulation.getValues(), StatutAnnulation.ADMIN.getValue(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                cancelReasonListener.onCancelReasonSelected(StatutAnnulation.values()[which]);
            }
        });
        alt_bld.setCancelable(true);
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public static void showConfirmCancelCommandDialog(final Context context, @StringRes int question, @StringRes int positiveText, DialogInterface.OnClickListener positiveButton) {

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        alt_bld.setMessage(question);
        alt_bld.setPositiveButton(positiveText, positiveButton);
        alt_bld.setNegativeButton(R.string.bt_no, null);
        alt_bld.setCancelable(true);
        alt_bld.create().show();
    }

    /**
     * Affiche une fenetre avec la liste des derniere commande de l'utilisateur et de leur succes
     *
     * @param context
     */
    public static void showHistoCommandUser(final Context context, CommandeBean commandeBean) {

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_history_user, null);

        TextView tvNbReussi = view.findViewById(R.id.tvNbReussi);
        TextView tvNbAnnulee = view.findViewById(R.id.tvNbAnnulee);
        TextView tvNbPreparerAnnulee = view.findViewById(R.id.tvNbPreparerAnnulee);
        TextView tvNbPreparerNonChercher = view.findViewById(R.id.tvNbPreparerNonChercher);

        tvNbReussi.setText(commandeBean.getUser().getNbCmdOk() + "");
        tvNbAnnulee.setText(commandeBean.getUser().getNbCmdCancel() + "");
        tvNbPreparerAnnulee.setText(commandeBean.getUser().getNbCmdReadyCancel() + "");
        tvNbPreparerNonChercher.setText(commandeBean.getUser().getNbCmdNverCome() + "");

        alt_bld.setView(view);
        alt_bld.setPositiveButton(R.string.bt_fermer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alt_bld.setCancelable(true);
        alt_bld.create().show();
    }

    public interface SelectComplementDialogListener {
        void onComplementSelected(ComplementchoixBean complementchoixBean);
    }

    public interface SelectCancelReasonListener {
        void onCancelReasonSelected(StatutAnnulation statutAnnulation);
    }
}
