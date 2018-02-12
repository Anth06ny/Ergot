package ergot.anthony.com.ergot.controler.historique;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private SimpleDateFormat formatheure = new SimpleDateFormat("HH`hmm");

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

        holder.tv_date.setText(formatCompl.format(commandeBean.getDateCommande()));
        holder.tv_price.setText(Utils.longToStringPrice(commandeBean.getTotalPrice()));
        holder.tv_time_expected.setText(formatheure.format(commandeBean.getDatePrevision()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.bt_accept.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            holder.bt_accept.setTextColor(Color.BLACK);
            holder.bt_send.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            holder.bt_send.setTextColor(Color.BLACK);
            holder.bt_ready.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            holder.bt_ready.setTextColor(Color.BLACK);
            holder.bt_delivery.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            holder.bt_delivery.setTextColor(Color.BLACK);
            holder.bt_cancel.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            holder.bt_cancel.setTextColor(Color.BLACK);
        }

        switch (commandeBean.getStatus()) {
            case Status.STATUS_SEND:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.bt_send.setBackgroundTintList(ColorStateList.valueOf(selectColor));
                }
                holder.bt_send.setTextColor(Color.WHITE);
                break;
            case Status.STATUS_SENDACCEPTED:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.bt_send.setBackgroundTintList(ColorStateList.valueOf(selectColor));
                }
                holder.bt_send.setTextColor(Color.WHITE);
                break;
            case Status.STATUS_READY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.bt_send.setBackgroundTintList(ColorStateList.valueOf(selectColor));
                }
                holder.bt_send.setTextColor(Color.WHITE);
                break;
            case Status.STATUS_DELIVERY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.bt_send.setBackgroundTintList(ColorStateList.valueOf(selectColor));
                }
                holder.bt_send.setTextColor(Color.WHITE);
                break;
            case Status.STATUS_CANCEL:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.bt_send.setBackgroundTintList(ColorStateList.valueOf(selectColor));
                }
                holder.bt_send.setTextColor(Color.WHITE);
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
        return 0;
    }

    //------------------
    // View Holder
    //------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_date;
        public TextView tv_time_expected;
        public TextView tv_price;
        public Button bt_send;
        public Button bt_accept;
        public Button bt_ready;
        public Button bt_delivery;
        public Button bt_cancel;
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
