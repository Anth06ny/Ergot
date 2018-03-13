package ergot.anthony.com.ergot.controler.historique;

import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.controler.commander.MotherActivity;
import ergot.anthony.com.ergot.controler.panier.PanierActivity;
import ergot.anthony.com.ergot.exception.TechnicalException;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.model.ws.WsUtils;
import ergot.anthony.com.ergot.utils.SharedPreferenceUtils;

public class HstoriqueCommandActivity extends MotherActivity implements View.OnClickListener, CommandeAdapter.OnComandeClicListener {

    public static final String LIST_COMMANDE_EXTRA = "LIST_COMMANDE_EXTRA";
    private static final int REQUEST_CODE_EMAIL = 1;
    private static final int MENU_REFRESH = 2;

    //Composant graphique
    private TextView tv_email, tv_error;
    private Button btIdentifier;
    private Button bt_refresh;
    private RecyclerView rv;
    private View ll_error;
    private TextView tv_wait;
    private TextView tv_info, tv_no_commande;
    private View ll_connected;
    private ImageView iv_logout;

    //View
    private CommandeAdapter commandeAdapter;
    private boolean showWaitingMessage;
    private TechnicalException erreur;

    //Data
    private ArrayList<CommandeBean> commandeBeanArrayList;
    private String tokenEmail;
    boolean startWithData = false; //est ce qu'on charge la page avec des données dans l'extra

    //Outils
    private AsyncTask<Void, Void, Void> asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hstorique_command);

        tv_email = findViewById(R.id.tv_email);
        btIdentifier = findViewById(R.id.btIdentifier);
        tv_error = findViewById(R.id.tv_error);
        ll_error = findViewById(R.id.ll_error);
        tv_wait = findViewById(R.id.tv_wait);
        tv_info = findViewById(R.id.tv_info);
        iv_logout = findViewById(R.id.iv_logout);
        tv_no_commande = findViewById(R.id.tv_no_commande);
        ll_connected = findViewById(R.id.ll_connected);
        bt_refresh = findViewById(R.id.bt_refresh);
        rv = findViewById(R.id.rv);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        btIdentifier.setOnClickListener(this);
        bt_refresh.setOnClickListener(this);
        iv_logout.setOnClickListener(this);

        ll_error.setVisibility(View.GONE);

        commandeBeanArrayList = new ArrayList<>();
        commandeAdapter = new CommandeAdapter(commandeBeanArrayList, this);
        rv.setAdapter(commandeAdapter);

        tokenEmail = SharedPreferenceUtils.getSaveEmail();

        //Est ce qu'on a rejoint l'écran avec une liste de commande à afficher ?
        //On regarde si on a une liste de commande
        String json = getIntent().getStringExtra(LIST_COMMANDE_EXTRA);
        if (StringUtils.isNotBlank(json)) {
            startWithData = true;
            commandeBeanArrayList.addAll((Collection<? extends CommandeBean>) new Gson().fromJson(json, new TypeToken<ArrayList<CommandeBean>>() {
            }.getType()));

            //On indique que la commande à été ajouté
            Snackbar snackbar = Snackbar.make(findViewById(R.id.root), R.string.order_sucess, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();
            refreshScreen();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            tokenEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            SharedPreferenceUtils.saveEmail(tokenEmail);
            refreshData();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Si on n'a pas recu de liste de commande en arrivant sur l'écran on lance la requete de recup commande
        //Si jamais on a modifier la comamnde dans le panier
        if (!startWithData) {
            refreshData();
        }
        startWithData = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_REFRESH, 0, "").setIcon(R.mipmap.ic_refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_REFRESH) {
            refreshData();
        }

        return super.onOptionsItemSelected(item);
    }

    /* ---------------------------------
    // click
    // -------------------------------- */

    @Override
    public void onClick(View v) {
        if (v == btIdentifier) {
            try {
                Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
                startActivityForResult(intent, REQUEST_CODE_EMAIL);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(this, R.string.identification_impossible, Toast.LENGTH_SHORT).show();
            }
        }
        else if (v == iv_logout) {
            //On supprime l'email sauvegarder
            tokenEmail = null;
            SharedPreferenceUtils.saveEmail("");
            refreshData();
        }
        else if (v == bt_refresh) {
            refreshData();
        }
        else {
            super.onClick(v);
        }
    }

      /* ---------------------------------
    // CallBack commande
    // -------------------------------- */

    @Override
    public void showDetailCommandClick(CommandeBean commandeBean) {
        Intent intent = new Intent(this, PanierActivity.class);
        intent.putExtra(PanierActivity.COMMANDE_EXTRA, commandeBean);
        startActivity(intent);
    }



     /* ---------------------------------
    // public
    // -------------------------------- */


    /* ---------------------------------
    // private
    // -------------------------------- */

    private void refreshScreen() {

        //Identification
        if (StringUtils.isBlank(tokenEmail)) {
            btIdentifier.setVisibility(View.VISIBLE);
            ll_connected.setVisibility(View.GONE);
            tv_info.setVisibility(View.VISIBLE);
        }
        else {
            btIdentifier.setVisibility(View.GONE);
            ll_connected.setVisibility(View.VISIBLE);
            tv_email.setText(tokenEmail);
            tv_info.setVisibility(View.GONE);
        }

        //Message d'erreur
        tv_wait.setVisibility(View.GONE);
        ll_error.setVisibility(View.GONE);
        if (showWaitingMessage) {
            tv_wait.setVisibility(View.VISIBLE);
        }
        else if (erreur != null) {
            ll_error.setVisibility(View.VISIBLE);
            tv_error.setText(erreur.getUserMessage());
        }

        //RecyclerView ou texte , liste vide
        if (commandeBeanArrayList.isEmpty()) {
            tv_no_commande.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }
        else {
            tv_no_commande.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Lance l'AsyncTask
     */
    private void refreshData() {

        if (asyncTask == null || asyncTask.getStatus() == AsyncTask.Status.FINISHED) {
            asyncTask = new WSGetHistory();
            asyncTask.execute();
        }
        else {
            Toast.makeText(this, R.string.reqEnCours, Toast.LENGTH_SHORT).show();
        }
    }



     /* ---------------------------------
    //      AsyncTask
    // -------------------------------- */

    public class WSGetHistory extends AsyncTask<Void, Void, Void> {

        private ArrayList<CommandeBean> result = null;
        private TechnicalException technicalException;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                result = WsUtils.getHistory();
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
                commandeBeanArrayList.clear();
                commandeBeanArrayList.addAll(result);
                commandeAdapter.notifyDataSetChanged();
            }

            refreshScreen();
        }
    }
}
