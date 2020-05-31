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

    Button btnBtListPairedDevices;
    Button btnGoToBtSettings;
    ImageButton btnSendMessage;

    TextView tvMessageMonitor;

    EditText etMessageToSend;

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    BluetoothAdapter mBluetoothAdapter;
    ArrayList<BluetoothDevice> mBTDevicesInfo = new ArrayList<>();
    BluetoothDeviceListAdapter mBluetoothDeviceListAdapter;

    BluetoothConnectionService mBluetoothConnectionService;

    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice touchedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBtListPairedDevices = (Button) findViewById(R.id.btn_list_paired_devices);
        btnGoToBtSettings = (Button) findViewById(R.id.btn_goto_bt_settings);
        btnSendMessage = (ImageButton) findViewById(R.id.btn_send_message);

        tvMessageMonitor = (TextView) findViewById(R.id.tv_message_monitor);

        etMessageToSend = (EditText) findViewById(R.id.et_message_to_send);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_paired_bt_devices);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Initialize recycler view adapter and layout manager
        // to resolve no adapter attached skipping layout runtime error
        mBluetoothDeviceListAdapter = new BluetoothDeviceListAdapter(mBTDevicesInfo, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mBluetoothDeviceListAdapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBTDevicesInfo = new ArrayList<>();

        enableBt(); // Enable Bluetooth "if its not already enabled"

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
        super.onDestroy();
    }

    private void enableBt() {
        if(!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "toggleBt: Request to enable Bluetooth");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }
    }

    private void ListPairedBtDevices() {
        Set<BluetoothDevice> pairedBTDevices = mBluetoothAdapter.getBondedDevices();

        if(pairedBTDevices.size() == 0) {
            Toast.makeText(this, "No Paired Devices", Toast.LENGTH_SHORT).show();
        }
        else {
            for(BluetoothDevice individualBTDeviceInfo :  pairedBTDevices) {
                mBTDevicesInfo.add(individualBTDeviceInfo);
                Log.d(TAG, individualBTDeviceInfo.getName() + "\n" + individualBTDeviceInfo.getAddress());
            }
        }

        // To display paired devices details in recycler view
        mBluetoothDeviceListAdapter = new BluetoothDeviceListAdapter(mBTDevicesInfo, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mBluetoothDeviceListAdapter);
    }

    // Create method for starting connection
    // ***Remember the connection will fail and app will crash if you haven't paired first
    public void startConnection() {
        Toast.makeText(getBaseContext(), "at the beginning of start connection", Toast.LENGTH_SHORT).show();
        startBtConnection(touchedDevice, MY_UUID_INSECURE);
    }

    // Start the Bluetooth Connection Service method
    public void startBtConnection(BluetoothDevice device, UUID uuid) {
        mBluetoothConnectionService.startClient(device, uuid);
    }

    @Override
    public void onItemClick(int position) {
        // Cancel discovery before trying to make the connection because discovery will slow down a connection
        mBluetoothAdapter.cancelDiscovery();

        touchedDevice = mBTDevicesInfo.get(position);

        String deviceName = touchedDevice.getName();
        String deviceAdddress = touchedDevice.getAddress();

        //connect to bluetooth bond
        mBTDevicesInfo.get(position).createBond();

        mBluetoothConnectionService = new BluetoothConnectionService(MainActivity.this);
        startConnection();
    }

    @Override
    public void onLongItemClick(int position) {
        //do nothing
    }
}