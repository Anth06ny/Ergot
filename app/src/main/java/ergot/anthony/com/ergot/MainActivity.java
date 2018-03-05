package ergot.anthony.com.ergot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import ergot.anthony.com.ergot.controler.admin.GestionCommandeActivity;
import ergot.anthony.com.ergot.controler.commander.CommanderActivity;
import ergot.anthony.com.ergot.controler.historique.HstoriqueCommandActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Composants
    private AppCompatButton btCommander, btYAller, bthisto;

    //Url
    private static final String URL_ERGOT = "https://www.google.com/maps/place/L'Ergot/@43.5841389,1.4282618,18.58z/data=!4m12!1m6!3m5!1s0x12aebb9943811f61:0x50fa05dc0fe6d00f!2sL'Ergot!8m2!3d43.5846299!4d1.4282615!3m4!1s0x12aebb9943811f61:0x50fa05dc0fe6d00f!8m2!3d43.5846299!4d1.4282615";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btCommander = findViewById(R.id.btCommander);
        btYAller = findViewById(R.id.btYAller);
        bthisto = findViewById(R.id.bthisto);

        if (MyApplication.isAdminMode()) {
            btCommander.setVisibility(View.GONE);
            bthisto.setText(R.string.gestion_command);
        }

        btYAller.setOnClickListener(this);
        btCommander.setOnClickListener(this);
        bthisto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (btCommander == v) {
            startActivity(new Intent(this, CommanderActivity.class));
        }
        else if (btYAller == v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_ERGOT));
            startActivity(browserIntent);
        }
        else if (bthisto == v) {
            if (MyApplication.isAdminMode()) {
                startActivity(new Intent(this, GestionCommandeActivity.class));
            }
            else {
                startActivity(new Intent(this, HstoriqueCommandActivity.class));
            }
        }
    }
}
