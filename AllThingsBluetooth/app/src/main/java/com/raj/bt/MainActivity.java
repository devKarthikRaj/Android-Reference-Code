package com.raj.bt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

//This must be imported for binding to the BluetoothConnectionService
import com.raj.bt.BluetoothConnectionService.LocalBinder;

public class MainActivity extends AppCompatActivity implements PairedDevicesRVClickInterface{
    private static final String TAG = "MainActivity"; // For logging

    TextView tvBTStatusMonitor;
    TextView tvMessageMonitor;

    EditText etMessageToSend;

    Button btnBtListPairedDevices;
    Button btnGoToBtSettings;
    Button btnDisconnect;
    ImageButton btnSendMessage;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothDeviceListAdapter mBluetoothDeviceListAdapter;
    ArrayList<BluetoothDevice> mBTDevicesInfo = new ArrayList<>(); // Data model containing pair Bluetooth devices list (The phone has this data model in it!)
    BluetoothDevice touchedDevice;
    String remoteDeviceName;
    String remoteDeviceAddress;

    RecyclerView rvPairedDevices;
    LinearLayoutManager mLayoutManager;

    // This UUID is specially for use with HC05 Bluetooth Modules (Yet to be tested for other devices)
    private static final UUID MY_HC05_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //Service
    BluetoothConnectionService mBluetoothConnectionService;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBTStatusMonitor = findViewById(R.id.tv_bt_status_monitor);
        tvMessageMonitor = findViewById(R.id.tv_bt_message_monitor);

        etMessageToSend = findViewById(R.id.et_message_to_send);

        btnBtListPairedDevices = findViewById(R.id.btn_list_paired_devices);
        btnGoToBtSettings = findViewById(R.id.btn_goto_bt_settings);
        btnDisconnect = findViewById(R.id.btn_disconnect);
        btnSendMessage = findViewById(R.id.btn_send_message);

