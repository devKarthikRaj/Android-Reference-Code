package com.raj.bt;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BtConnService";

    private static final String appName = "AllThingsBluetooth";

    private Context mContext;

    private final BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mmDevice;

    private UUID deviceUUID;
    // This UUID is specially for use with HC05 Bluetooth Modules (Yet to be tested for other devices)
    private static final UUID MY_HC05_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private ProgressDialog mProgressDialog;

    //Constructor for the entire BluetoothConnectionService Class
    public BluetoothConnectionService(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //
        start();
    }

    // All the threads are defined below (AcceptThread, ConnectThread, ConnectedThread)
    /*
     *   All threads are private
     *
     *   Each thread contains 3 methods:
     *   > Constructor method
     *   > run
     *   > cancel
     */

    /*
     * This thread runs while LISTENING FOR INCOMING CONNECTIONS. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket - Bluetooth Server Socket (which is a listening Bluetooth socket)
        private final BluetoothServerSocket mmServerSocket;

        // Constructor
        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            // listenUsingInsecureRfcommWithServiceRecord method creates a listening, insecure RFCOMM Bluetooth socket with Service Record
            try {
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_HC05_UUID);
            } catch (Exception e) {
                Log.d(TAG, "AcceptThread: Failed to create Bluetooth Socket for incoming connection");
            }

            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "AcceptThread: Running");

            // The Bluetooth Socket - A connected or connecting Bluetooth socket
            BluetoothSocket socket = null;

            try {
                // The accept method listens for a connection to be made to this socket and accepts it.
                // This accept method blocks the code execution until a connection is made... meaning...
                // This method is a blocking call (code will be stuck here at runtime until the accept method returns something)and will only
                // return on a successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (Exception e) {
                Log.d(TAG, "AcceptThread: Timeout while listening for incoming Bluetooth Connection");
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
                Log.d(TAG, "AcceptThread: Cancelling");
                mmServerSocket.close();
            } catch (IOException e) {
                Log.d(TAG, "AcceptThread: Unable to cancel");
            }
        }
    }

    /*
     * This thread runs while ATTEMPTING TO MAKE AN OUTGOING CONNECTION
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
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
            Log.d(TAG, "ConnectThread: Running");

            BluetoothSocket tmp = null;

            // The createRfcommSocketToServiceRecord method creates an RFCOMM BluetoothSocket ready to start a secure outgoing connection to
            // this remote device using SDP lookup of uuid
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.d(TAG, "ConnectThread: Failed to create socket");
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
                Log.d(TAG, "ConnectThread: Failed to connect to socket created");

                // Close the socket
                try {
                    Log.d(TAG, "ConnectThread: Closing socket created");
                    mmSocket.close();
                } catch (IOException ex) {
                    Log.d(TAG, "ConnectThread: Unable to close socket closed");
                }
            }

            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                Log.d(TAG, "ConnectThread: Cancelling");
                mmSocket.close();
            } catch (IOException e) {
                Log.d(TAG, "ConnectThread: Unable to cancel");
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
            // Cancel any ongoing Bluetooth connections in the ConnectedThread
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if(mInsecureAcceptThread == null) {
            // Start the AcceptThread to listen for new incoming connections
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }

        // ConnectThread is not being called here when this BluetoothConnectionService class is started cuz ConnectThread can only be called
        // by the user when any of the items in the paired devices recycler view is touched
    }

    /*
     * The ConnectedThread comes into play once when the connect method is connected method is called from AcceptThread or ConnectThread after
     * a connection has successfully been established with a remote device
     *
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
                Log.d(TAG, "ConnectedThread: Unable to dismiss progress dialog");
            }

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                Log.d(TAG, "ConnectedThread: Unable to get input/output stream");
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

            while(true) {
                // Read from the InputStream
                try {
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);

                    Log.d(TAG, "ConnectedThread: Incoming message: " + incomingMessage);

                    // not working part... sends received incoming message to main activity
                    //Send the received message from remote device to the main activity through an intent!!!
                    //Intent incomingMessageIntent = new Intent("incomingMessage");
                    //incomingMessageIntent.putExtra("theMessage", incomingMessage);
                    //LocalBroadcastManager.getInstance(mContext).sendBroadcast(incomingMessageIntent);
                } catch (IOException e) {
                    Log.d(TAG, "ConnectedThread: All data received from from remote device");
                    break; //Leave the loop cuz there's nothing being received in the InputStream, no point listening to it anymore
                }
            }
        }

        // This method is called through the public write method outside the ConnectedThread class
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.d(TAG, "ConnectedThread: Unable to write to remote device");
            }
        }

        // Call this from the main activity to shutdown the connection
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.d(TAG, "cancel: Unable to shutdown the connection");
            }
        }
    }

    // This method is called from AcceptThread and ConnectThread after a connection has successfully been established with a remote device
    // All this method does is basically... just starts the ConnectedThread
    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        // Start ConnectedThread to manage the connection and perform transmission
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    // All public access methods are defined below
    // -------------------------------------------

    // This method is called from the mainActivity when the user wants to initiate (start) a connection with a remote device
    public void startClient(BluetoothDevice device, UUID uuid) {
        // Open Progress Dialog while ConnectThread is running... Progress Dialog will be dismissed by the ConnectedThread
        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth", "Please Wait..", true);

        // Start the ConnectThread to attempt an outgoing connection to a remote device
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
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

    // Cancels ConnectedThread
    public void cancelConnectedThread() {
        mConnectedThread.cancel();
    }

    // Cancels all threads
    public void cancelAllThreads() {
        mInsecureAcceptThread.cancel();
        mConnectThread.cancel();
        mConnectedThread.cancel();
    }
}