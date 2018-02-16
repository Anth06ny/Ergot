package ergot.anthony.com.ergot.controler.historique;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.model.bean.Status;
import ergot.anthony.com.ergot.utils.AlertDialogUtils;
import ergot.anthony.com.ergot.utils.Utils;

/**
 * Created by Utilisateur on 12/02/2018.
 */

public class CommandeAdapter extends RecyclerView.Adapter<CommandeAdapter.ViewHolder> {

    private List<CommandeBean> commandeBeanList;
    private OnComandeClicListener onComandeClicListener;

    //Gestion de la date
    private SimpleDateFormat formatCompl = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private SimpleDateFormat formatheure = new SimpleDateFormat("HH'h'mm");

    public CommandeAdapter(List<CommandeBean> commandeBeanList, OnComandeClicListener onComandeClicListener) {
        this.commandeBeanList = commandeBeanList;
        this.onComandeClicListener = onComandeClicListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history, parent, false);

        return new CommandeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CommandeBean commandeBean = commandeBeanList.get(position);

        int selectColor = MyApplication.getMyApplication().getResources().getColor(R.color.load_shadow_color);
        int selectedTextSize = holder.bt_cancel.getResources().getDimensionPixelSize(R.dimen.font_14);
        int unSelectedTextSize = holder.bt_cancel.getResources().getDimensionPixelSize(R.dimen.font_12);

        holder.tv_date.setText(formatCompl.format(commandeBean.getDateCommande()));
        holder.tv_price.setText(Utils.longToStringPrice(commandeBean.getTotalPrice()));
        if (commandeBean.getDatePrevision() > 0) {
            holder.tv_time_expected.setText(formatheure.format(commandeBean.getDatePrevision()));
        }
        else {
            holder.tv_time_expected.setText(" - ");
        }
        holder.bt_cancel_order.setVisibility(View.VISIBLE);

        holder.bt_accept.setTextColor(Color.BLACK);
        holder.bt_accept.setTypeface(null, Typeface.ITALIC);
        holder.bt_accept.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectedTextSize);
        holder.bt_send.setTextColor(Color.BLACK);
        holder.bt_send.setTypeface(null, Typeface.ITALIC);
        holder.bt_send.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectedTextSize);
        holder.bt_ready.setTextColor(Color.BLACK);
        holder.bt_ready.setTypeface(null, Typeface.ITALIC);
        holder.bt_ready.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectedTextSize);
        holder.bt_delivery.setTextColor(Color.BLACK);
        holder.bt_delivery.setTypeface(null, Typeface.ITALIC);
        holder.bt_delivery.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectedTextSize);
        holder.bt_cancel.setTextColor(Color.BLACK);
        holder.bt_cancel.setTypeface(null, Typeface.ITALIC);
        holder.bt_cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectedTextSize);

        switch (commandeBean.getStatut()) {
            case Status.STATUS_SEND:
                holder.bt_send.setTypeface(null, Typeface.BOLD);
                holder.bt_send.setTextColor(selectColor);
                holder.bt_send.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                break;
            case Status.STATUS_SENDACCEPTED:
                holder.bt_accept.setTypeface(null, Typeface.BOLD);
                holder.bt_accept.setTextColor(selectColor);
                holder.bt_accept.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                break;
            case Status.STATUS_READY:
                holder.bt_ready.setTypeface(null, Typeface.BOLD);
                holder.bt_ready.setTextColor(Color.GREEN);
                holder.bt_ready.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                break;
            case Status.STATUS_DELIVERY:
                holder.bt_delivery.setTypeface(null, Typeface.BOLD);
                holder.bt_delivery.setTextColor(Color.GREEN);
                holder.bt_delivery.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                holder.bt_cancel_order.setVisibility(View.GONE);//On affiche pas le bouton si la commande est déja annulé ou livré
                break;
            case Status.STATUS_CANCEL:
                holder.bt_cancel.setTypeface(null, Typeface.BOLD);
                holder.bt_cancel.setTextColor(Color.RED);
                holder.bt_cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                holder.bt_cancel_order.setVisibility(View.GONE);//On affiche pas le bouton si la commande est déja annulé ou livré
                break;
        }

        //Show detail
        holder.bt_show_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onComandeClicListener != null) {
                    onComandeClicListener.showDetailCommand(commandeBean);
                }
            }
        });

        //Annulation Comande
        holder.bt_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogUtils.showConfirmDialog(holder.tv_date.getContext(), R.string.dialog_supp_commande, R.string.bt_cancel_order, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onComandeClicListener != null) {
                            onComandeClicListener.cancelCommand(commandeBean);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return commandeBeanList.size();
    }

    //------------------
    // View Holder
    //------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_date;
        public TextView tv_time_expected;
        public TextView tv_price;
        public TextView bt_send;
        public TextView bt_accept;
        public TextView bt_ready;
        public TextView bt_delivery;
        public TextView bt_cancel;
        public TextView bt_show_detail;
        public TextView bt_cancel_order;
        private View root;

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
            bt_show_detail = itemView.findViewById(R.id.bt_show_detail);
            bt_cancel_order = itemView.findViewById(R.id.bt_cancel_order);

            root = itemView.findViewById(R.id.root);
        }
    }

    public interface OnComandeClicListener {
        void showDetailCommand(CommandeBean commandeBean);

        void cancelCommand(CommandeBean commandeBean);
    }
}
