package ergot.anthony.com.ergot.controler.productlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.ProductBean;
import ergot.anthony.com.ergot.utils.Utils;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final List<ProductBean> productBeanList;
    private OnProductClicListener onCategoryClicListener;

    public ProductAdapter(List<ProductBean> productBeanList, OnProductClicListener onCategoryClicListener) {
        this.productBeanList = productBeanList;
        this.onCategoryClicListener = onCategoryClicListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product, parent, false);

        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ProductBean productBean = productBeanList.get(position);

        holder.tv_title.setText(productBean.getName());
        holder.tv_price.setText(Utils.longToStringPrice(productBean.getPrice()));

        if (StringUtils.isBlank(productBean.getDescription())) {
            holder.tv_description.setVisibility(View.GONE);
        }
        else {
            holder.tv_description.setText(productBean.getDescription());
            holder.tv_description.setVisibility(View.VISIBLE);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCategoryClicListener != null) {
                    int[] clicOnScreen = new int[2];
                    holder.tv_price.getLocationOnScreen(clicOnScreen);
                    onCategoryClicListener.onProductClick(productBean, clicOnScreen);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productBeanList.size();
    }

    //------------------
    // View Holder
    //------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_description, tv_price;
        public ImageView rc_iv;
        public View root;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_price = itemView.findViewById(R.id.tv_price);
            root = itemView.findViewById(R.id.root);
        }
    }

    public interface OnProductClicListener {
        void onProductClick(ProductBean productBean, int[] clicOnScreen);
    }
}
