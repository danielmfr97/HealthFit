package com.daniel.ramos.projetotcc.presenter


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.daniel.ramos.projetotcc.view.fragment.ConfigurarAppFragment

private const val TAG = "ConfigurarAppPresenter"
class ConfigurarAppPresenter(private val view: ConfigurarAppFragment?) {

    val broadcastDiscoverable: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
                val mode =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR)
                when (mode) {
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> Log.d(
                        TAG,
                        "mBroadcastReceiver2: Discoverability Enabled."
                    )
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE -> Log.d(
                        TAG,
                        "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections."
                    )
                    BluetoothAdapter.SCAN_MODE_NONE -> Log.d(
                        TAG,
                        "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections."
                    )
                    BluetoothAdapter.STATE_CONNECTING -> Log.d(
                        TAG,
                        "mBroadcastReceiver2: Connecting...."
                    )
                    BluetoothAdapter.STATE_CONNECTED -> Log.d(
                        TAG,
                        "mBroadcastReceiver2: Connected."
                    )
                }
            }
        }
    }
    val broadcastDiscoverBTDevices: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) {
                Log.d(TAG, "Action discovery started")
                view?.enableDisableProgress(true)
                view?.mDeviceList?.clear()
                view?.updateAdapterDispositivosEncontrados()
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                Log.d(TAG, "Action discovery Finished")
                view?.enableDisableProgress(false)
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND == action) {
                Log.d(TAG, "Bluetooth encontrado")
                //bluetooth device found
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    view?.mDeviceList?.add(device)
                    view?.updateAdapterDispositivosEncontrados()
                }
            }
        }
    }
    val broadcastBondStateBT = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action) {
                val device = intent
                    .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                when (device!!.bondState) {
                    BluetoothDevice.BOND_BONDED -> {
                        Log.d(TAG, "BroadcastReceiver: BOND_BONDED")
                        //TODO: Editar
                        view?.mPairedDeviceList?.add(device)
                        view?.atualizarListaPareados()
                    }
                    BluetoothDevice.BOND_BONDING -> {
                        Log.d(TAG, "BroadcastReceiver: BOND_BONDING")
//                        habilitarDiscoverableBluetooth()
                    }
                    BluetoothDevice.BOND_NONE -> {
                        Log.d(TAG, "BroadcastReceiver: BOND_DONE")
                    }
                }
            }
        }
    }
}
