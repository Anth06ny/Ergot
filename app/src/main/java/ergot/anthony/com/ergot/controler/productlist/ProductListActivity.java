package ergot.anthony.com.ergot.controler.productlist;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.controler.commander.MotherActivity;
import ergot.anthony.com.ergot.model.bean.CategoryBean;
import ergot.anthony.com.ergot.model.bean.ComplementchoixBean;
import ergot.anthony.com.ergot.model.bean.ProductBean;
import ergot.anthony.com.ergot.model.bean.SelectionBean;
import ergot.anthony.com.ergot.transverse.utils.AlertDialogUtils;

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

        productAdapter = new ProductAdapter(categoryBean.getProduitsVisible(), this);
        rv.setAdapter(productAdapter);

        //Réglage toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_title.setText(categoryBean.getNom());

        //Image
        int color = getResources().getColor(R.color.colorPrimary);
        Drawable waitIcon = getResources().getDrawable(R.drawable.ic_hourglass_empty_black_48dp);
        waitIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN);

        color = getResources().getColor(R.color.error_color);
        Drawable error_icon = getResources().getDrawable(R.drawable.ic_highlight_off_black_48dp);
        error_icon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        Picasso.get().load(categoryBean.getUrl_image()).placeholder(waitIcon).error(error_icon).centerCrop().fit().into(civ);
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

    /**
     * @param productBean
     * @param clicOnScreen Position du clic à l'ecran pour l'animation
     */
    @Override
    public void onProductClick(ProductBean productBean, int[] clicOnScreen) {

        if (productBean.isRupture()) {
            Toast.makeText(this, R.string.product_disable, Toast.LENGTH_SHORT).show();
            return;
        }
        afficherComplement(productBean, clicOnScreen, 0, new ArrayList<ComplementchoixBean>());
    }

    /**
     * Methode recursive pour parcourir tout les complements.
     * Cumule dans selectedComplement la liste des complement choisis.
     * L'ensemble sera ajouté à la commande is on arrive au bout sans cancel.
     */
    private void afficherComplement(final ProductBean productBean, final int[] clicOnScreen, final int numero, final ArrayList<ComplementchoixBean> selectedComplement) {

        if (productBean.getListcomplements() == null || productBean.getListcomplements().size() <= numero) {
            // Pas de supplement
            addProduct(new SelectionBean(productBean, selectedComplement), clicOnScreen);
        } else {
            AlertDialogUtils.showSelectSuppDialog(this, productBean.getListcomplements().get(numero).getComplement(), new AlertDialogUtils.SelectComplementDialogListener() {
                @Override
                public void onComplementSelected(ComplementchoixBean complementchoixBean) {
                    selectedComplement.add(complementchoixBean);
                    afficherComplement(productBean, clicOnScreen, numero + 1, selectedComplement);
                }


            });
        }
    }
}
