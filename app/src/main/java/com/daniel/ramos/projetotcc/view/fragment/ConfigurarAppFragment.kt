package com.daniel.ramos.projetotcc.view.fragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color.argb
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.FragmentConfigurarAppBinding
import com.daniel.ramos.projetotcc.presenter.ConfigurarAppPresenter
import com.daniel.ramos.projetotcc.presenter.adapters.DeviceListAdapter
import com.daniel.ramos.projetotcc.presenter.adapters.DeviceListPairedAdapter
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.effet.RippleEffect
import com.takusemba.spotlight.shape.Circle


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
        atualizarDispositivosPareados()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.doOnPreDraw {
            initspotlightTa()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initspotlightTa() {
        val targets = ArrayList<Target>()

        val layout = layoutInflater.inflate(R.layout.layout_target, FrameLayout(requireContext()))
        val target = Target.Builder()
            .setAnchor(requireView().findViewById<View>(R.id.buscarDispositivos))
            .setShape(Circle(300f))
            .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
            .setOverlay(layout)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {
                    makeText(requireContext(), "first target is started", LENGTH_SHORT).show()
                }

                override fun onEnded() {
                    makeText(requireContext(), "first target is ended", LENGTH_SHORT).show()
                }
            })
            .build()

        targets.add(target)
        val spotlight = Spotlight.Builder(requireActivity())
            .setTargets(target)
            .setBackgroundColor(R.color.spotlightBackground)
            .setDuration(1000L)
            .setAnimation(DecelerateInterpolator(2f))
            .setOnSpotlightListener(object : OnSpotlightListener {
                override fun onStarted() {
                    Toast.makeText(requireContext(), "spotlight is started", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onEnded() {
                    Toast.makeText(requireContext(), "spotlight is ended", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .build()
        spotlight.start()
    }

    private fun inicializarPresenter() {
        presenter = ConfigurarAppPresenter(this)
    }

    private fun configurarBotoes() {
        binding.buscarDispositivos.setOnClickListener {
            btAdapter.cancelDiscovery()
            getListDispositivosDisponiveis()
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