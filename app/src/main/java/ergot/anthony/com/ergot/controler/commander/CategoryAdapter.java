package ergot.anthony.com.ergot.controler.commander;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.CategoryBean;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<CategoryBean> categoryBeanList;
    private Drawable waitIcon, error_icon;
    private OnCategoryClicListener onCategoryClicListener;

    public CategoryAdapter(Context context, List<CategoryBean> categoryBeanList, OnCategoryClicListener onCategoryClicListener) {
        this.categoryBeanList = categoryBeanList;
        this.onCategoryClicListener = onCategoryClicListener;
        int color = context.getResources().getColor(R.color.colorPrimary);
        waitIcon = context.getResources().getDrawable(R.drawable.ic_hourglass_empty_black_48dp);
        waitIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN);

        color = context.getResources().getColor(R.color.error_color);
        error_icon = context.getResources().getDrawable(R.drawable.ic_highlight_off_black_48dp);
        error_icon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);

        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CategoryBean categoryBean = categoryBeanList.get(position);

        holder.rc_tv.setText(categoryBean.getNom());
        Log.w("TAG_IMAGE", "" + categoryBean.getUrl_image());
        Picasso.get().load(categoryBean.getUrl_image()).placeholder(waitIcon).error(error_icon).centerCrop().fit().into(holder.rc_iv);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCategoryClicListener != null) {
                    onCategoryClicListener.onCategoryClick(categoryBean, holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryBeanList.size();
    }

    //------------------
    // View Holder
    //------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rc_tv;
        public ImageView rc_iv;
        private View root;

        public ViewHolder(View itemView) {
            super(itemView);
            rc_tv = itemView.findViewById(R.id.rc_tv);
            rc_iv = itemView.findViewById(R.id.rc_iv);
            root = itemView.findViewById(R.id.root);
        }
    }

    public interface OnCategoryClicListener {
        void onCategoryClick(CategoryBean categoryBean, CategoryAdapter.ViewHolder vh);
    }
}
