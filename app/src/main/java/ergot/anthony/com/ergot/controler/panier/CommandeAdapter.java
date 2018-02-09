package ergot.anthony.com.ergot.controler.panier;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.ProductBean;
import ergot.anthony.com.ergot.model.bean.SuppBean;
import ergot.anthony.com.ergot.utils.Utils;

public class CommandeAdapter extends RecyclerView.Adapter<CommandeAdapter.ViewHolder> {

    private ArrayList<Pair<ProductBean, SuppBean>> commandList;
    private OnCommandListener onCommandListener;

    public CommandeAdapter(ArrayList<Pair<ProductBean, SuppBean>> commandList, OnCommandListener onCommandListener) {
        this.commandList = commandList;
        this.onCommandListener = onCommandListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_commade, parent, false);
        return new CommandeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Pair<ProductBean, SuppBean> produitAndSupp = commandList.get(position);

        //Nom du produit
        holder.tv_title.setText(produitAndSupp.first.getName());

        //On parcourt les supp pour remplir la description et calculer le prix final avec supplement
        String description = "";
        long price = produitAndSupp.first.getPrice();
        if (produitAndSupp.second != null) {
            price += produitAndSupp.second.getNewPrice();

            if (price == 0) {
                description += produitAndSupp.second.getProduit().getName() + "  ";
            }
            else {
                description += produitAndSupp.second.getProduit().getName() + " (+" + Utils.longToStringPrice(price) + ")";
            }
        }

        holder.tv_price.setText(Utils.longToStringPrice(price));
        holder.tv_description.setText(description);

        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommandListener.onRemoveProductClic(produitAndSupp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commandList.size();
    }

    //------------------
    // View Holder
    //------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_description, tv_price;
        public ImageView iv;
        public View root;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_price = itemView.findViewById(R.id.tv_price);
            iv = itemView.findViewById(R.id.iv);
            root = itemView.findViewById(R.id.root);

            iv.setColorFilter(iv.getResources().getColor(android.support.v7.appcompat.R.color.error_color_material));
        }
    }

    public interface OnCommandListener {
        void onRemoveProductClic(Pair<ProductBean, SuppBean> produitAndSupp);
    }
}
