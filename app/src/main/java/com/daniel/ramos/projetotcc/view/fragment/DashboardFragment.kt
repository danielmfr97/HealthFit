package com.daniel.ramos.projetotcc.view.fragment

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.FragmentDashboardBinding
import com.daniel.ramos.projetotcc.presenter.DashboardPresenter
import com.daniel.ramos.projetotcc.presenter.dialogs.DialogInicializarBluetooth
import com.daniel.ramos.projetotcc.presenter.listeners.OnBluetoothAtivado
import com.daniel.ramos.projetotcc.view.activity.MainActivity

class DashboardFragment : Fragment(), View.OnClickListener {
    private val TAG = "DashboardFragment"
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: DashboardPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        configurarCardviews()
        return binding.root
    }

    private fun inicializarPresenter() {
        presenter = DashboardPresenter(this)
    }

    private fun configurarCardviews() {
        binding.cvRelatorios.setOnClickListener(this)
        binding.cvPacientes.setOnClickListener(this)
        binding.cvExercicios.setOnClickListener(this)
        binding.cvConfigurarApp.setOnClickListener(this)
    }


    private fun verificarBluetoothAtivo(): Boolean {
        var isBluetoothAtivado = false
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            //TODO: Dispositivo não suporta bluetooth
        } else if (!bluetoothAdapter.isEnabled) {
            //TODO: Bluetooth não esta ativado
            //TODO: Abrir dialog para ativar bluetooth
        } else {
            isBluetoothAtivado = true
        }
        return isBluetoothAtivado
    }

    private fun exibirDialogInializarBluetooth() {
        val builder = AlertDialog.Builder(context)
            .setTitle("O bluetooth está desativado")
            .setMessage(getString(R.string.avisoBluetoothDesabilitado))
            .setPositiveButton("CONFIRMAR") { dialog, which ->
                Log.d(TAG, "Ativando bluetooth")
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivity(enableBluetoothIntent)
                val bluetoothFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
                MainActivity.context.registerReceiver(broadcastReceiverOnOffBT, bluetoothFilter)
                findNavController().navigate(R.id.action_dashboardFragment_to_configurarAppFragment)
            }
            .setNegativeButton("CANCELAR") { _, _ -> }
        builder.create().show()
    }

    private val broadcastReceiverOnOffBT = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action!! == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    BluetoothAdapter.STATE_OFF -> Log.d(TAG, "onReceive: STATE OFF")
                    BluetoothAdapter.STATE_TURNING_OFF -> Log.d(
                        TAG,
                        "mBroadcastReceiver1: STATE TURNING OFF"
                    )
                    BluetoothAdapter.STATE_ON -> {
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON")
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> Log.d(
                        TAG,
                        "mBroadcastReceiver1: STATE TURNING ON"
                    )
                }
            }
        }
    }

    private val onBluetoothAtivado = object : OnBluetoothAtivado {
        override fun ativarBroadcastReceiver(bluetoothFilter: IntentFilter) {
            MainActivity.context.registerReceiver(broadcastReceiverOnOffBT, bluetoothFilter)
        }
    }

    override fun onClick(v: View) {
        when (v) {
//            binding.cvRelatorios -> findNavController().navigate(R.id.action_dashboardFragment_to_relatoriosFragment)
            binding.cvPacientes -> findNavController().navigate(R.id.action_dashboardFragment_to_meusPacientesFragment)
            binding.cvExercicios -> findNavController().navigate(R.id.action_dashboardFragment_to_exerciciosFragment)
            binding.cvConfigurarApp -> {
                if (!verificarBluetoothAtivo())
                    exibirDialogInializarBluetooth()
                else
                    findNavController().navigate(R.id.action_dashboardFragment_to_configurarAppFragment)
            }
        }
    }
}