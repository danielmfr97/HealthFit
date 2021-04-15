package com.daniel.ramos.projetotcc.presenter.listeners

import android.content.IntentFilter

interface OnBluetoothAtivado {
    fun ativarBroadcastReceiver(bluetoothFilter: IntentFilter)
}