package com.daniel.ramos.projetotcc.view.fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color.argb
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.FragmentConfigurarAppBinding
import com.daniel.ramos.projetotcc.presenter.ConfigurarAppPresenter
import com.daniel.ramos.projetotcc.presenter.adapters.DeviceListAdapter
import com.daniel.ramos.projetotcc.presenter.adapters.DeviceListPairedAdapter
import com.daniel.ramos.projetotcc.presenter.utils.PreferencesClass
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.activity.MainActivity.Companion.instance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.effet.RippleEffect
import com.takusemba.spotlight.shape.RoundedRectangle


//TODO: Adicionar progress bar para indicar a busca por dispositivos bluetooth
//TODO: Bloquear o onBack ao aparecer a tela de targets
class ConfigurarAppFragment : Fragment() {
    private val TAG = "ConfigurarAppFragment"

    private var _binding: FragmentConfigurarAppBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ConfigurarAppPresenter
    private var progressDialog: ProgressDialog? = null

    private lateinit var deviceListPairedAdapter: DeviceListPairedAdapter
    private lateinit var deviceListAdapter: DeviceListAdapter

    private lateinit var sharedPrefs: PreferencesClass

    var mDeviceList = arrayListOf<BluetoothDevice>()
    var mPairedDeviceList = arrayListOf<BluetoothDevice>()
    lateinit var mBTDevice: BluetoothDevice
    val btAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigurarAppBinding.inflate(layoutInflater, container, false)
        sharedPrefs = PreferencesClass(requireContext())
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
            if (sharedPrefs.getFirstRunSpotlight())
                initSpotlight()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initSpotlight() {
        val targets = ArrayList<Target>()

        val targetOneLayout = FrameLayout(requireContext())
        val targetOneOverlay = layoutInflater.inflate(R.layout.layout_target, targetOneLayout)
        val targetOne = Target.Builder()
            .setAnchor(requireView().findViewById<View>(R.id.buscarDispositivos))
            .setShape(
                RoundedRectangle(
                    binding.buscarDispositivos.height.toFloat(),
                    binding.buscarDispositivos.width.toFloat(),
                    100f
                )
            )
            .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
            .setOverlay(targetOneOverlay)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {
                    targetOneOverlay.findViewById<TextView>(R.id.custom_title)
                        .setText(R.string.targetOnetitle)
                    targetOneOverlay.findViewById<TextView>(R.id.custom_text)
                        .setText(R.string.targetOneText)
                }

                override fun onEnded() {
                }
            })
            .build()

        val targetTwoLayout = FrameLayout(requireContext())
        val targetTwoOverlay = layoutInflater.inflate(R.layout.layout_target, targetTwoLayout)
        val targetTwo = Target.Builder()
            .setAnchor(requireView().findViewById<View>(R.id.rvDispositivosBlue))
            .setShape(
                RoundedRectangle(
                    binding.rvDispositivosBlue.height.toFloat(),
                    binding.rvDispositivosBlue.width.toFloat(),
                    100f
                )
            )
            .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
            .setOverlay(targetTwoOverlay)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {
                    targetTwoOverlay.findViewById<TextView>(R.id.custom_text)
                        .setText(R.string.targetTwoText)
                }

                override fun onEnded() {}
            })
            .build()

        val targetThreeLayout = FrameLayout(requireContext())
        val targetThreeOverlay = layoutInflater.inflate(R.layout.layout_target, targetThreeLayout)
        val targetThree = Target.Builder()
            .setAnchor(requireView().findViewById<View>(R.id.rvDispositivosPareados))
            .setShape(
                RoundedRectangle(
                    binding.rvDispositivosPareados.height.toFloat(),
                    binding.rvDispositivosPareados.width.toFloat(),
                    100f
                )
            )
            .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
            .setOverlay(targetThreeOverlay)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {
                    targetThreeOverlay.findViewById<TextView>(R.id.custom_text)
                        .setText(R.string.targetThreeText)
                }

                override fun onEnded() {
                    // Evita que o spotlight rode novamente
                    sharedPrefs.setFirstRunSpotlight(false)
                }
            })
            .build()

        targets.add(targetOne)
        targets.add(targetTwo)
        targets.add(targetThree)
        val spotlight = Spotlight.Builder(requireActivity())
            .setTargets(targets)
            .setBackgroundColor(R.color.spotlightBackground)
            .setDuration(1000L)
            .setAnimation(DecelerateInterpolator(2f))
            .build()
        targetOneOverlay.findViewById<FloatingActionButton>(R.id.next_target)
            .setOnClickListener { spotlight.next() }
        targetTwoOverlay.findViewById<FloatingActionButton>(R.id.next_target)
            .setOnClickListener { spotlight.next() }
        targetThreeOverlay.findViewById<FloatingActionButton>(R.id.next_target)
            .setOnClickListener { spotlight.next() }
        spotlight.start()
    }

    private fun inicializarPresenter() {
        presenter = ConfigurarAppPresenter(this)
    }

    private fun configurarBotoes() {
        binding.buscarDispositivos.setOnClickListener {
            btAdapter.cancelDiscovery()
            getListDispositivosDisponiveis()
            atualizarDispositivosPareados()
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
                //TODO: Adicionar a validação de ser fitspot
//                if (device.name.startsWith("FitSpot"))
                    mPairedDeviceList.add(device)
            }
        }
        binding.rvDispositivosPareados.recycledViewPool.clear()
        binding.rvDispositivosPareados.adapter!!.notifyDataSetChanged()
    }

    fun atualizarListaPareados() {
        binding.rvDispositivosPareados.adapter!!.notifyDataSetChanged()
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

    fun enableDisableProgress(show: Boolean) {
        // Alteração deve ser executada na UI Thread
        Handler(Looper.getMainLooper()).post {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(instance)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setMessage("Buscando...")
                progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                progressDialog!!.isIndeterminate = true
            }
            if (show) {
                progressDialog!!.show()
            } else {
                progressDialog!!.dismiss()
            }
        }
    }
}