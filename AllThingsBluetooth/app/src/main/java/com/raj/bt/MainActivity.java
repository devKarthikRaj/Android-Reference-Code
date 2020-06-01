package com.raj.bt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements PairedDevicesRVClickInterface{
    private static final String TAG = "MainActivity"; // For logging

    TextView tvMessageMonitor;

    EditText etMessageToSend;

    Button btnBtListPairedDevices;
    Button btnGoToBtSettings;
    ImageButton btnSendMessage;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothDeviceListAdapter mBluetoothDeviceListAdapter;
    ArrayList<BluetoothDevice> mBTDevicesInfo = new ArrayList<>();
    BluetoothConnectionService mBluetoothConnectionService;
    BluetoothDevice touchedDevice;

    RecyclerView rvPairedDevices;
    LinearLayoutManager mLayoutManager;

    // This UUID is specially for use with HC05 Bluetooth Modules (Yet to be tested for other devices)
    private static final UUID MY_HC05_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMessageMonitor = (TextView) findViewById(R.id.tv_message_monitor);

        etMessageToSend = (EditText) findViewById(R.id.et_message_to_send);

        btnBtListPairedDevices = (Button) findViewById(R.id.btn_list_paired_devices);
        btnGoToBtSettings = (Button) findViewById(R.id.btn_goto_bt_settings);
        btnSendMessage = (ImageButton) findViewById(R.id.btn_send_message);

        // Initialize recycler view adapter and layout manager
        // to resolve no adapter attached skipping layout runtime error
        mBluetoothDeviceListAdapter = new BluetoothDeviceListAdapter(mBTDevicesInfo, this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBTDevicesInfo = new ArrayList<>();
        enableBt(); // Enable Bluetooth "if its not already enabled"
        mBluetoothConnectionService = new BluetoothConnectionService(MainActivity.this); // Start the BluetoothConnectionService Class

        rvPairedDevices= (RecyclerView) findViewById(R.id.rv_paired_bt_devices);
        rvPairedDevices.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPairedDevices.setLayoutManager(mLayoutManager);
        rvPairedDevices.setAdapter(mBluetoothDeviceListAdapter);

        // OnClickListeners
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                byte[] bytes = etMessageToSend.getText().toString().getBytes(Charset.defaultCharset());
                mBluetoothConnectionService.write(bytes);
                etMessageToSend.setText(null);
            }
        });

        btnBtListPairedDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBluetoothAdapter.isEnabled()) {
                    Log.d(TAG, "onClick: Listing paired devices (if any)");
                    ListPairedBtDevices();
                }
                else {
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

        etMessageToSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etMessageToSend.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: Called");
        mBluetoothConnectionService.cancelAllThreads(); // Cancel all threads running in BluetoothConnectionService
        mBluetoothAdapter.disable(); // Turn of Bluetooth
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

    // ***Remember the connection will fail and app will crash if you attempt to connect to an unpaired device***
    private void startConnection() {
        startBtConnection(touchedDevice, MY_HC05_UUID);
    }

    // mBluetoothConnectionService.startClient method starts the ConnectThread which will attempt to make an outgoing connection
    private void startBtConnection(BluetoothDevice device, UUID uuid) {
        mBluetoothConnectionService.startClient(device, uuid);
    }

    // Recycler View onItemClick method
    @Override
    public void onItemClick(int position) {
        // Cancel discovery before trying to make the connection because discovery will slow down a connection
        mBluetoothAdapter.cancelDiscovery();

        touchedDevice = mBTDevicesInfo.get(position);

        String deviceName = touchedDevice.getName();
        String deviceAdddress = touchedDevice.getAddress();

        //connect to bluetooth bond
        mBTDevicesInfo.get(position).createBond();

        startConnection();
    }

    // Recycler View onItemLongClick method
    @Override
    public void onLongItemClick(int position) {
        //do nothing
    }
}