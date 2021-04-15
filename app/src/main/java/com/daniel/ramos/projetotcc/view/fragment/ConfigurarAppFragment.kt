package com.daniel.ramos.projetotcc.view.fragment

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniel.ramos.projetotcc.databinding.FragmentConfigurarAppBinding
import com.daniel.ramos.projetotcc.presenter.ConfigurarAppPresenter
import com.daniel.ramos.projetotcc.presenter.listeners.OnBluetoothAtivado
import com.daniel.ramos.projetotcc.view.activity.MainActivity

class ConfigurarAppFragment : Fragment() {
    private val TAG = "ConfigurarAppFragment"

    private var _binding: FragmentConfigurarAppBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ConfigurarAppPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfigurarAppBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //TODO: Se o bluetooth estiver desativado temos de informar para o user
    }

    private fun inicializarPresenter() {
        presenter = ConfigurarAppPresenter(this)
    }

    private val broadcastReceiverOnOffBT = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action!! == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> Log.d(TAG, "onReceive: STATE OFF")
                    BluetoothAdapter.STATE_TURNING_OFF -> Log.d(
                        TAG,
                        "mBroadcastReceiver1: STATE TURNING OFF"
                    )
                    BluetoothAdapter.STATE_ON -> Log.d(TAG, "mBroadcastReceiver1: STATE ON")
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
}