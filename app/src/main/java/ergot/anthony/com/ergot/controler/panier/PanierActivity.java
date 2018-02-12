package ergot.anthony.com.ergot.controler.panier;

import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.controler.commander.MotherActivity;
import ergot.anthony.com.ergot.controler.historique.HstoriqueCommandActivity;
import ergot.anthony.com.ergot.exception.TechnicalException;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.model.bean.ProductBean;
import ergot.anthony.com.ergot.model.bean.SuppBean;
import ergot.anthony.com.ergot.model.ws.WsUtils;
import ergot.anthony.com.ergot.utils.SharedPreferenceUtils;

public class PanierActivity extends MotherActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, CommandeAdapter.OnCommandListener {

    private static final int REQUEST_CODE_EMAIL = 1;

    //Composants graphique
    private TextView tv_time;
    private Button bt_modifier;
    private EditText et_rem;
    private RecyclerView rv;
    private EditText etPhone;
    private TextView tvEmail;
    private CardView cvIdentification, cvIdentifier;
    private Button btIdentifier;
    private ImageView iv_logout;
    private View rl_root;

    //Gestion de la date
    private Calendar calendar;
    private SimpleDateFormat formatCompl = new SimpleDateFormat("EEE d MMM 'à' HH:mm");
    private SimpleDateFormat formatheure = new SimpleDateFormat("HH:mm");

    //Data
    private CommandeBean commandeBean;
    private String tokenEmail;

    private CommandeAdapter commandeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        rv = findViewById(R.id.rv);
        tv_time = findViewById(R.id.tv_time);
        bt_modifier = findViewById(R.id.bt_modifier);
        et_rem = findViewById(R.id.et_rem);
        etPhone = findViewById(R.id.etPhone);
        cvIdentification = findViewById(R.id.cvIdentification);
        cvIdentifier = findViewById(R.id.cvIdentifier);
        etPhone = findViewById(R.id.etPhone);
        tvEmail = findViewById(R.id.tvEmail);
        btIdentifier = findViewById(R.id.btIdentifier);
        iv_logout = findViewById(R.id.iv_logout);
        rl_root = findViewById(R.id.rl_root);

        commandeBean = MyApplication.getCommandeBean();

        bt_modifier.setOnClickListener(this);
        btIdentifier.setOnClickListener(this);
        iv_logout.setOnClickListener(this);

        //RecylcerView
        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        commandeAdapter = new CommandeAdapter(commandeBean.getProductList(), this);
        rv.setAdapter(commandeAdapter);

        calendar = Calendar.getInstance();
        if (commandeBean.getDateCommande() != 0) {
            calendar.setTimeInMillis(commandeBean.getDateCommande());
        }

        //Remarque
        et_rem.setText(commandeBean.getRemarque());

        tokenEmail = SharedPreferenceUtils.getSaveEmail();

        refreshScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        commandeBean.setRemarque(et_rem.getText().toString());
    }

    @Override
    public void onClick(View v) {
        if (v == bt_modifier) {
            //Création de la fenêtre
            new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
        else if (v == bt_commande) {

            MyApplication.getCommandeBean().setEmail(tvEmail.getText().toString());
            MyApplication.getCommandeBean().setTelephone(etPhone.getText().toString());
            MyApplication.getCommandeBean().setRemarque(et_rem.getText().toString());
            //Tout est on on lance la validation
            new WSAsyncTask().execute();
        }
        else if (v == btIdentifier) {
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
            refreshScreen();
        }
        else {
            super.onClick(v);
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



    /* ---------------------------------
    // CallBack
    // -------------------------------- */

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        commandeBean.setDateCommande(calendar.getTime().getTime());

        refreshScreen();

        new TimePickerDialog(this, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        commandeBean.setDateCommande(calendar.getTime().getTime());

        refreshScreen();
    }

    @Override
    public void onRemoveProductClic(Pair<ProductBean, SuppBean> produitAndSupp) {
        int position = commandeBean.getProductList().indexOf(produitAndSupp);
        commandeBean.getProductList().remove(produitAndSupp);
        commandeAdapter.notifyItemRemoved(position);

        refreshScreen();
        refreshFoot();
    }

    /* ---------------------------------
    // Private
    // -------------------------------- */
    private void refreshScreen() {
        Date date = new Date();
        if (StringUtils.isBlank(tokenEmail)) {
            cvIdentification.setVisibility(View.VISIBLE);
            cvIdentifier.setVisibility(View.GONE);
        }
        else {
            cvIdentification.setVisibility(View.GONE);
            cvIdentifier.setVisibility(View.VISIBLE);
        }
        tvEmail.setText(tokenEmail);

        //Si c'est dans moins de 30min c'est au plus tôt
        {
            if (calendar.getTimeInMillis() - date.getTime() < 1800000) {
                tv_time.setText(R.string.au_plus_tot);
            }
            //Si c'est aujourd'hui
            else if (DateUtils.isSameDay(calendar.getTime(), date)) {
                tv_time.setText("Aujourd'hui à " + formatheure.format(calendar.getTime()));
            }
            else {
                tv_time.setText(formatCompl.format(calendar.getTime()));
            }
        }
    }

    /* ---------------------------------
    //  AsyncTask
    // -------------------------------- */

    public class WSAsyncTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<CommandeBean> result = null;
        private TechnicalException technicalException;
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                result = WsUtils.sendCommande(MyApplication.getCommandeBean());
            }
            catch (TechnicalException e) {
                this.technicalException = e;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(PanierActivity.this, "", "Envoie de la commande en cours....");
            refreshScreen();
        }

        @Override
        protected void onPostExecute(Void le) {
            progressDialog.dismiss();

            if (technicalException != null) {
                // erreur = technicalException;
                technicalException.printStackTrace();
                Snackbar snackbar = Snackbar.make(rl_root, technicalException.getUserMessage(), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
            else {
                //On redirige sur l'écran d'Historique
                Intent intent = new Intent(PanierActivity.this, HstoriqueCommandActivity.class);
                intent.putExtra(HstoriqueCommandActivity.LIST_COMMANDE_EXTRA, new Gson().toJson(result));
                startActivity(intent);
            }
        }
    }
}
