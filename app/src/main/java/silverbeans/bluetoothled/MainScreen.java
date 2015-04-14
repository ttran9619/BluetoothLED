package silverbeans.bluetoothled;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


public class MainScreen extends ActionBarActivity {

    private static final String TAG = "LEDOnOff";

    TableLayout tab;

    Button btnRed, btnGreen, btnBlue, btnRGBBlink, btnMultiBlink, btnRandBlink, btnBrightBlink;

    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your bluetooth devices MAC address
    private static String address = "00:06:66:6B:B6:A9";

    BluetoothArduino talker = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        talker = new BluetoothArduino();


        Log.d(TAG, "In onCreate()");
        // btnRed, btnGreen, btnBlue, btnRGBBlink, btnMultiBlink, btnRandBlink, btnBrightBlink
        setContentView(R.layout.activity_main_screen);

        btnRed = (Button) findViewById(R.id.btnRed);
        btnGreen = (Button) findViewById(R.id.btnGreen);
        btnBlue = (Button) findViewById(R.id.btnBlue);
        btnRGBBlink = (Button) findViewById(R.id.btnRGBBlink);
        btnMultiBlink = (Button) findViewById(R.id.btnMultiBlink);
        btnRandBlink = (Button) findViewById(R.id.btnRandBlink);
        btnBrightBlink = (Button) findViewById(R.id.btnBrightBlink);

        tab = (TableLayout)findViewById(R.id.tab);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));


        btnRed.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                talker.SendMessage("0");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the Red LED", Toast.LENGTH_SHORT);
                msg.show();
            }
        });

        btnGreen.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                talker.SendMessage("1");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the Green LED", Toast.LENGTH_SHORT);
                msg.show();
            }
        });
        btnBlue.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                talker.SendMessage("2");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the Blue LED", Toast.LENGTH_SHORT);
                msg.show();
            }
        });
        btnRGBBlink.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                talker.SendMessage("3");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the RGB Blinker", Toast.LENGTH_SHORT);
                msg.show();
            }
        });
        btnMultiBlink.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                talker.SendMessage("4");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the MultiColor Blinker", Toast.LENGTH_SHORT);
                msg.show();
            }
        });
        btnRandBlink.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                talker.SendMessage("5");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the Random Blinker", Toast.LENGTH_SHORT);
                msg.show();
            }
        });
        btnBrightBlink.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                talker.SendMessage("5");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the Bright LED Blinker", Toast.LENGTH_SHORT);
                msg.show();
            }
        });
    }



    private BroadcastReceiver onNotice= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");


            if (pack.equalsIgnoreCase("com.google.android.talk")){
                talker.SendMessage("0");
            }

            if (pack.equalsIgnoreCase("com.valvesoftware.android.steam.community")){
                talker.SendMessage("1");
            }

            if (pack.equalsIgnoreCase("com.google.android.apps.inbox")){
                talker.SendMessage("2");
            }

            if (pack.equalsIgnoreCase("com.groupme.android")){
                talker.SendMessage("3");
            }


            TableRow tr = new TableRow(getApplicationContext());
            tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#0B0719"));
            textview.setText(Html.fromHtml(pack +"<br><b>" + title + " : </b>" + text));
            tr.addView(textview);
            tab.addView(tr);


        }
    };

    private void errorExit(String title, String message){
        Toast msg = Toast.makeText(getBaseContext(),
                title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();
        finish();
    }

}
