package ergot.anthony.com.ergot.commander;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.model.bean.CategoryBean;
import ergot.anthony.com.ergot.model.bean.TechnicalException;

public class CommanderActivity extends MotherActivity implements View.OnClickListener {

    //Composant graphique
    private RecyclerView rv;
    private TextView tv_wait, tv_error;
    private Button bt_refresh;
    private View ll_error;

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

        categoryAdapter = new CategoryAdapter(this, categoryList);
        rv.setAdapter(categoryAdapter);

        bt_refresh.setOnClickListener(this);

        //On charge les données
        new WSAsyncTask().execute();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == bt_refresh) {
            new WSAsyncTask().execute();
        }
    }

    /* ---------------------------------
    // private
    // -------------------------------- */

    /**
     * Gestion de l'exception
     *
     * @param le
     */
    private void updateError(TechnicalException le) {

        le.printStackTrace();

        if (StringUtils.isNotBlank(le.getUserMessage())) {
            showErrorMessage(le.getUserMessage());
        }
        else {
            showErrorMessage(getString(R.string.generic_error));
        }
    }

    /**
     * Gere l'affichage du bandeau d'erreur
     *
     * @param message
     */
    private void showErrorMessage(String message) {
        if (message == null) {
            ll_error.setVisibility(View.INVISIBLE);
        }
        else {
            ll_error.setVisibility(View.VISIBLE);
            tv_error.setText(message);
        }
    }

    /**
     * Gestion du bandeau d'attente
     *
     * @param show
     */
    private void showWaintingMessage(boolean show) {
        if (show) {
            tv_wait.setVisibility(View.VISIBLE);
        }
        else {
            tv_wait.setVisibility(View.INVISIBLE);
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

            //            try {
            //
            //            }
            //            catch (TechnicalException e) {
            //                technicalException = e;
            //            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaintingMessage(true);
        }

        @Override
        protected void onPostExecute(Void le) {
            showWaintingMessage(false);

            if (le != null) {
                updateError(technicalException);
            }
            else {
                categoryList.clear();
                categoryList.addAll(result);
                categoryAdapter.notifyDataSetChanged();
            }
        }
    }
}
