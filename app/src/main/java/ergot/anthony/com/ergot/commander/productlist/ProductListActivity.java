package ergot.anthony.com.ergot.commander.productlist;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.commander.MotherActivity;
import ergot.anthony.com.ergot.model.bean.CategoryBean;
import ergot.anthony.com.ergot.model.bean.ProductBean;
import ergot.anthony.com.ergot.model.bean.SuppBean;
import ergot.anthony.com.ergot.utils.AlertDialogUtils;
import ergot.anthony.com.ergot.utils.GlideApp;

public class ProductListActivity extends MotherActivity implements ProductAdapter.OnProductClicListener {

    public static final String CATEGORY_BEAN_EXTRA = "CATEGORY_BEAN_EXTRA";

    private CategoryBean categoryBean;

    private RecyclerView rv;
    private TextView tv_title;
    private ImageView civ;

    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        tv_title = findViewById(R.id.tv_title);
        civ = findViewById(R.id.civ);
        rv = findViewById(R.id.rv);

        categoryBean = (CategoryBean) getIntent().getSerializableExtra(CATEGORY_BEAN_EXTRA);

        //RecylcerView
        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        productAdapter = new ProductAdapter(categoryBean.getProductBeenList(), this);
        rv.setAdapter(productAdapter);

        //Réglage toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_title.setText(categoryBean.getName());
        GlideApp.with(this).load(R.drawable.burger).into(civ);
    }

    /**
     * Pour eviter qu'il recrée l'activité parante
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* ---------------------------------
    // Callback
    // -------------------------------- */

    @Override
    public void onProductClick(final ProductBean productBean) {
        SuppBean suppBean = productBean.getSuppBean();

        if (suppBean == null) {
            // Pas de supplement
            MyApplication.getCommandeBean().getProductList().add(new Pair<ProductBean, ArrayList<ProductBean>>(productBean, null));
            supportFinishAfterTransition();
        }
        else {
            AlertDialogUtils.getRadioAlertDialog(this, suppBean.getSupplement(), new AlertDialogUtils.RadioAlertDialogCB() {
                @Override
                public void onSupplementSelected(ProductBean supp) {
                    //la liste de supplement
                    ArrayList<ProductBean> selectedSupp = new ArrayList<>();
                    selectedSupp.add(supp);
                    MyApplication.getCommandeBean().getProductList().add(new Pair<ProductBean, ArrayList<ProductBean>>(productBean, selectedSupp));
                    supportFinishAfterTransition();
                }
            });
        }
    }
}
