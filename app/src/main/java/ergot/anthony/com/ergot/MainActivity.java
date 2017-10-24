package ergot.anthony.com.ergot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import ergot.anthony.com.ergot.model.ws.WsUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Composants
    private AppCompatButton btCommander, btYAller;

    //Url
    private static final String URL_ERGOT = "https://www.google.com/maps/place/L'Ergot/@43.5841389,1.4282618,18.58z/data=!4m12!1m6!3m5!1s0x12aebb9943811f61:0x50fa05dc0fe6d00f!2sL'Ergot!8m2!3d43.5846299!4d1.4282615!3m4!1s0x12aebb9943811f61:0x50fa05dc0fe6d00f!8m2!3d43.5846299!4d1.4282615";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btCommander = (AppCompatButton) findViewById(R.id.btCommander);
        btYAller = (AppCompatButton) findViewById(R.id.btYAller);

        btYAller.setOnClickListener(this);
        btCommander.setOnClickListener(this);

        WsUtils.printJSon();
    }

    @Override
    public void onClick(View v) {
        if (btCommander == v) {

        }
        else if (btYAller == v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_ERGOT));
            startActivity(browserIntent);
        }
    }
}
