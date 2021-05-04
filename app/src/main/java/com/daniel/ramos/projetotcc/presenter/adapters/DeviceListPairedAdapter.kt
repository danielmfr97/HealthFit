package com.daniel.ramos.projetotcc.presenter.adapters

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniel.ramos.projetotcc.databinding.RowPairedDeviceInfoBinding
import com.daniel.ramos.projetotcc.presenter.BluetoothServiceA
import com.daniel.ramos.projetotcc.presenter.factory.ModelFactory
import java.util.*

class DeviceListPairedAdapter(private val context: Context, private var deviceList: List<BluetoothDevice>) : RecyclerView.Adapter<DeviceListPairedAdapter.ViewHolder>() {
    private var _binding: RowPairedDeviceInfoBinding? = null
    private val binding get() = _binding!!
    private val bluetoothServiceA = ModelFactory.getBluetoothServiceA

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = binding.textViewDeviceName
        val textAddress = binding.textViewDeviceAddress
        val deviceInfoLayout = binding.linearLayoutDeviceInfo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceListPairedAdapter.ViewHolder {
        _binding = RowPairedDeviceInfoBinding.inflate(LayoutInflater.from(context), parent, false)
        deviceList = deviceList.distinct()
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: DeviceListPairedAdapter.ViewHolder, position: Int) {
        val deviceInfoModel = deviceList[position]
        holder.textName.text = deviceInfoModel.name
        holder.textAddress.text = deviceInfoModel.address
        holder.deviceInfoLayout.setOnClickListener {
            val uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
            bluetoothServiceA.startClient(deviceList[position], uuid)
//            val blueComm = (MainActivity.context.applicationContext as MyApplication).myBlueComm
//            blueComm.startClient(deviceList[position], MY_UUID_INSECURE)
        }
    }
}