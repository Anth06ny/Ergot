package ergot.anthony.com.ergot.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.SuppBean;

/**
 * Created by Anthony on 25/10/2017.
 */

public class AlertDialogUtils {

    public static void showSelectSuppDialog(final Context context, final ArrayList<SuppBean> list, final RadioAlertDialogCB radioAlertDialogCB) {
        ArrayList<String> strings = new ArrayList<>();

        for (SuppBean p : list) {

            if (p.getNewPrice() > 0) {
                strings.add(p.getProduit().getName() + " (" + Utils.longToStringPrice(p.getNewPrice()) + ")");
            }
            else {
                strings.add(p.getProduit().getName());
            }
        }

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle("Supplément accompagnement");
        alt_bld.setSingleChoiceItems(strings.toArray(new String[0]), -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();// dismiss the alertbox after chose option
                if (radioAlertDialogCB != null) {
                    radioAlertDialogCB.onSupplementSelected(list.get(item));
                }
            }
        });
        alt_bld.setCancelable(true);
        AlertDialog alert = alt_bld.create();
        alert.show();
    }



    public interface RadioAlertDialogCB {
        void onSupplementSelected(SuppBean suppBean);
    }

    public static void showConfirmDialog(final Context context, @StringRes int question, @StringRes int positiveText, DialogInterface.OnClickListener positiveButton) {

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        alt_bld.setMessage(question);
        alt_bld.setPositiveButton(positiveText, positiveButton);
        alt_bld.setNegativeButton(R.string.bt_cancel, null);
        alt_bld.setCancelable(true);
        alt_bld.create().show();
    }
}
