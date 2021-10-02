package com.daniel.ramos.projetotcc.presenter.adapters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.RowPairedDeviceInfoBinding
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.BluetoothServiceA
import com.daniel.ramos.projetotcc.view.activity.MainActivity


class DeviceListPairedAdapter(
    private val context: Context,
    private var deviceList: MutableList<BluetoothDevice>
) : RecyclerView.Adapter<DeviceListPairedAdapter.ViewHolder>() {
    private var _binding: RowPairedDeviceInfoBinding? = null
    private val binding get() = _binding!!
    private val bluetoothServiceA = ModelFactory.getBluetoothServiceA

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = binding.textViewDeviceName
        val textAddress = binding.textViewDeviceAddress
        val btnConectar = binding.btnConectar
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeviceListPairedAdapter.ViewHolder {
        _binding = RowPairedDeviceInfoBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: DeviceListPairedAdapter.ViewHolder, position: Int) {
        val deviceInfoModel = deviceList[position]
        holder.textName.text = deviceInfoModel.name
        holder.textAddress.text = deviceInfoModel.address
        if (MainActivity.statusBlueDevice.value == BluetoothServiceA.STATE_CONNECTED && deviceInfoModel.name.startsWith("FitSpot")) {
            holder.btnConectar.setText(R.string.Desconectar)
            holder.btnConectar.setOnClickListener { bluetoothServiceA.stop() }
        }
        else {
            holder.btnConectar.setText(R.string.Conectar)
            holder.btnConectar.setOnClickListener { bluetoothServiceA.connect(deviceInfoModel, true)}
        }
    }

}