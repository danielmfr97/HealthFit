package com.daniel.ramos.projetotcc.view.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.FragmentDashboardBinding
import com.daniel.ramos.projetotcc.presenter.DashboardPresenter
import com.daniel.ramos.projetotcc.presenter.dialogs.DialogInicializarBluetooth
import com.daniel.ramos.projetotcc.presenter.listeners.OnBluetoothAtivado
import com.daniel.ramos.projetotcc.presenter.utils.PreferencesClass
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.effet.RippleEffect
import com.takusemba.spotlight.shape.RoundedRectangle
import android.content.res.TypedArray

import android.os.Build
import androidx.appcompat.widget.TintTypedArray.obtainStyledAttributes
import android.util.TypedValue
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.floatingactionbutton.FloatingActionButton


class DashboardFragment : Fragment(), View.OnClickListener {
    private val TAG = "DashboardFragment"
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: DashboardPresenter
    private lateinit var sharedPrefs: PreferencesClass


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        sharedPrefs = PreferencesClass(requireContext())
        inicializarPresenter()
        configurarCardviews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnPreDraw {
            if (sharedPrefs.getFirstRunDashboardSpotlight()) {
                switchClickableViewsOnFragment(false)
                initSpotlight()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initSpotlight() {
        val targets = ArrayList<Target>()
        val targetOneLayout = FrameLayout(requireContext())
        val targetOneOverlay = layoutInflater.inflate(R.layout.layout_target, targetOneLayout)
        val targetOne = Target.Builder()
            .setAnchor(requireView().findViewById<View>(R.id.cvConfigurarApp))
            .setShape(
                RoundedRectangle(
                    binding.cvConfigurarApp.height.toFloat(),
                    binding.cvConfigurarApp.width.toFloat(),
                    100f
                )
            )
            .setEffect(RippleEffect(100f, 200f, Color.argb(30, 124, 255, 90)))
            .setOverlay(targetOneOverlay)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {
                    targetOneOverlay.findViewById<TextView>(R.id.custom_title)
                        .setText(R.string.configurarAppTitleSpotlight)
                    targetOneOverlay.findViewById<TextView>(R.id.custom_text)
                        .setText(R.string.configurarAppDescriptionSpotlight)
                }

                override fun onEnded() {
                }
            })
            .build()

        val targetTwoLayout = FrameLayout(requireContext())
        val targetTwoOverlay = layoutInflater.inflate(R.layout.layout_target, targetTwoLayout)
        val targetTwo = Target.Builder()
            .setAnchor(requireView().findViewById<View>(R.id.cvPacientes))
            .setShape(
                RoundedRectangle(
                    binding.cvPacientes.height.toFloat(),
                    binding.cvPacientes.width.toFloat(),
                    100f
                )
            )
            .setEffect(RippleEffect(100f, 200f, Color.argb(30, 124, 255, 90)))
            .setOverlay(targetTwoOverlay)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {
                    targetTwoOverlay.findViewById<TextView>(R.id.custom_title)
                        .setText(R.string.pacientesAppTitleSpotlight)
                    targetTwoOverlay.findViewById<TextView>(R.id.custom_text)
                        .setText(R.string.pacientesAppDescriptionSpotlight)
                }

                override fun onEnded() {}
            })
            .build()

        val targetThreeLayout = FrameLayout(requireContext())
        val targetThreeOverlay = layoutInflater.inflate(R.layout.layout_target, targetThreeLayout)
        val targetThree = Target.Builder()
            .setAnchor(requireView().findViewById<View>(R.id.cvExercicios))
            .setShape(
                RoundedRectangle(
                    binding.cvExercicios.height.toFloat(),
                    binding.cvExercicios.width.toFloat(),
                    100f
                )
            )
            .setEffect(RippleEffect(100f, 200f, Color.argb(30, 124, 255, 90)))
            .setOverlay(targetThreeOverlay)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {
                    targetThreeOverlay.findViewById<TextView>(R.id.custom_title)
                        .setText(R.string.exerciciosAppTitleSpotlight)
                    targetThreeOverlay.findViewById<TextView>(R.id.custom_text)
                        .setText(R.string.exerciciosAppDescriptionSpotlight)
                }

                override fun onEnded() {}
            })
            .build()

        val targetFourLayout = FrameLayout(requireContext())
        val targetFourOverlay = layoutInflater.inflate(R.layout.layout_target, targetFourLayout)
        val targetFour = Target.Builder()
            .setAnchor(requireView().findViewById<View>(R.id.cvRelatorios))
            .setShape(
                RoundedRectangle(
                    binding.cvRelatorios.height.toFloat(),
                    binding.cvRelatorios.width.toFloat(),
                    100f
                )
            )
            .setEffect(RippleEffect(100f, 200f, Color.argb(30, 124, 255, 90)))
            .setOverlay(targetFourOverlay)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {
                    targetFourOverlay.findViewById<TextView>(R.id.custom_title).apply {
                        setText(R.string.relatoriosAppTitleSpotlight)
                        updateLayoutParams<ConstraintLayout.LayoutParams> {
                            topToBottom = binding.cvRelatorios.id
                        }
                    }
                    targetFourOverlay.findViewById<TextView>(R.id.custom_text).apply {
                        setText(R.string.relatoriosAppDescriptionSpotlight)
                        updateLayoutParams<ConstraintLayout.LayoutParams> {
                            topToBottom =
                                targetFourOverlay.findViewById<TextView>(R.id.custom_title).id
                        }
                    }

                }

                override fun onEnded() {
                    switchClickableViewsOnFragment(true)
                    sharedPrefs.setFirstRunDashboardSpotlight(false)
                }
            })
            .build()

        targets.add(targetOne)
        targets.add(targetTwo)
        targets.add(targetThree)
        targets.add(targetFour)
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
        targetFourOverlay.findViewById<FloatingActionButton>(R.id.next_target)
            .setOnClickListener { spotlight.next() }
        spotlight.start()
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

    private fun switchClickableViewsOnFragment(isClickable: Boolean) {
        val clickableCards = arrayListOf<View>(
            binding.cvRelatorios,
            binding.cvPacientes,
            binding.cvConfigurarApp,
            binding.cvExercicios
        )
        clickableCards.forEach {
            it.isClickable = isClickable
        }
    }

    override fun onClick(v: View) {
        when (v) {
            binding.cvRelatorios -> findNavController().navigate(R.id.action_dashboardFragment_to_relatoriosFragment)
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