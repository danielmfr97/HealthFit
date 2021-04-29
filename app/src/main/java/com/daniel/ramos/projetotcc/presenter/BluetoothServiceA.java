package com.daniel.ramos.projetotcc.presenter;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothServiceA extends Service {
    private static final String TAG = "BluetoothServiceA";
    private BluetoothAdapter mBluetoothAdapter;
    private static final String APP = "PROJETOSMI";
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private BluetoothDevice mDevice;
    private UUID deviceUUID;


    private final IBinder mBinder = new BluetoothServiceA.LocalBinder();

    public class LocalBinder extends Binder {
        BluetoothServiceA getService() {
            // Return this instance of LocalService so clients can call public methods
            return BluetoothServiceA.this;
        }
    }

    public void init(BluetoothDevice device, UUID uuid) {
        if (mBluetoothAdapter == null)
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        startClient(device, uuid);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return START_STICKY;
    }

    public synchronized void start() throws IOException {
        Log.d(TAG, "Start");

        // Cancel any thread attemp to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    /**
     * Roda ate uma conexão ser aceita
     * Escuta possíveis conexoes
     */
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        public AcceptThread() throws IOException {
            BluetoothServerSocket tmp = null;
            tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP, MY_UUID_INSECURE);
            mmServerSocket = tmp;
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;

            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (socket != null) {
                connected(socket, mDevice);
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Thread roda enquanto tenta estabelecer uma 'outgoing connection with a device'. It Runs straight thought
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            mDevice = device;
            deviceUUID = uuid;
        }

        @Override
        public void run() {
            BluetoothSocket tmp = null;
            try {
                tmp = mDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;

            mBluetoothAdapter.cancelDiscovery();

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();

            } catch (IOException e) {
                try {
                    mmSocket = (BluetoothSocket) mDevice.getClass().getMethod(
                            "createRfcommSocket", Integer.class).invoke(mDevice, 1);
                    mmSocket.connect();
                } catch (IOException | NoSuchMethodException e1 ){
                    e1.printStackTrace();
                    try {
                        mmSocket.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                } catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
            }
            connected(mmSocket, mDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * AcceptThread starts and sits waiting for a connection.
     * Then ConnectThread start and attempts to make a connection with the other devices AcceptThread
     */
    public void startClient(BluetoothDevice device, UUID uuid) {
//        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth", "Please Wait...", true);
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    /**
     Finally the ConnectedThread which is responsible for maintaining the BTConnection, Sending the data, and
     receiving incoming data through input/output streams respectively.
     **/
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting.");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

//            //dismiss the progressdialog when connection is established
//            try{
//                mProgressDialog.dismiss();
//            }catch (NullPointerException e){
//                e.printStackTrace();
//            }

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                // Read from the InputStream
                try {
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream: " + incomingMessage);
                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage() );
                    e.printStackTrace();
                    break;
                }
            }
        }

        //Call this from the main activity to send data to the remote device
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
                e.printStackTrace();
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;

        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called.");
        //perform the write
        mConnectedThread.write(out);
    }
}
