package ergot.anthony.com.ergot.controler.panier;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.ComplementchoixBean;
import ergot.anthony.com.ergot.model.bean.SelectionBean;
import ergot.anthony.com.ergot.transverse.utils.Utils;

public class CommandeAdapter extends RecyclerView.Adapter<CommandeAdapter.ViewHolder> {

    private ArrayList<SelectionBean> commandList;
    private OnCommandListener onCommandListener;
    private boolean editable;

    public CommandeAdapter(ArrayList<SelectionBean> commandList, OnCommandListener onCommandListener, boolean editable) {
        this.commandList = commandList;
        this.onCommandListener = onCommandListener;
        this.editable = editable;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_commade, parent, false);
        return new CommandeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final SelectionBean produitAndSupp = commandList.get(position);

        //Nom du produit
        holder.tv_title.setText(produitAndSupp.getProductBean().getNom());
        holder.tv_price.setText(Utils.longToStringPrice(produitAndSupp.getProductBean().getPrix()));
        holder.tv_description.setText(produitAndSupp.getProductBean().getDescription());

        //On parcourt les supp pour remplir la description des choix
        String complement = "";
        if (produitAndSupp.getComplementchoixBeans() != null) {
            for (ComplementchoixBean c : produitAndSupp.getComplementchoixBeans()) {
                complement += c.getProduit().getNom() + " / ";
            }
        }
        holder.tv_complement.setText(StringUtils.removeEnd(complement, " / "));


        //Si on est en mode éditable on affiche la possibilité de supprimer un élément
        if (editable) {
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCommandListener.onRemoveProductClic(produitAndSupp);
                }
            });
        }
        else {
            holder.iv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return commandList.size();
    }

    public interface OnCommandListener {
        void onRemoveProductClic(SelectionBean selectProductBean);
    }

    //------------------
    // View Holder
    //------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_description, tv_price, tv_complement;
        public ImageView iv;
        public View root;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_complement = itemView.findViewById(R.id.tv_complement);
            iv = itemView.findViewById(R.id.iv);
            root = itemView.findViewById(R.id.root);

            iv.setColorFilter(iv.getResources().getColor(android.support.v7.appcompat.R.color.error_color_material_dark));
        }
    }
}
