package ergot.anthony.com.ergot.panier;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ergot.anthony.com.ergot.MyApplication;
import ergot.anthony.com.ergot.R;
import ergot.anthony.com.ergot.commander.MotherActivity;
import ergot.anthony.com.ergot.model.bean.CommandeBean;
import ergot.anthony.com.ergot.model.bean.ProductBean;

public class PanierActivity extends MotherActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, CommandeAdapter.OnCommandListener {

    private TextView tv_time;
    private Button bt_modifier;
    private EditText et_rem;
    private RecyclerView rv;
    private EditText etPhone, etEmail;
    //Gestion de la date
    private Calendar calendar;
    private SimpleDateFormat formatCompl = new SimpleDateFormat("EEE d MMM 'à' HH:mm");
    private SimpleDateFormat formatheure = new SimpleDateFormat("HH:mm");

    private CommandeBean commandeBean;

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
        etEmail = findViewById(R.id.etEmail);

        commandeBean = MyApplication.getCommandeBean();

        bt_modifier.setOnClickListener(this);

        //RecylcerView
        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        commandeAdapter = new CommandeAdapter(commandeBean.getProductList(), this);
        rv.setAdapter(commandeAdapter);

        calendar = Calendar.getInstance();
        if (commandeBean.getDateCommande() != null) {
            calendar.setTime(commandeBean.getDateCommande());
            commandeBean.setDateCommande(calendar.getTime());
        }

        //Remarque
        et_rem.setText(commandeBean.getRemarque());

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
            if (StringUtils.isBlank(etEmail.getText().toString()) && StringUtils.isBlank(etPhone.getText().toString())) {
                Toast.makeText(this, R.string.nophone_or_email, Toast.LENGTH_LONG).show();
            }

        }
        else {
            super.onClick(v);
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
        commandeBean.setDateCommande(calendar.getTime());

        refreshScreen();

        new TimePickerDialog(this, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        commandeBean.setDateCommande(calendar.getTime());

        refreshScreen();
    }

    @Override
    public void onDeleteComande(Pair<ProductBean, ArrayList<ProductBean>> command) {
        int position = commandeBean.getProductList().indexOf(command);
        commandeBean.getProductList().remove(command);
        commandeAdapter.notifyItemRemoved(position);

        refreshScreen();
        refreshFoot();
    }

    /* ---------------------------------
    // Private
    // -------------------------------- */
    private void refreshScreen() {
        Date date = new Date();

        //Si c'est dans moins de 30min c'est au plus tôt
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
