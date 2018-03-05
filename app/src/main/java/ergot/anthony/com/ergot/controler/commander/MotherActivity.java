package ergot.anthony.com.ergot.controler.commander;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ergot.anthony.com.ergot.MainActivity;
import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.controler.panier.PanierActivity;
import ergot.anthony.com.ergot.model.bean.sendbean.SelectProductBean;
import ergot.anthony.com.ergot.utils.Utils;

/**
 * Created by Anthony on 24/10/2017.
 */

public class MotherActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int HOME_MENU_ID = 3;

    protected TextView tvNbArticle, tv_price;
    protected Button bt_commande;
    protected boolean panierVersion;
    protected ImageView tvPlusUn;
    protected View shadow;
    private View foot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("TAG_ACTIVITY", "Activity : " + getClass().getName());
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        tvNbArticle = findViewById(R.id.tvNbArticle);
        bt_commande = findViewById(R.id.bt_commande);
        tv_price = findViewById(R.id.tv_price);
        tvPlusUn = findViewById(R.id.tvPlusUn);
        shadow = findViewById(R.id.shadow);
        foot = findViewById(R.id.foot);

        if (tvPlusUn != null) {
            tvPlusUn.setVisibility(View.INVISIBLE);
            tvPlusUn.setColorFilter(getResources().getColor(R.color.add_color));
        }
        bt_commande.setOnClickListener(this);

        if (this instanceof PanierActivity) {
            panierVersion = true;
            bt_commande.setText(R.string.bt_envoyer);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, HOME_MENU_ID, 0, "Acceuil").setIcon(R.mipmap.ic_home).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == HOME_MENU_ID) {
            Intent a = new Intent(this, MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        }

        return super.onOptionsItemSelected(item);
    }

    /* ---------------------------------
    // Protected
    // -------------------------------- */

    protected void hideFoot() {
        shadow.setVisibility(View.GONE);
        foot.setVisibility(View.GONE);
    }

    protected void refreshFoot() {

        tvNbArticle.setText(MyApplication.getCommandeBean().getCompositionCommande().size() + "");
        tv_price.setText(Utils.longToStringPrice(MyApplication.getCommandeBean().getTotalPrice()));
    }

    public void addProduct(SelectProductBean selection, int[] clicOnScreen) {
        MyApplication.getCommandeBean().getCompositionCommande().add(selection);
        if (tvPlusUn != null) {
            animAteFromeClicToFoot(clicOnScreen);
        }
        else {
            refreshFoot();
        }

        Utils.vibrate();
    }

    /* ---------------------------------
    // Click
    // -------------------------------- */
    @Override
    public void onClick(View v) {
        if (v == bt_commande) {
            if (panierVersion) {
                //gerer dans panierActivity
            }
            else {
                if (MyApplication.getCommandeBean().getCompositionCommande().isEmpty()) {
                    Toast.makeText(this, R.string.no_produit_to_send, Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(this, PanierActivity.class));
                }
            }
        }
    }


       /* ---------------------------------
       // private
       // -------------------------------- */

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
