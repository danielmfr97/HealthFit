package com.daniel.ramos.projetotcc.presenter.adapters

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniel.ramos.projetotcc.databinding.RowDeviceInfoBinding
//TODO: Remover da lista dispositivos já pareados
class DeviceListAdapter(
    private val context: Context,
    private val deviceList: MutableList<BluetoothDevice>
) : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {
    private var _binding: RowDeviceInfoBinding? = null
    private val binding get() = _binding!!
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = binding.textViewDeviceName
        val textAddress = binding.textViewDeviceAddress
        val deviceInfoLayout = binding.linearLayoutDeviceInfo
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = RowDeviceInfoBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deviceInfoModel = deviceList[position]
        holder.textName.text = deviceInfoModel.name
        holder.textAddress.text = deviceInfoModel.address
        holder.deviceInfoLayout.setOnClickListener {
            bluetoothAdapter.cancelDiscovery()
            deviceList[position].createBond()
        }
    }

    fun removerItemPorMAC(deviceAddress: String) {
        deviceList.forEachIndexed { index, bluetoothDevice ->
            if (deviceAddress == bluetoothDevice.address) {
                deviceList.removeAt(index)
                notifyItemRemoved(index)
            }
        }
    }
}