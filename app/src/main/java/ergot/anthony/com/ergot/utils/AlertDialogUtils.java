package ergot.anthony.com.ergot.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import ergot.anthony.com.ergot.model.bean.ProductBean;

/**
 * Created by Anthony on 25/10/2017.
 */

public class AlertDialogUtils {

    public static void getRadioAlertDialog(final Context context, final ArrayList<ProductBean> list, final RadioAlertDialogCB radioAlertDialogCB) {
        ArrayList<String> strings = new ArrayList<>();

        for (ProductBean p : list) {
            if (p.getPrice() > 0) {
                strings.add(p.getName() + " (" + Utils.longToStringPrice(p.getPrice()) + ")");
            }
            else {
                strings.add(p.getName());
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
        void onSupplementSelected(ProductBean productBean);
    }
}
