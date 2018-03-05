package ergot.anthony.com.ergot.controler.panier;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
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
import ergot.anthony.com.ergot.model.bean.Statut;
import ergot.anthony.com.ergot.model.bean.StatutAnnulation;
import ergot.anthony.com.ergot.model.bean.sendbean.SelectProductBean;
import ergot.anthony.com.ergot.model.ws.WSUtilsAdmin;
import ergot.anthony.com.ergot.model.ws.WsUtils;
import ergot.anthony.com.ergot.utils.AlertDialogUtils;
import ergot.anthony.com.ergot.utils.SharedPreferenceUtils;

public class PanierActivity extends MotherActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, CommandeAdapter.OnCommandListener {

    private static final int REQUEST_CODE_EMAIL = 1;
    public static final String COMMANDE_EXTRA = "COMMANDE_EXTRA";

    //Composants graphique
    private TextView tv_time;
    private Button bt_modifier;
    private EditText et_rem;
    private RecyclerView rv;
    private EditText etPhone;
    private TextView tvEmail;
    private CardView cvIdentification, cvIdentifier, cv_when;
    private Button btIdentifier;
    private ImageView iv_logout;
    private View rl_root;
    private ProgressDialog progressDialog;
    private Button bt_annuler_commande;

    //Gestion de la date
    private Calendar calendar;
    private SimpleDateFormat formatCompl = new SimpleDateFormat("EEE d MMM 'à' HH:mm");
    private SimpleDateFormat formatheure = new SimpleDateFormat("HH:mm");

    //Data
    private CommandeBean commandeBean;
    private String tokenEmail;
    private boolean isCurrentCommand;//Est ce qu'on est en mode commande en cours, ou en mode afficher une ancienne commande

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
        cv_when = findViewById(R.id.cv_when);
        bt_annuler_commande = findViewById(R.id.bt_annuler_commande);

        //Est ce qu'on recoit une commande à afficher, donc on est en mode affichage histoiriue commande
        commandeBean = (CommandeBean) getIntent().getSerializableExtra(COMMANDE_EXTRA);
        bt_annuler_commande.setVisibility(View.GONE);
        if (commandeBean != null) {
            isCurrentCommand = false;
            et_rem.setEnabled(false);
            //Si la commande n'a  pas été annulé ou livré on affiche le bouton d'annulation
            if (commandeBean.getStatut() < Statut.STATUS_DELIVERY) {
                bt_annuler_commande.setVisibility(View.VISIBLE);
            }
            hideFoot();
        }
        else {
            //Sinon on affiche la commande en cours
            commandeBean = MyApplication.getCommandeBean();
            isCurrentCommand = true;
        }

        bt_modifier.setOnClickListener(this);
        btIdentifier.setOnClickListener(this);
        iv_logout.setOnClickListener(this);
        bt_annuler_commande.setOnClickListener(this);

        //RecylcerView
        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        commandeAdapter = new CommandeAdapter(commandeBean.getCompositionCommande(), this, isCurrentCommand);
        rv.setAdapter(commandeAdapter);

        calendar = Calendar.getInstance();
        if (commandeBean.getDateCommande() != 0) {
            calendar.setTimeInMillis(commandeBean.getDateCommande());
        }

        //Le numéro de téléphone de l'appareil  + permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_NUMBERS}, 1);
            Toast.makeText(this, R.string.tv_number_permission_exp, Toast.LENGTH_LONG).show();
        }
        else {
            TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            etPhone.setText(tMgr.getLine1Number());
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
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
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
            if (panierVersion) {
                if (MyApplication.getCommandeBean().getCompositionCommande().isEmpty()) {
                    Toast.makeText(this, R.string.no_produit_to_send, Toast.LENGTH_SHORT).show();
                    return;
                }

                MyApplication.getCommandeBean().getUser().setEmail(tvEmail.getText().toString());
                MyApplication.getCommandeBean().getUser().setTelephone(etPhone.getText().toString());
                MyApplication.getCommandeBean().setRemarque(et_rem.getText().toString());
                //Tout est on on lance la validation
                new PanierActivity.WSAsyncTask().execute();
            }
            else {
                startActivity(new Intent(this, PanierActivity.class));
            }
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
        else if (v == bt_annuler_commande) {
            if (MyApplication.isAdminMode()) {
                //L'admin annule une comamnde
                AlertDialogUtils.showSelectCancelReasonDialog(this, new AlertDialogUtils.SelectCancelReasonListener() {
                    @Override
                    public void onCancelReasonSelected(StatutAnnulation statutAnnulation) {
                        commandeBean.getUser().setEmail(tokenEmail);
                        new WSCancelCommande(commandeBean, statutAnnulation).execute();
                    }
                });
            }
            else {
                //Un client annule sa commande
                AlertDialogUtils.showConfirmCancelCommandDialog(this, R.string.dialog_supp_commande, R.string.bt_cancel_order, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commandeBean.getUser().setEmail(tokenEmail);
                        new WSCancelCommande(commandeBean, StatutAnnulation.ADMIN).execute();
                    }
                });
            }
        }
        else {
            super.onClick(v);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            tokenEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            SharedPreferenceUtils.saveEmail(tokenEmail);
            refreshScreen();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            etPhone.setText(tMgr.getLine1Number());
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
    public void onRemoveProductClic(SelectProductBean selectProductBean) {
        int position = commandeBean.getCompositionCommande().indexOf(selectProductBean);
        commandeBean.getCompositionCommande().remove(selectProductBean);
        commandeAdapter.notifyItemRemoved(position);

        refreshScreen();
        refreshFoot();
    }

    /* ---------------------------------
    // Private
    // -------------------------------- */
    private void refreshScreen() {
        Date date = new Date();

        if (isCurrentCommand) {
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
        else {
            //Mode historique commande on n'affiche que le detail
            cv_when.setVisibility(View.GONE);
            cvIdentification.setVisibility(View.GONE);
            cvIdentifier.setVisibility(View.GONE);
        }
    }

    /* ---------------------------------
    //  AsyncTask
    // -------------------------------- */

    public class WSAsyncTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<CommandeBean> result = null;
        private TechnicalException technicalException;

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
                //La commande a été validé, on la supprime de la commande en cours
                MyApplication.newCommande();
                //On redirige sur l'écran d'Historique
                Intent intent = new Intent(PanierActivity.this, HstoriqueCommandActivity.class);
                intent.putExtra(HstoriqueCommandActivity.LIST_COMMANDE_EXTRA, new Gson().toJson(result));
                startActivity(intent);
                finish();
            }
        }
    }

    /**
     * Envoie la demande d'annulation d'une commande
     */
    public class WSCancelCommande extends AsyncTask<Void, Void, Void> {

        private TechnicalException technicalException;
        private CommandeBean commandeBean;
        private StatutAnnulation statutAnnulation;

        public WSCancelCommande(CommandeBean commandeBean, StatutAnnulation statutAnnulation) {
            this.commandeBean = commandeBean;
            this.statutAnnulation = statutAnnulation;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                WSUtilsAdmin.updateCommandStatut(commandeBean, Statut.STATUS_CANCEL, 0, statutAnnulation.getValue());
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
        }

        @Override
        protected void onPostExecute(Void le) {
            progressDialog.dismiss();

            if (technicalException != null) {
                technicalException.printStackTrace();
                Snackbar snackbar = Snackbar.make(rl_root, technicalException.getUserMessage(), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
            else {
                Toast.makeText(PanierActivity.this, R.string.tv_command_canceled, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            refreshScreen();
        }
    }
}
