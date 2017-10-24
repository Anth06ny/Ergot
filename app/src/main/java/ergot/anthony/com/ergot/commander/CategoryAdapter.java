package ergot.anthony.com.ergot.commander;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.CategoryBean;
import ergot.anthony.com.ergot.utils.GlideApp;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<CategoryBean> categoryBeanList;
    private Drawable waitIcon, error_icon;

    public CategoryAdapter(Context context, List<CategoryBean> categoryBeanList) {
        this.categoryBeanList = categoryBeanList;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CategoryBean categoryBean = categoryBeanList.get(position);

        holder.rc_tv.setText(categoryBean.getName());
        GlideApp.with(holder.rc_tv.getContext()).load(categoryBean.getUrl()).placeholder(waitIcon).error(error_icon).fitCenter().into(holder.rc_iv);
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

        public ViewHolder(View itemView) {
            super(itemView);
            rc_tv = itemView.findViewById(R.id.rc_tv);
            rc_iv = itemView.findViewById(R.id.rc_iv);
        }
    }
}
