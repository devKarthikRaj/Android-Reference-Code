package com.raj.bt;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BtConnService";

    private static final String appName = "AllThingsBluetooth";

    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;

    //Constructor
    public BluetoothConnectionService(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    // All the threads are defined below
    /*
        Each thread must contain 3 methods:
        > Constructor method
        > run
        > cancel
     */

    /*
     * This thread runs while LISTENING FOR INCOMING CONNECTIONS. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     *
     * [USE BLUETOOTH SERVER SOCKET FOR THIS]
     */
    private class AcceptThread extends Thread {
        // The local server socket - Bluetooth Server Socket (which is a listening Bluetooth socket)
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            // listenUsingInsecureRfcommWithServiceRecord method creates a listening, insecure RFCOMM Bluetooth socket with Service Record
            try {
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "Accept Thread Running");

            BluetoothSocket socket = null;

            try {
                // The accept method listens for a connection to be made to this socket and accepts it.
                // This accept method blocks the code execution until a connection is made... meaning...
                // This method is a blocking call (code will be stuck here at runtime until the accept method returns something)and will only
                // return on a successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // If the server socket is not null i.e. it has something in it...
            // meaning... it mmServerSocket.accept() has found a connection and accepted it
            // Then run the connected method
            if(socket != null) {
                connected(socket, mmDevice);
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /*
     * This thread runs while ATTEMPTING TO MAKE AN OUTGOING CONNECTION
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     *
     * [USE BLUETOOTH SOCKET FOR THIS]
     */
    private class ConnectThread extends Thread {

        // The Bluetooth Socket - A connected or connecting Bluetooth socket
        private BluetoothSocket mmSocket;

        // Constructor
        public ConnectThread(BluetoothDevice device, UUID uuid) {
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            Log.d(TAG, "Connect Thread Running");

            BluetoothSocket tmp = null;

            // The createRfcommSocketToServiceRecord method creates an RFCOMM BluetoothSocket ready to start a secure outgoing connection to
            // this remote device using SDP lookup of uuid
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {

                e.printStackTrace();
            }

            mmSocket = tmp;

            // Cancel discovery before trying to make the connection because discovery will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket created
            try {
                // The connect method attempts to connect to a remote device.
                // This connect method blocks the code execution until a connection is made... meaning...
                // This method is a blocking call (code will be stuck here at runtime until the connect method returns something) and will only
                // return on a successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * The start function is called when the service is started (hence... the name start DUH!)
     *
     * In this method, the AcceptThread is "started" to begin a session in listening (server) mode
     * Called by the Activity onResume()
     *
     * The synchronized keyword allows one thread at a time into a particular section of code thus allowing us to protect...
     * for example, variables or data from being corrupted by simultaneous modifications from different threads
     */
    public synchronized void start() {
        if(mConnectedThread != null) {
            // Cancel any thread attemping to make a connection and empty the the mConnectedThread
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if(mInsecureAcceptThread == null) {
            // Start the AcceptThread to listen for new incoming connections
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    /*
     * AcceptThread starts and sits waiting for a connection.
     * Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
     */
    public void startClient(BluetoothDevice device, UUID uuid) {
        // initprogress dialog
        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth", "Please Wait..", true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    /*
     * The ConnectedThread is responsible for:
     * > Maintaining the BTConnection
     * > Sending the data
     * > Receiving incoming data through input/output streams respectively
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        // Constructor
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;

            // The Java InputStream class, java.io.InputStream, represents an ordered stream of bytes. In other words, you can read data from a
            // Java InputStream as an ordered sequence of bytes. This is useful when reading data from a file, or received over the network.
            InputStream tmpIn = null;

            // The Java OutputStream class, java.io.OutputStream, accepts output bytes and sends them to some link.
            OutputStream tmpOut = null;

            // Dismiss the progress dialog when connection is established
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {

                e.printStackTrace();
            }

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {

                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.d(TAG, "Connected Thread Running");

            byte[] buffer = new byte[1024]; // Buffer store for the stream - Basically this is a byte array that can store 1024 bytes

            int bytes; // To store bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            // One of the cases in which the exception might be thrown is when buffer is null, a NullPointerException will be thrown,
            // this means there is nothing being received in the InputStream, so this loop will run until nothing is received in the
            // input stream
            /*
            while(true) {
                // Read from the InputStream
                try {
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                } catch (IOException e) {

                    e.printStackTrace();
                    break; //Leave the loop cuz there's nothing being received in the InputStream, no point listening to it anymore
                }
            }
            */
        }

        // Call this method from the main activity to send data to the remote device
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        // Call this from the main activity to shutdown the connection
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        // Start this thread to manage the connection and perform transmission
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    /*
     * Write to the ConnectedThread in an asynchronous manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;

        // Perform the write
        mConnectedThread.write(out);
    }
}