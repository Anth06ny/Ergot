package ergot.anthony.com.ergot.commander;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.ProductBean;
import ergot.anthony.com.ergot.utils.Utils;

/**
 * Created by Anthony on 24/10/2017.
 */

public class MotherActivity extends AppCompatActivity implements View.OnClickListener {

    protected TextView tvNbArticle, tv_price;
    protected Button bt_commande;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        tvNbArticle = findViewById(R.id.tvNbArticle);
        bt_commande = findViewById(R.id.bt_commande);
        tv_price = findViewById(R.id.tv_price);

        bt_commande.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshFoot();
    }

    protected void refreshFoot() {
        long totalPrice = 0;

        //On parcours tous les produits séléctionnés
        for (Pair<ProductBean, ArrayList<ProductBean>> selection : MyApplication.getCommande().getProductList()) {
            ProductBean productBean = selection.first;
            totalPrice += productBean.getPrice();
            //on regarde s'il y a des suppléments dans les produits séléctionnés
            if (selection.second != null) {
                for (ProductBean productSupp : selection.second) {
                    totalPrice += productSupp.getPrice();
                }
            }
        }

        tvNbArticle.setText(MyApplication.getCommande().getProductList().size() + "");
        tv_price.setText(Utils.longToStringPrice(totalPrice));
    }

    @Override
    public void onClick(View v) {
        if (v == bt_commande) {

        }
    }
}
