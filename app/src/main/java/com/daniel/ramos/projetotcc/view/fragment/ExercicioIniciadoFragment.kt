package com.daniel.ramos.projetotcc.view.fragment

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.FragmentExercicioIniciadoBinding
import com.daniel.ramos.projetotcc.databinding.FragmentExerciciosBinding
import com.daniel.ramos.projetotcc.presenter.ExercicioIniciadoPresenter
import com.daniel.ramos.projetotcc.presenter.ExerciciosPresenter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "exercicioId"
private const val ARG_PARAM2 = "pacienteId"

class ExercicioIniciadoFragment : Fragment() {
    private var _binding: FragmentExercicioIniciadoBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ExercicioIniciadoPresenter

    private var contadorTempo: Long? = null

     var exercicioId: String? = null
     var pacienteId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            exercicioId = it.getString(ARG_PARAM1)
            pacienteId = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExercicioIniciadoBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        configurarBotaoParada()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.setBluetoothHandler()
        binding.llCronometro.visibility = View.VISIBLE
    }

    private fun inicializarPresenter() {
        presenter = ExercicioIniciadoPresenter(this)
    }

    private fun configurarBotaoParada() {
        binding.btnParar.setOnClickListener {
            binding.contadorCronometro.stop()
            presenter.pararExercicio()
        }
    }

    fun startCronometro() {
        binding.contadorCronometro.apply {
            setOnChronometerTickListener {
                contadorTempo = SystemClock.elapsedRealtime() - it.base
            }
            base = SystemClock.elapsedRealtime()
            start()
        }
    }
}