package com.daniel.ramos.projetotcc.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.FragmentExerciciosBinding
import com.daniel.ramos.projetotcc.databinding.FragmentRelatoriosBinding
import com.daniel.ramos.projetotcc.presenter.ExerciciosPresenter
import com.daniel.ramos.projetotcc.presenter.RelatoriosPresenter
import com.daniel.ramos.projetotcc.view.activity.MainActivity

class ExerciciosFragment : Fragment() {
    private var _binding: FragmentExerciciosBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ExerciciosPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciciosBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        configurarRecyclerView()
        configurarFabAddExercicio()
        return binding.root
    }

    private fun inicializarPresenter() {
        presenter = ExerciciosPresenter(this)
    }

    private fun configurarRecyclerView() {
        binding.rvExercicios.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(MainActivity.instance)
            //TODO: Configurar adapter
//            adapter = presenter.getAdapterExercicios()
        }
    }

    private fun configurarFabAddExercicio() {
        binding.fabAddExercicio.setOnClickListener {
            findNavController().navigate(R.id.action_exerciciosFragment_to_novoExercicioFragment)
        }
    }

}