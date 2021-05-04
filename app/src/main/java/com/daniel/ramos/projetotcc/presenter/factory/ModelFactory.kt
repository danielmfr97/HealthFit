package com.daniel.ramos.projetotcc.presenter.factory

import com.daniel.ramos.projetotcc.presenter.BluetoothServiceA

object ModelFactory {
    private var bluetoothServiceA: BluetoothServiceA? = null

    val getBluetoothServiceA: BluetoothServiceA
        get() {
            if (bluetoothServiceA == null)
                bluetoothServiceA = BluetoothServiceA()
            return bluetoothServiceA as BluetoothServiceA
        }
}
