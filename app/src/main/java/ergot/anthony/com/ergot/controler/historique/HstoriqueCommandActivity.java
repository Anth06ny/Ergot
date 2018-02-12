package ergot.anthony.com.ergot.controler.historique;

import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.AccountPicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.controler.commander.MotherActivity;
import ergot.anthony.com.ergot.exception.TechnicalException;
import ergot.anthony.com.ergot.model.bean.CategoryBean;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.model.ws.WsUtils;
import ergot.anthony.com.ergot.utils.GlideApp;
import ergot.anthony.com.ergot.utils.SharedPreferenceUtils;

public class HstoriqueCommandActivity extends MotherActivity implements View.OnClickListener, CommandeAdapter.OnComandeClicListener {

    public static final String LIST_COMMANDE_EXTRA = "LIST_COMMANDE_EXTRA";
    private static final int REQUEST_CODE_EMAIL = 1;
    private static final int MENU_REFRESH = 2;

    //Composant graphique
    private TextView tv_email, tv_error;
    private CircleImageView profile_image;
    private Button btIdentifier;
    private RecyclerView rv;
    private Button bt_refresh;
    private View ll_error;
    private TextView tv_wait;
    private View ll_connected;

    //View
    private CommandeAdapter commandeAdapter;
    private boolean showWaitingMessage;
    private TechnicalException erreur;

    //Data
    private ArrayList<CommandeBean> commandeBeanArrayList;
    private String tokenEmail;

    //Outils
    private WSAsyncTask wsAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hstorique_command);

        tv_email = findViewById(R.id.tv_email);
        profile_image = findViewById(R.id.profile_image);
        btIdentifier = findViewById(R.id.btIdentifier);
        tv_error = findViewById(R.id.tv_error);
        bt_refresh = findViewById(R.id.bt_refresh);
        ll_error = findViewById(R.id.ll_error);
        tv_wait = findViewById(R.id.tv_wait);
        ll_connected = findViewById(R.id.ll_connected);
        rv = findViewById(R.id.rv);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        bt_refresh.setOnClickListener(this);
        btIdentifier.setOnClickListener(this);

        ll_error.setVisibility(View.GONE);



        tokenEmail = SharedPreferenceUtils.getSaveEmail();

        //Lance l'ASyncTask qui ensuite rafraichira l'écran
        //On regarde si on a une liste de commande
        if (getIntent() != null) {
            String json = getIntent().getStringExtra(LIST_COMMANDE_EXTRA);
            commandeBeanArrayList = new Gson().fromJson(json, new TypeToken<ArrayList<CategoryBean>>() {
            }.getType());

            //On indique que la commande à été ajouté
            Snackbar snackbar = Snackbar.make(findViewById(R.id.root), R.string.order_sucess, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();

            refreshScreen();
        }
        if (commandeBeanArrayList == null) {
            commandeBeanArrayList = new ArrayList<>();
        }

        commandeAdapter = new CommandeAdapter(commandeBeanArrayList, this);
        rv.setAdapter(commandeAdapter);


        //Récupérer la photo
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null && acct.getPhotoUrl() != null) {

            int color = getResources().getColor(R.color.colorPrimary);
            Drawable waitIcon = getResources().getDrawable(R.drawable.ic_hourglass_empty_black_48dp);
            waitIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN);

            color = getResources().getColor(R.color.error_color);
            Drawable error_icon = getResources().getDrawable(R.drawable.ic_highlight_off_black_48dp);
            error_icon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            GlideApp.with(this).load(acct.getPhotoUrl()).placeholder(waitIcon).error(error_icon).fitCenter().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into
                    (profile_image);
        }
        else {
            profile_image.setVisibility(View.INVISIBLE);
        }

        if (commandeBeanArrayList.isEmpty()) {
            refreshData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            tokenEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            SharedPreferenceUtils.saveEmail(tokenEmail);
            refreshScreen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_REFRESH, 0, "").setIcon(R.mipmap.ic_refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_REFRESH) {

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
    }

      /* ---------------------------------
    // CallBack commande
    // -------------------------------- */

    @Override
    public void showDetailCommand(CommandeBean commandeBean) {

    }

    @Override
    public void cancelCommand(CommandeBean commandeBean) {

    }

    /* ---------------------------------
    // private
    // -------------------------------- */

    private void refreshScreen() {

        if (StringUtils.isBlank(tokenEmail)) {
            btIdentifier.setVisibility(View.VISIBLE);
            ll_connected.setVisibility(View.GONE);
        }
        else {
            btIdentifier.setVisibility(View.GONE);
            ll_connected.setVisibility(View.VISIBLE);
            tv_email.setText(tokenEmail);
        }

        tv_wait.setVisibility(View.GONE);
        ll_error.setVisibility(View.GONE);

        if (showWaitingMessage) {
            tv_wait.setVisibility(View.VISIBLE);
        }
        else if (erreur != null) {
            ll_error.setVisibility(View.VISIBLE);
            tv_error.setText(erreur.getUserMessage());
        }
    }

    /**
     * Lance l'AsyncTask
     */
    private void refreshData() {

        if (wsAsyncTask == null || wsAsyncTask.getStatus() == AsyncTask.Status.FINISHED) {
            wsAsyncTask = new WSAsyncTask();
            wsAsyncTask.execute();
        }
    }

     /* ---------------------------------
    //      AsyncTask
    // -------------------------------- */

    public class WSAsyncTask extends AsyncTask<Void, Void, Void> {

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
