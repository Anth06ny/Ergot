package ergot.anthony.com.ergot.controler.historique;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.model.bean.Statut;
import ergot.anthony.com.ergot.utils.Utils;

/**
 * Created by Utilisateur on 12/02/2018.
 */

public class CommandeAdapter extends RecyclerView.Adapter<CommandeAdapter.ViewHolder> {

    private List<CommandeBean> commandeBeanList;
    private OnComandeClicListener onComandeClicListener;
    private OnComandeClicListenerAdmin onComandeClicListenerAdmin;//Pointera sur le meme que onCommandClic, c'est juste pour eviter de faire les cast à chaque fois

    //Gestion de la date
    public static SimpleDateFormat formatCompl = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static SimpleDateFormat formatheure = new SimpleDateFormat("HH'h'mm");

    //Ressource
    private int selectColor, selectedTextSize, unSelectedTextSize;

    private CommandeAdapter(List<CommandeBean> commandeBeanList) {
        this.commandeBeanList = commandeBeanList;
        selectColor = MyApplication.getMyApplication().getResources().getColor(R.color.load_shadow_color);
        selectedTextSize = MyApplication.getMyApplication().getResources().getDimensionPixelSize(R.dimen.font_14);
        unSelectedTextSize = MyApplication.getMyApplication().getResources().getDimensionPixelSize(R.dimen.font_12);
    }

    public CommandeAdapter(List<CommandeBean> commandeBeanList, OnComandeClicListener onComandeClicListener) {
        this(commandeBeanList);
        this.onComandeClicListener = onComandeClicListener;
    }

    public CommandeAdapter(List<CommandeBean> commandeBeanList, OnComandeClicListenerAdmin onComandeClicListenerAdmin) {
        this(commandeBeanList);
        this.onComandeClicListener = onComandeClicListenerAdmin;
        this.onComandeClicListenerAdmin = onComandeClicListenerAdmin;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        //Mode admin on peut editer l'etat de la commande
        if (MyApplication.isAdminMode()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_command_admin, parent, false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history, parent, false);
        }
        return new CommandeAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return commandeBeanList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CommandeBean commandeBean = commandeBeanList.get(position);

        holder.tv_date.setText(formatCompl.format(commandeBean.getDateCommande()));
        holder.tv_price.setText(Utils.longToStringPrice(commandeBean.getTotalPrice()));

        //Par defaut bouton

