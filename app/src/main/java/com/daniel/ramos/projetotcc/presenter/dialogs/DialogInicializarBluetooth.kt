package com.daniel.ramos.projetotcc.presenter.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.presenter.listeners.OnBluetoothAtivado

class DialogInicializarBluetooth(val onBluetoothAtivado: OnBluetoothAtivado): DialogFragment() {
    private val TAG = "DialogInicializarBlueto"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.avisoBluetoothDesabilitado)
                .setPositiveButton(R.string.confirmar
                ) { _, _ ->

                }
                .setNegativeButton(R.string.cancelar
                ) { _, _ ->
                   Toast.makeText(context, "Impossível acessar as configurações com bluetooth desativado", Toast.LENGTH_LONG).show()
                }
            // Create the AlertDialog object and return it
            builder.show()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun onBluetoothAtivado(bluetoothFilter: IntentFilter) {
        onBluetoothAtivado.ativarBroadcastReceiver(bluetoothFilter)
    }
}