package ergot.anthony.com.ergot.commander;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.ProductBean;
import ergot.anthony.com.ergot.panier.PanierActivity;
import ergot.anthony.com.ergot.utils.Utils;

/**
 * Created by Anthony on 24/10/2017.
 */

public class MotherActivity extends AppCompatActivity implements View.OnClickListener {

    protected TextView tvNbArticle, tv_price;
    protected Button bt_commande;
    protected boolean panierVersion;
    protected ImageView tvPlusUn;
    protected View shadow;

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
        tvPlusUn = findViewById(R.id.tvPlusUn);
        shadow = findViewById(R.id.shadow);

        if (tvPlusUn != null) {
            tvPlusUn.setVisibility(View.INVISIBLE);
            tvPlusUn.setColorFilter(getResources().getColor(R.color.add_color));
        }
        bt_commande.setOnClickListener(this);

        if (this instanceof PanierActivity) {
            panierVersion = true;
            bt_commande.setText(R.string.bt_payer);
        }
        else {
            panierVersion = false;
            bt_commande.setText(R.string.bt_voir_commande);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshFoot();
    }

    protected void refreshFoot() {
        long totalPrice = 0;

        //On parcours tous les produits séléctionnés
        for (Pair<ProductBean, ArrayList<ProductBean>> selection : MyApplication.getCommandeBean().getProductList()) {
            ProductBean productBean = selection.first;
            totalPrice += productBean.getPrice();
            //on regarde s'il y a des suppléments dans les produits séléctionnés
            if (selection.second != null) {
                for (ProductBean productSupp : selection.second) {
                    totalPrice += productSupp.getPrice();
                }
            }
        }

        tvNbArticle.setText(MyApplication.getCommandeBean().getProductList().size() + "");
        tv_price.setText(Utils.longToStringPrice(totalPrice));
    }

    @Override
    public void onClick(View v) {
        if (v == bt_commande) {
            if (panierVersion) {

            }
            else {
                startActivity(new Intent(this, PanierActivity.class));
            }
        }
    }

    public void addProduct(Pair<ProductBean, ArrayList<ProductBean>> product, int[] clicOnScreen) {
        MyApplication.getCommandeBean().getProductList().add(product);
        if (tvPlusUn != null) {
            animAteFromeClicToFoot(clicOnScreen);
        }
        else {
            refreshFoot();
        }

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
    }

    private void animAteFromeClicToFoot(int[] clicOnScreen) {
        int originalPos[] = new int[2];
        shadow.getLocationOnScreen(originalPos);

        TranslateAnimation anim = new TranslateAnimation(clicOnScreen[0], originalPos[0], clicOnScreen[1], originalPos[1]);
        anim.setDuration(600);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tvPlusUn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvPlusUn.setVisibility(View.INVISIBLE);
                refreshFoot();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tvPlusUn.startAnimation(anim);
    }
}