        //Bind to service
        /*
        * Could simply just do this instead of bind:
        * mBluetoothConnectionService = new BluetoothConnectionService(MainActivity.this, handler); // Start the BluetoothConnectionService Class
        * But this is not a good way to do it cuz if you have to access the BluetoothConnectionService from multiple activites then you'll
        * be forced to create multiple instances of the BluetoothConnectionService from these multiple activities
        *
        * But once a service is alr started in the activity that you are first creating an instance of it from... its stuck with that
        * activity... So...
        *
        * I am "binding" to the service instead of creating an instance of the service... apparently somehow android does some black magic
        * (im actually just lazy to explain the mechanism at work here but yeah)... this way we can access the service from multiple
        * activites and the service will only start once... Problem solved!!!*/
        Intent serviceIntent = new Intent(this, BluetoothConnectionService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        // Initialize recycler view adapter and layout manager
        //-
        // To resolve no adapter attached skipping layout runtime error
        mBluetoothDeviceListAdapter = new BluetoothDeviceListAdapter(mBTDevicesInfo, this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBTDevicesInfo = new ArrayList<>();
        enableBt(); // Enable Bluetooth "if its not already enabled"


        rvPairedDevices = findViewById(R.id.rv_paired_bt_devices);
        rvPairedDevices.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPairedDevices.setLayoutManager(mLayoutManager);
        rvPairedDevices.setAdapter(mBluetoothDeviceListAdapter);

        // Intent to detect Bluetooth Connection Established with remote device (Note: Intent Filters work hand in hand with broadcast receivers)
        // ACTION_ACL_CONNECTED is used instead of BLUETOOTH_CONNECTION_STATE_CHANGED cuz the UUID used is weird (HC05 problems!!!)
        IntentFilter BTConnectedToRemoteDeviceIntent = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        registerReceiver(BTConnectedBroadcastReceiver, BTConnectedToRemoteDeviceIntent);

        IntentFilter BTDisconnectedFromRemoteDeviceIntent = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(BTDisonnectedBroadcastReceiver, BTDisconnectedFromRemoteDeviceIntent);

        // OnClickListeners
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                tvMessageMonitor.append("\n" + "> " + etMessageToSend.getText() + "\n");
                byte[] bytes = etMessageToSend.getText().toString().getBytes(Charset.defaultCharset());
                mBluetoothConnectionService.write(bytes);
                etMessageToSend.setText(null);
            }
        });

        btnBtListPairedDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBluetoothAdapter.isEnabled()) {
                    Log.d(TAG, "onClick: Listing paired devices (if any)");
                    ListPairedBtDevices();
                } else {
                    Toast.makeText(getBaseContext(), "Enable Bluetooth to view paired devices", Toast.LENGTH_SHORT).show();
                    enableBt();
                }
            }
        });

        btnGoToBtSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS), 0);
            }
        });

        // Basically... What this method does is to simply detect when you touch "Edit Text Message to Send" and clear the help text displayed
        // in the edit text so that you can type in whatever you want to send over the link
        etMessageToSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etMessageToSend.setText("");
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: Called");
        mBluetoothConnectionService.cancelAllThreads(); // Cancel all threads running in BluetoothConnectionService
        mBluetoothAdapter.disable(); // Turn of Bluetooth
        unregisterReceiver(BTConnectedBroadcastReceiver);
        unregisterReceiver(BTDisonnectedBroadcastReceiver);
        super.onDestroy(); // Ultimate Destruction aka - Close the app!!!
    }

    // Turn on the device's Bluetooth
    private void enableBt() {
        if(!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "toggleBt: Request to enable Bluetooth");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }
    }

    // Lists all devices that the device has "already paired with"
    private void ListPairedBtDevices() {
        Set<BluetoothDevice> pairedBTDevices = mBluetoothAdapter.getBondedDevices();

        if(pairedBTDevices.size() == 0) {
            Toast.makeText(this, "No Paired Devices", Toast.LENGTH_SHORT).show();
        }
        else {
            // The ":" operator usage in for loop...
            // For every item in pairedBTDevices, equal that particular item to individualBTDevices and go through the for loop, then repeat!
            for(BluetoothDevice individualBTDeviceInfo :  pairedBTDevices) {
                mBTDevicesInfo.add(individualBTDeviceInfo);
                Log.d(TAG, individualBTDeviceInfo.getName() + "\n" + individualBTDeviceInfo.getAddress());
            }
        }

        // To display paired devices details in recycler view
        mBluetoothDeviceListAdapter = new BluetoothDeviceListAdapter(mBTDevicesInfo, this);
        rvPairedDevices.setLayoutManager(mLayoutManager);
        rvPairedDevices.setAdapter(mBluetoothDeviceListAdapter);
    }

    // Recycler View onItemClick method
    @Override
    public void onItemClick(int position) {
        // Cancel discovery before trying to make the connection because discovery will slow down a connection
        mBluetoothAdapter.cancelDiscovery();

        touchedDevice = mBTDevicesInfo.get(position);

        remoteDeviceName = touchedDevice.getName();
        remoteDeviceAddress = touchedDevice.getAddress();

        //connect to bluetooth bond
        mBTDevicesInfo.get(position).createBond();

        startConnection();
    }

    // Recycler View onItemLongClick method
    @Override
    public void onLongItemClick(int position) {
        //do nothing
    }

    // ***Remember the connection will fail and app will crash if you attempt to connect to an unpaired device***
    private void startConnection() {
        mBluetoothConnectionService.startClient(touchedDevice, MY_HC05_UUID);
    }

    private void disconnect() {
        mBluetoothConnectionService.cancelConnectedThread();
    }

    //Broadcast receiver to detect incoming message received (not working!!!)
    /*
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String recMessage = intent.getStringExtra("theMessage");
            tvMessageMonitor.append("\n< " + recMessage);
        }
    };
     */

    // Broadcast receivers to monitor Bluetooth connection status and update tvBTStatusMonitor
    private final BroadcastReceiver BTConnectedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Resources resources = getResources();

            tvBTStatusMonitor.setText(R.string.connected_to_text);
            tvBTStatusMonitor.append(" ");
            tvBTStatusMonitor.append(String.format(resources.getString(R.string.remote_device_name), remoteDeviceName));

            tvMessageMonitor.setText(R.string.start_of_comms_text );
            tvMessageMonitor.append(" ");
            tvMessageMonitor.append(String.format(resources.getString(R.string.remote_device_name), remoteDeviceName));
            tvMessageMonitor.append("\n");
        }
    };

    private final BroadcastReceiver BTDisonnectedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tvBTStatusMonitor.setText(R.string.disconnected_text);
            tvMessageMonitor.setText(R.string.end_of_comms_text);
        }
    };

    @SuppressLint("HandlerLeak")
    public final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle recBundle = msg.getData();
            String incomingMessage = recBundle.getString("incomingMessage");
            Resources resources = getResources();
            tvMessageMonitor.append("\n" + String.format(resources.getString(R.string.remote_device_name), remoteDeviceName) + ": " + incomingMessage);
            Log.d(TAG,"HandlerMsg: " + incomingMessage);
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBinder mBinder = (LocalBinder) service;
            mBluetoothConnectionService = mBinder.getService();

            //Can't pass the handler through the construtor of the service class (no point having a constructor in a service class so
            //we gotta pass it through a method that I coded into the service (something like a setter method)
            mBluetoothConnectionService.setHandler(handler);

            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
}