        holder.bt_accept.setTypeface(null, Typeface.ITALIC);
        holder.bt_accept.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectedTextSize);
        if (holder.bt_send != null) {
            holder.bt_send.setTextColor(Color.BLACK);
            holder.bt_send.setTypeface(null, Typeface.ITALIC);
            holder.bt_send.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectedTextSize);
        }

        holder.bt_ready.setTypeface(null, Typeface.ITALIC);
        holder.bt_ready.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectedTextSize);

        holder.bt_delivery.setTypeface(null, Typeface.ITALIC);
        holder.bt_delivery.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectedTextSize);

        holder.bt_cancel.setTypeface(null, Typeface.ITALIC);
        holder.bt_cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectedTextSize);

        holder.ll_expected_time.setVisibility(View.VISIBLE);

        holder.tv_num_commande.setText(commandeBean.getId() + "");

        //defaut datePrevision
        if (commandeBean.getDatePrevision() == 0) {
            holder.tv_time_expected.setText(" - ");
        }
        else {
            holder.tv_time_expected.setText(formatheure.format(commandeBean.getDatePrevision()));
        }

        if (MyApplication.isAdminMode()) {
            onBindViewHolderAdmin(holder, commandeBean);
        }
        else {
            onBindViewHolderClient(holder, commandeBean);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onComandeClicListener != null) {
                    onComandeClicListener.showDetailCommandClick(commandeBean);
                }
            }
        });
    }

    /* ---------------------------------
    // Private
    // -------------------------------- */
    private void onBindViewHolderAdmin(final ViewHolder holder, final CommandeBean commandeBean) {
        //Email
        if (StringUtils.isNotBlank(commandeBean.getUser().getEmail())) {
            holder.tv_client.setText(commandeBean.getUser().getEmail());
        }
        else if (StringUtils.isNotBlank(commandeBean.getUser().getToken())) {
            holder.tv_client.setText(StringUtils.abbreviate(commandeBean.getUser().getToken(), 8));
        }
        else {
            holder.tv_client.setText(" - ");
        }

        //Téléphone
        holder.tv_telephone.setText(StringUtils.defaultIfBlank(commandeBean.getUser().getTelephone(), "-"));

        changeBtBackGroundTint((Button) holder.bt_accept, R.color.colorPrimaryDark);
        changeBtBackGroundTint((Button) holder.bt_ready, R.color.colorPrimaryDark);
        changeBtBackGroundTint((Button) holder.bt_delivery, R.color.colorPrimaryDark);
        changeBtBackGroundTint((Button) holder.bt_cancel, R.color.colorPrimaryDark);

        //Pour le moment invisible tout le temps
        holder.iv_warning.setVisibility(View.INVISIBLE);
        holder.iv_new.setVisibility(View.INVISIBLE);

        //if(co)

        switch (commandeBean.getStatut()) {
            case Statut.STATUS_SEND:
                holder.iv_new.setVisibility(View.VISIBLE);
                break;
            case Statut.STATUS_SENDACCEPTED:
                holder.bt_accept.setTypeface(null, Typeface.BOLD);
                changeBtBackGroundTint((Button) holder.bt_accept, R.color.load_shadow_color);
                holder.bt_accept.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                break;
            case Statut.STATUS_READY:
                changeBtBackGroundTint((Button) holder.bt_ready, R.color.green);
                holder.bt_ready.setTypeface(null, Typeface.BOLD);
                holder.bt_ready.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                break;
            case Statut.STATUS_DELIVERY:
                changeBtBackGroundTint((Button) holder.bt_delivery, R.color.green);
                holder.bt_delivery.setTypeface(null, Typeface.BOLD);
                holder.bt_delivery.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                break;
            case Statut.STATUS_CANCEL:
                changeBtBackGroundTint((Button) holder.bt_cancel, R.color.error_color);
                holder.bt_cancel.setTypeface(null, Typeface.BOLD);
                holder.bt_cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                break;
        }

        holder.bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onComandeClicListenerAdmin.onAcceptCommand(commandeBean);
            }
        });

        holder.bt_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onComandeClicListenerAdmin.onReadyCommand(commandeBean);
            }
        });

        holder.bt_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onComandeClicListenerAdmin.onSendCommandClick(commandeBean);
            }
        });

        holder.bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onComandeClicListenerAdmin.cancelCommandClick(commandeBean);
            }
        });
    }

    private void onBindViewHolderClient(final ViewHolder holder, final CommandeBean commandeBean) {

        holder.bt_ready.setTextColor(Color.BLACK);
        holder.bt_delivery.setTextColor(Color.BLACK);
        holder.bt_cancel.setTextColor(Color.BLACK);
        holder.bt_accept.setTextColor(Color.BLACK);

        switch (commandeBean.getStatut()) {
            case Statut.STATUS_SEND:
                holder.bt_send.setTypeface(null, Typeface.BOLD);
                holder.bt_send.setTextColor(selectColor);
                holder.bt_send.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                break;
            case Statut.STATUS_SENDACCEPTED:
                holder.bt_accept.setTypeface(null, Typeface.BOLD);
                holder.bt_accept.setTextColor(selectColor);
                holder.bt_accept.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                break;
            case Statut.STATUS_READY:
                holder.bt_ready.setTypeface(null, Typeface.BOLD);
                holder.bt_ready.setTextColor(Color.GREEN);
                holder.bt_ready.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                holder.ll_expected_time.setVisibility(View.GONE);
                break;
            case Statut.STATUS_DELIVERY:
                holder.bt_delivery.setTypeface(null, Typeface.BOLD);
                holder.bt_delivery.setTextColor(Color.GREEN);
                holder.bt_delivery.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                holder.ll_expected_time.setVisibility(View.GONE);
                break;
            case Statut.STATUS_CANCEL:
                holder.bt_cancel.setTypeface(null, Typeface.BOLD);
                holder.bt_cancel.setTextColor(Color.RED);
                holder.bt_cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                holder.ll_expected_time.setVisibility(View.GONE);
                break;
        }
    }

    private static void changeBtBackGroundTint(Button bt, @ColorRes int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bt.setBackgroundTintList(ContextCompat.getColorStateList(bt.getContext(), colorId));
        }
        else {
            bt.setBackgroundResource(colorId);
        }
    }

    /* ---------------------------------
    //      View Holder
    // -------------------------------- */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Commun
        public TextView tv_date;
        public TextView tv_price;
        public TextView bt_accept;
        public TextView bt_ready;
        public TextView bt_delivery;
        public TextView bt_cancel;
        public TextView tv_num_commande;
        public View ll_expected_time;
        private View root;

        //Client
        public TextView bt_send;
        public TextView tv_time_expected;

        //Admin
        public TextView tv_client;
        public TextView tv_telephone;
        public ImageView iv_warning;
        public ImageView iv_new;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time_expected = itemView.findViewById(R.id.tv_time_expected);
            tv_price = itemView.findViewById(R.id.tv_price);
            bt_send = itemView.findViewById(R.id.bt_send);
            bt_accept = itemView.findViewById(R.id.bt_accept);
            bt_ready = itemView.findViewById(R.id.bt_ready);
            bt_delivery = itemView.findViewById(R.id.bt_delivery);
            bt_cancel = itemView.findViewById(R.id.bt_cancel);
            ll_expected_time = itemView.findViewById(R.id.ll_expected_time);
            tv_client = itemView.findViewById(R.id.tv_client);
            tv_telephone = itemView.findViewById(R.id.tv_telephone);
            iv_warning = itemView.findViewById(R.id.iv_warning);
            iv_new = itemView.findViewById(R.id.iv_new);
            tv_num_commande = itemView.findViewById(R.id.tv_num_commande);

            root = itemView.findViewById(R.id.root);
        }
    }

    /* ---------------------------------
    // interface
    // -------------------------------- */

    public interface OnComandeClicListener {
        void showDetailCommandClick(CommandeBean commandeBean);
    }

    public interface OnComandeClicListenerAdmin extends OnComandeClicListener {

        void cancelCommandClick(CommandeBean commandeBean);

        void onAcceptCommand(CommandeBean commandeBean);

        void onReadyCommand(CommandeBean commandeBean);

        void onSendCommandClick(CommandeBean commandeBean);
    }
}
