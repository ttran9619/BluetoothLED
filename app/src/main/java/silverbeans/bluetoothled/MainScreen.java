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

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainScreen extends ActionBarActivity {
    private static final String TAG = "LEDOnOff";

    Button btnRed, btnGreen, btnBlue, btnRGBBlink, btnMultiBlink, btnRandBlink, btnBrightBlink;

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your bluetooth devices MAC address
    private static String address = "00:06:66:6B:B6:A9";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));


        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        btnRed.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SendMessage("0");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the Red LED", Toast.LENGTH_SHORT);
                msg.show();
            }
        });

        btnGreen.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SendMessage("1");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the Green LED", Toast.LENGTH_SHORT);
                msg.show();
            }
        });
        btnBlue.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SendMessage("2");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the Blue LED", Toast.LENGTH_SHORT);
                msg.show();
            }
        });
        btnRGBBlink.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SendMessage("3");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the RGB Blinker", Toast.LENGTH_SHORT);
                msg.show();
            }
        });
        btnMultiBlink.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SendMessage("4");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the MultiColor Blinker", Toast.LENGTH_SHORT);
                msg.show();
            }
        });
        btnRandBlink.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SendMessage("5");
                Toast msg = Toast.makeText(getBaseContext(),
                        "You turned on the Random Blinker", Toast.LENGTH_SHORT);
                msg.show();
            }
        });
        btnBrightBlink.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SendMessage("6");
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



            if (pack.equals("com.google.android.talk")){
                SendMessage("0");
            }

            if (pack.equals("com.valvesoftware.android.steam.community")){
                SendMessage("0");
            }

            if (pack.equals("com.google.android.apps.inbox")){
                SendMessage("2");
            }

            if (pack.equals("com.groupme.android")){
                SendMessage("3");
            }

            if (text.equals("blue")){
                SendMessage("0");
            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...In onResume - Attempting client connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting to Remote...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection established and data link opened...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Creating Socket...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on

        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth is enabled...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void errorExit(String title, String message){
        //Toast msg = Toast.makeText(getBaseContext(),
        //        title + " - " + message, Toast.LENGTH_SHORT);
       // msg.show();
       // finish();
    }

    private void SendMessage(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

           // errorExit("Fatal Error", msg);
        }
    }

}
