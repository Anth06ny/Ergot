package ergot.anthony.com.ergot.controler.admin;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.controler.historique.CommandeAdapter;
import ergot.anthony.com.ergot.controler.panier.PanierActivity;
import ergot.anthony.com.ergot.exception.TechnicalException;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.model.bean.Status;
import ergot.anthony.com.ergot.model.ws.WSUtilsAdmin;
import ergot.anthony.com.ergot.model.ws.WsUtils;
import ergot.anthony.com.ergot.utils.Utils;

public class GestionCommandeActivity extends AppCompatActivity implements CommandeAdapter.OnComandeClicListenerAdmin, View.OnClickListener {

    private static final int MENU_REFRESH = 2;
    private static final int REFRESH_TIME = 30000;

    //Composant graphique
    private TextView tv_error;
    private RecyclerView rv;
    private View ll_error;
    private TextView tv_wait;
    private TextView tv_no_commande;
    private Button bt_refresh;

    //View
    private CommandeAdapter commandeAdapter;
    private boolean showWaitingMessage;
    private TechnicalException erreur;

    //Data
    private ArrayList<CommandeBean> commandeBeanArrayList;
    private boolean firstCall; // Permet d'identifier le1er chargement ,pour ne mettre le son que pour les ajout ulterieur

    //Outils
    private AsyncTask<Void, Void, Void> asyncTask, updateCommandAT;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_commande);

        tv_error = findViewById(R.id.tv_error);
        ll_error = findViewById(R.id.ll_error);
        tv_wait = findViewById(R.id.tv_wait);
        bt_refresh = findViewById(R.id.bt_refresh);
        tv_no_commande = findViewById(R.id.tv_no_commande);
        rv = findViewById(R.id.rv);

        bt_refresh.setOnClickListener(this);

        firstCall = true;

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        ll_error.setVisibility(View.GONE);

        handler = new Handler();

        commandeBeanArrayList = new ArrayList<>();
        commandeAdapter = new CommandeAdapter(commandeBeanArrayList, this);
        rv.setAdapter(commandeAdapter);
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

    @Override
    protected void onStart() {
        super.onStart();
        //Récupère les info
        refreshData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //En quittant l'écran on retire la relance de rafraichissement
        handler.removeCallbacksAndMessages(null);
    }

    /* ---------------------------------
    // Click
    // -------------------------------- */

    @Override
    public void onClick(View v) {
        if (v == bt_refresh) {
            refreshData();
        }
    }

    /* ---------------------------------
    // Callback adapter
    // -------------------------------- */

    @Override
    public void showDetailCommandClick(CommandeBean commandeBean) {
        Intent intent = new Intent(this, PanierActivity.class);
        intent.putExtra(PanierActivity.COMMANDE_EXTRA, commandeBean);
        startActivity(intent);
    }

    @Override
    public void cancelCommandClick(CommandeBean commandeBean) {

    }

    @Override
    public void onAcceptCommand(final CommandeBean commandeBean) {
        //Annulation Comande
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //ICI on envoie l'ecart en milliseconde, c'est le serveur qui calculera la date de prevision
                updateCommande(commandeBean, Status.STATUS_SENDACCEPTED, hourOfDay * 3600000 + minute * 60000);
            }
        }, 0, 20, true);
        timePickerDialog.setTitle(getString(R.string.comande_ready_in));
        timePickerDialog.show();
    }

    @Override
    public void onReadyCommand(CommandeBean commandeBean) {
        updateCommande(commandeBean, Status.STATUS_READY, -1);
    }

    @Override
    public void onSendCommandClick(CommandeBean commandeBean) {
        updateCommande(commandeBean, Status.STATUS_DELIVERY, -1);
    }

      /* ---------------------------------
    // private
    // -------------------------------- */

    private void refreshScreen() {

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
    private synchronized void refreshData() {

        if (asyncTask == null || asyncTask.getStatus() == AsyncTask.Status.FINISHED) {
            //On retire les eventuelles message
            handler.removeCallbacksAndMessages(null);
            asyncTask = new WSGetHistory();
            asyncTask.execute();
        }
        else {
            Toast.makeText(this, R.string.reqEnCours, Toast.LENGTH_SHORT).show();
        }
    }

    private synchronized void updateCommande(CommandeBean send, int newStatut, long datePrevision) {
        if (updateCommandAT == null || updateCommandAT.getStatus() == AsyncTask.Status.FINISHED) {
            //On retire les eventuelles message
            updateCommandAT = new WSUpdateCommande(send, newStatut, datePrevision);
            updateCommandAT.execute();
        }
        else {
            Toast.makeText(this, R.string.reqEnCours, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Méthode qui parcourt la liste de l'appli avec la liste recu pour la mettre à jour tout en ajoutant les animation d'insertion et de modification
     *
     * @param result
     */
    private void updateList(ArrayList<CommandeBean> result) {

        //On a une liste vide
        if (commandeBeanArrayList.isEmpty()) {
            commandeBeanArrayList.addAll(result);
            commandeAdapter.notifyItemRangeInserted(0, result.size());
            return;
        }

        //On parcourt les 2 liste
        for (int iAppli = 0, iServeur = 0; iServeur < result.size(); ) {
            CommandeBean cbServeur = result.get(iServeur);//Commande du serveur
            CommandeBean cbAppli = commandeBeanArrayList.get(iAppli);//Commande de l'appli

            //C'est la même
            if (cbServeur.getId() == cbAppli.getId()) {
                //on retire et on remplace par celle la
                commandeBeanArrayList.remove(iAppli);
                commandeBeanArrayList.add(iAppli, cbServeur);
                commandeAdapter.notifyItemChanged(iAppli);
                iAppli++;
                iServeur++;
            }
            //Si on rencontre une commande plus ancienne on place la commande à cette position
            else if (cbServeur.getDateCommande() >= cbAppli.getDateCommande()) {
                commandeBeanArrayList.add(iAppli, cbServeur);
                commandeAdapter.notifyItemInserted(iAppli);
                iAppli++;
                iServeur++;
                //ON indique qu'une commande est arrivée
                if (!firstCall) {
                    Utils.vibrate();
                    Utils.sound();
                }
            }
            //Date de la comande du serveur inferieur, on continue le parcours
            else {
                iAppli++;
            }
        }

        firstCall = false;
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
                updateList(result);
            }

            //On relance l'actualisation dans 30seconde
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshData();
                }
            }, REFRESH_TIME);

            refreshScreen();
        }
    }

    /**
     * Update du statut de la comande
     */
    public class WSUpdateCommande extends AsyncTask<Void, Void, Void> {

        private int newStatut;
        private long datePrevision;
        private CommandeBean send;
        private CommandeBean result = null;
        private TechnicalException technicalException;

        public WSUpdateCommande(CommandeBean send, int newStatut, long datePrevision) {
            this.newStatut = newStatut;
            this.send = send;
            this.datePrevision = datePrevision;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                result = WSUtilsAdmin.updateCommandStatut(send, newStatut, datePrevision);
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
                //On acutalise la commande avec celle recu
                int index = commandeBeanArrayList.indexOf(send);
                commandeBeanArrayList.remove(index);
                commandeBeanArrayList.add(result);
                commandeAdapter.notifyItemChanged(index);
            }

            refreshScreen();
        }
    }
}
