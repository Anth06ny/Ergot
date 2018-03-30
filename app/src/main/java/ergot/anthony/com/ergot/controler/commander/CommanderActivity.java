package ergot.anthony.com.ergot.controler.commander;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.controler.productlist.ProductListActivity;
import ergot.anthony.com.ergot.transverse.exception.TechnicalException;
import ergot.anthony.com.ergot.model.bean.CategoryBean;
import ergot.anthony.com.ergot.model.ws.WsUtils;

public class CommanderActivity extends MotherActivity implements View.OnClickListener, CategoryAdapter.OnCategoryClicListener {

    //Composant graphique
    private RecyclerView rv;
    private TextView tv_wait, tv_error;
    private Button bt_refresh;
    private View ll_error;

    //data affichage
    private TechnicalException erreur;
    private boolean showWaitingMessage;

    //metier
    private CategoryAdapter categoryAdapter;

    //donnees
    private ArrayList<CategoryBean> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander);

        categoryList = new ArrayList<>();

        rv = findViewById(R.id.rv);
        tv_wait = findViewById(R.id.tv_wait);
        tv_error = findViewById(R.id.tv_error);
        bt_refresh = findViewById(R.id.bt_refresh);
        ll_error = findViewById(R.id.ll_error);

        ll_error.setVisibility(View.INVISIBLE);

        //est ce que la taille de la recycle view va changer ?
        rv.setHasFixedSize(true);
        //A ajouter obligatoirement
        rv.setLayoutManager(new GridLayoutManager(this, 3));

        rv.setItemAnimator(new DefaultItemAnimator());

        categoryAdapter = new CategoryAdapter(this, categoryList, this);
        rv.setAdapter(categoryAdapter);

        bt_refresh.setOnClickListener(this);

        //On charge les données
        new WSAsyncTask().execute();
    }

    /* ---------------------------------
    // CallBack
    // -------------------------------- */

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == bt_refresh) {
            new WSAsyncTask().execute();
        }
    }

    @Override
    public void onCategoryClick(CategoryBean categoryBean, CategoryAdapter.ViewHolder vh) {

        //On trasnmet image et text avec animation
        Intent intent = new Intent(this, ProductListActivity.class);
        // Pass data object in the bundle and populate details activity.
        intent.putExtra(ProductListActivity.CATEGORY_BEAN_EXTRA, categoryBean);
        Pair<View, String> p1 = Pair.create((View) vh.rc_iv, "trans_image");
        Pair<View, String> p2 = Pair.create((View) vh.rc_tv, "trans_text");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
        startActivity(intent, options.toBundle());
    }

    /* ---------------------------------
    // private
    // -------------------------------- */

    private void refreshScreen() {

        tv_wait.setVisibility(View.INVISIBLE);
        ll_error.setVisibility(View.INVISIBLE);

        if (showWaitingMessage) {
            tv_wait.setVisibility(View.VISIBLE);
        }
        else if (erreur != null) {
            ll_error.setVisibility(View.VISIBLE);
            tv_error.setText(erreur.getUserMessage());
        }
    }




    /* ---------------------------------
    //      AsyncTask
    // -------------------------------- */

    public class WSAsyncTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<CategoryBean> result = null;
        private TechnicalException technicalException;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                result = WsUtils.getCatalogue();
                if (result == null) {
                    throw new TechnicalException("Le catalogue est nulle");
                }
            }
            catch (TechnicalException e) {
                this.technicalException = e;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaitingMessage = true;
            refreshScreen();
        }

        @Override
        protected void onPostExecute(Void le) {
            showWaitingMessage = false;

            if (technicalException != null) {
                erreur = technicalException;
                erreur.printStackTrace();
            }
            else {
                erreur = null;
                categoryList.clear();
                categoryList.addAll(result);
                categoryAdapter.notifyDataSetChanged();
            }

            refreshScreen();
        }
    }
}
