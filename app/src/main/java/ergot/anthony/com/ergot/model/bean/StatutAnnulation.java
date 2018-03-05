package ergot.anthony.com.ergot.model.bean;

import android.support.annotation.StringRes;

import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;

/**
 * Created by Anthony on 05/03/2018.
 */

public enum StatutAnnulation {

    //Annulation correct sans consequence
    //5min pour annuler une commande ou 20min avant la date de prévision
    ADMIN(0, R.string.status_ann_admin),

    //Annulation  mais pas en derniere minute.
    CLIENT(1, R.string.status_ann_client),

    //Annulation alors que la commande est préte et apres les 5min de passage de commande
    CLIENT_CMD_PRETE(2, R.string.status_ann_client_cmd_ready),
    //Client jamais venu
    CLIENT_NEVER_COME(3, R.string.status_ann_client_never_come);

    private String text;
    private int value;

    StatutAnnulation(int value, @StringRes int resId) {
        this.text = MyApplication.getMyApplication().getString(resId);
        this.value = value;
    }

    public static String[] getValues() {
        String[] strs = new String[StatutAnnulation.values().length];
        int i = 0;
        for (StatutAnnulation p : StatutAnnulation.values()) {
            strs[i++] = p.toString();
        }

        return strs;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return text;
    }
}
