package com.daniel.ramos.projetotcc.view.fragment

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
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
import com.daniel.ramos.projetotcc.presenter.BluetoothServiceA
import com.daniel.ramos.projetotcc.presenter.ConfigurarAppPresenter
import com.daniel.ramos.projetotcc.presenter.adapters.DeviceListAdapter
import com.daniel.ramos.projetotcc.presenter.adapters.DeviceListPairedAdapter
import com.daniel.ramos.projetotcc.presenter.factory.ModelFactory
import com.daniel.ramos.projetotcc.view.activity.MainActivity


//TODO: Adicionar progress bar para indicar a busca por dispositivos bluetooth
class ConfigurarAppFragment : Fragment() {
    private val TAG = "ConfigurarAppFragment"


    private var _binding: FragmentConfigurarAppBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ConfigurarAppPresenter

    private lateinit var deviceListPairedAdapter: DeviceListPairedAdapter
    private lateinit var deviceListAdapter: DeviceListAdapter

    var mDeviceList = arrayListOf<BluetoothDevice>()
    var mPairedDeviceList = arrayListOf<BluetoothDevice>()
    lateinit var mBTDevice: BluetoothDevice
    val btAdapter = BluetoothAdapter.getDefaultAdapter()

    private val bluetoothService = ModelFactory.getBluetoothServiceA


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfigurarAppBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        configurarBotoes()
        configurarRecyclerView()
        inicializarBroadcasts()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //TODO: Criar lista de dispositivos disponiveis e dispositivos pareados
        atualizarDispositivosPareados()
    }

    private fun inicializarPresenter() {
        presenter = ConfigurarAppPresenter(this)
    }

    private fun configurarBotoes() {
        binding.buscarDispositivos.setOnClickListener {
            btAdapter.cancelDiscovery()
            getListDispositivosDisponiveis()
        }

        binding.testeEnvio.setOnClickListener {
            val bytes: ByteArray = "teste2".toByteArray()
            bluetoothService.write(bytes)
            Log.d(TAG, "Dados enviados $bytes ")
        }
    }

    private fun configurarRecyclerView() {
        deviceListAdapter = DeviceListAdapter(requireContext(), mDeviceList)
        deviceListPairedAdapter = DeviceListPairedAdapter(requireContext(), mPairedDeviceList)
        binding.rvDispositivosPareados.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = deviceListPairedAdapter
        }
        binding.rvDispositivosBlue.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = deviceListAdapter
        }
    }

    fun atualizarDispositivosPareados() {
        mPairedDeviceList.clear()
        val pairedDevice: Set<BluetoothDevice> =
            btAdapter.bondedDevices
        if (pairedDevice.isNotEmpty()) {
            for (device in pairedDevice) {
                mPairedDeviceList.add(device)
            }
        }
        binding.rvDispositivosPareados.recycledViewPool.clear()
        deviceListPairedAdapter.notifyDataSetChanged()
    }

    private fun inicializarBroadcasts() {
        val filterDiscover = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        MainActivity.context.registerReceiver(presenter.broadcastDiscoverBTDevices, filterDiscover)

        val filterBondChange = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        MainActivity.context.registerReceiver(presenter.broadcastBondStateBT, filterBondChange)
    }

    private fun getListDispositivosDisponiveis() {
        btAdapter.startDiscovery()
    }

    fun updateAdapterDispositivosEncontrados() {
        binding.rvDispositivosBlue.adapter!!.notifyDataSetChanged()
    }

    private fun habilitarDiscoverableBluetooth() {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        MainActivity.context.startActivity(intent)
        val intentFilter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        MainActivity.context.registerReceiver(presenter.broadcastDiscoverable, intentFilter)
    }


}