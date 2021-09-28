package com.daniel.ramos.projetotcc.view.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import androidx.annotation.NonNull
import com.daniel.ramos.projetotcc.R
import androidx.core.content.ContextCompat.startActivity

import android.bluetooth.BluetoothAdapter

import android.content.Intent
import androidx.core.content.ContextCompat
import com.daniel.ramos.projetotcc.view.activity.MainActivity


class CustomProgressDialog(@NonNull mContext: Context) : Dialog(mContext) {
    val params = window?.attributes

    init {
        params?.gravity = Gravity.CENTER_HORIZONTAL
        window?.attributes = params
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_loading, null)
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        view.findViewById<Button>(R.id.btnParar).setOnClickListener {
            bluetoothAdapter.cancelDiscovery()
        }

        setTitle("Buscando...")

        setContentView(view)
    }
}