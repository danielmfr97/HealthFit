package com.daniel.ramos.projetotcc.presenter

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*

private const val TAG = "BlueComm"
private const val appName = "PROJETOTCC"
class BlueComm {
    // Para bluetooth serial board  00001101-0000-1000-8000-00805F9B34FB
//    private val MY_UUID_INSECURE =
//        UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
    private val MY_UUID_INSECURE =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val MY_UUID_SECURE =
        UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private var mInsecureAcceptThread: AcceptThread? = null
    private var mConnectThread: ConnectThread? = null
    private var mConnectedThread: ConnectedThread? = null

    private var mmDevice: BluetoothDevice? = null
    private var deviceUUID: UUID? = null


    /**
     * Essa thread roda enquanto espera por conexões possíveis.
     * Atua como um server-side client. Roda até uma conexão ser aceita (ou até cancelada)
     */
    inner class AcceptThread : Thread() {
        // Socket server local
        private var mmServerSocket: BluetoothServerSocket? = null

        // Cria um novo listening para server socket
        init {
            var tmp: BluetoothServerSocket? = null
            try {
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                    appName,
                    MY_UUID_INSECURE
                )
                Log.d(TAG, "AcceptThread: Configurando Server usando: $MY_UUID_INSECURE")
            } catch (e: IOException) {
                Log.e(TAG, "AcceptThread: IOException: ${e.message}")
            }
            mmServerSocket = tmp
        }

        override fun run() {
            Log.d(TAG, "AcceptThread: Running")
            var socket: BluetoothSocket? = null

            // Este bloco ira retornar uma conexão sucedida ou exceção
            Log.d(TAG,"AcceptThread: Run: RFCOM server socket iniciado...")
            socket = mmServerSocket!!.accept()
            Log.d(TAG,"AcceptThread: Run: RFCOM server socket aceitou conexão.")

            if (socket != null)
                connected(socket, mmDevice!!)
            Log.i(TAG, "END mAcceptedThread'")
        }

        fun cancel() {
            mmServerSocket!!.close()
        }
    }

    /**
     * Essa thread roda enquanto tenta estabelecer uma conexão com dispositivo.
     * A conexão é bem sucedida ou falha
     */
    inner class ConnectThread(device: BluetoothDevice, uuid: UUID) : Thread() {
        private var mmSocket: BluetoothSocket? = null
        init {
            Log.d(TAG, "ConnectedThread: started")
            mmDevice = device
            deviceUUID = uuid
        }

        override fun run() {
            var tmp: BluetoothSocket? = null
            Log.i(TAG, "RUN mConnectThread")

            // Pega um BluetoothSocket para uma conexão com o dado BluetoothDevice

            try {
                Log.d(TAG, "ConnectThread: Tentando criar InsecureRfcommSocket usando UUID: $MY_UUID_INSECURE")
                tmp = mmDevice!!.createRfcommSocketToServiceRecord(deviceUUID)
            } catch (e: IOException) {
                Log.e(TAG, "ConnectThread: Não foi possível criar InsecureRfcommSocket ${e.message}")
            }

            mmSocket = tmp

            // Sempre cancele o discovery pois pode causar lentidão na conexão
            bluetoothAdapter.cancelDiscovery()

            try {
                mmSocket!!.connect()
                Log.d(TAG, "run: ConnectThread connected.")
            } catch (e: IOException) {
                // Fecha o socket
                try {
                    mmSocket!!.close()
                    Log.d(TAG, "run: Closed Socket.")
                } catch (e1: IOException) {
                    Log.e(TAG, "mConnectThread: run: Impossível fechar a conexão no socket ${e1.message}")
                }
                Log.d(TAG,"run: ConnectThread: Impossível conectar to UUID: $MY_UUID_INSECURE")
            }

            connected(mmSocket!!, mmDevice!!)
        }

        fun cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket")
                mmSocket!!.close()
            } catch (e: IOException) {
                Log.e(TAG, "cancel: close() of mmSocket na ConnectThread falhou. ${e.message}")
            }
        }
    }

    private fun connected(mmSocket: BluetoothSocket, mmDevice: BluetoothDevice) {
        Log.d(TAG, "connected: Starting.")

        // Inicia a thread que gerencia a conexão e performa as transmissões
        mConnectedThread = ConnectedThread(mmSocket)
        mConnectedThread!!.start()
    }

    /**
     * Inicia o serviço de comunicação. Especificamente inicia AcceptThread para iniciar a sessão
     * em modo de listening (server). Chamado pela Activity onResume
     */
    @Synchronized
    fun start() {
        if (mConnectThread != null) {
            mConnectThread!!.cancel()
            mConnectThread = null
        }

        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = AcceptThread()
            mInsecureAcceptThread!!.start()
        }
    }

    /**
     *  AcceptThread inicia e aguarda pela conexão.
     *  Então ConnectThread inicia e tenta estabelecer uma conexão com outro dispositivo
     */
    fun startClient(device: BluetoothDevice, uuid: UUID) {
        Log.d(TAG, "startClient: Started.")

        // initprogress dialog
//        mProgressDialog = ProgressDialog.show(context, "Conectando Bluetooth", "Por favor aguarde...", true)
        mConnectThread = ConnectThread(device, uuid)
        mConnectThread!!.start()
    }

    inner class ConnectedThread(socket: BluetoothSocket): Thread() {
        private var mmSocket: BluetoothSocket
        private var mmInStream: InputStream? = null
        private var mmOutStream: OutputStream? = null

        init {
            Log.d(TAG, "ConnectedThread: Starting")
            mmSocket = socket
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null

            // Dismiss progressDialog quando conexão estabelecida
            //mProgressDialog.dismiss()

            try {
                tmpIn = mmSocket.inputStream
                tmpOut = mmSocket.outputStream
            }catch (e: IOException) {e.printStackTrace()}

            mmInStream = tmpIn
            mmOutStream = tmpOut
        }

        override fun run() {
            Log.i(TAG, "BEGIN mConnectedThread")
            val buffer = ByteArray(1024) //Armazena o stream
            var bytes: Int // bytes retornados de read()

            // Continua escutando ao InputStream enquanto conectado
            while (true) {
                try {
                    bytes = mmInStream!!.read(buffer)
                    val incomingMessage = String(buffer, 0, bytes)
                    Log.d(TAG, "InputStream: $incomingMessage")
                } catch (e: IOException) {
                    Log.e(TAG, "disconnected", e)
                    e.printStackTrace()
                    break
                }
            }
        }

        // Chame esta função para enviar dados ao dispositivo remoto
        fun write(bytes: ByteArray) {
            val text = String(bytes, Charset.defaultCharset())
            Log.d(TAG, "write: Escrevendo na outputstream: $text")
            try {
                mmOutStream!!.write(bytes)
            } catch (e: IOException) {
                Log.e(TAG, "write: Error escrevendo ao outputstream ${e.message}")
            }
        }

        // Chame esta função para desligar a conexão
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {e.printStackTrace()}
        }
    }

    /**
     * Escreve na ConnectedThread de maneira assincrona
     * @param out Os bytes para escrite
     * @see ConnectedThread#write(byte[])
     */
    fun write(out: ByteArray) {
        Log.d(TAG, "write: Write chamado")
        mConnectedThread!!.write(out)
    }
}