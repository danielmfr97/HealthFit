package com.daniel.ramos.projetotcc.view.fragment

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.ramos.projetotcc.databinding.FragmentConfigurarAppBinding
import com.daniel.ramos.projetotcc.presenter.ConfigurarAppPresenter
import com.daniel.ramos.projetotcc.presenter.adapters.DeviceListAdapter
import com.daniel.ramos.projetotcc.view.activity.MainActivity


class ConfigurarAppFragment : Fragment() {
    private val TAG = "ConfigurarAppFragment"

    private var _binding: FragmentConfigurarAppBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ConfigurarAppPresenter

    private var mDeviceList = arrayListOf<BluetoothDevice>()
    lateinit var mBTDevice: BluetoothDevice

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfigurarAppBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        configurarBotoes()
        configurarRecyclerView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        MainActivity.context.registerReceiver(broadcastBondStateBT, filter)
        //TODO: Criar lista de dispositivos disponiveis e dispositivos pareados
    }

    override fun onDestroy() {
        MainActivity.context.unregisterReceiver(broadcastDiscoverBTDevices)
        MainActivity.context.unregisterReceiver(broadcastBondStateBT)
        super.onDestroy()
    }

    private fun inicializarPresenter() {
        presenter = ConfigurarAppPresenter(this)
    }

    private fun configurarBotoes() {
        binding.buscarDispositivos.setOnClickListener {
            mDeviceList.clear()
            updateAdapterDispositivosPareados()
            getListDispositivosDisponiveis()
        }
    }

    private fun configurarRecyclerView() {
        binding.rvDispositivosBlue.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = DeviceListAdapter(context, mDeviceList)
        }
    }

    private fun getListDispositivosDisponiveis() {
        val btAdapter = BluetoothAdapter.getDefaultAdapter()
        val filter = IntentFilter()

        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

        MainActivity.context.registerReceiver(broadcastDiscoverBTDevices, filter)
        btAdapter.startDiscovery()
    }

    private fun updateAdapterDispositivosPareados() {
        binding.rvDispositivosBlue.adapter!!.notifyDataSetChanged()
    }

    private val broadcastDiscoverBTDevices: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) {
                Log.d(TAG, "Action discovery started")
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                Log.d(TAG, "Action discovery Finished")
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND == action) {
                Log.d(TAG, "Bluetooth encontrado")
                //bluetooth device found
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    mDeviceList.add(device)
                    updateAdapterDispositivosPareados()
                }
            }
        }
    }
    private val broadcastBondStateBT = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action) {
                val device = intent
                    .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                when (device!!.bondState) {
                    BluetoothDevice.BOND_BONDED -> {
                        Log.d(TAG, "BroadcastReceiver: BOND_BONDED")
                        mBTDevice = device
                    }
                    BluetoothDevice.BOND_BONDING -> {
                        Log.d(TAG, "BroadcastReceiver: BOND_BONDING")
                    }
                    BluetoothDevice.BOND_NONE -> {
                        Log.d(TAG, "BroadcastReceiver: BOND_DONE")
                    }
                }
            }
        }
    }

}