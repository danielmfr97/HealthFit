package com.daniel.ramos.projetotcc.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.ramos.projetotcc.databinding.FragmentRelatoriosBinding
import com.daniel.ramos.projetotcc.presenter.RelatoriosPresenter
import com.daniel.ramos.projetotcc.presenter.adapters.ResultadosAdapter
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class RelatoriosFragment : Fragment() {

    private var _binding: FragmentRelatoriosBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: RelatoriosPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRelatoriosBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configurarSpinnerPaciente()
        configurarRecyclerView()
    }

    private fun inicializarPresenter() {
        presenter = RelatoriosPresenter(this)
    }

    private fun configurarSpinnerPaciente() {
        (binding.spinnerPacientes.editText as MaterialAutoCompleteTextView).apply {
            setAdapter(presenter.getAdapterSpinner())
        }
    }

    private fun configurarRecyclerView() {
        binding.rvResultados.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(MainActivity.instance)
        }
    }

    fun setResultadosAdapter(resultadosAdapter: ResultadosAdapter) {
        binding.rvResultados.adapter = resultadosAdapter
    }

    fun setAutoCompleteText(nome: String) {
        (binding.spinnerPacientes.editText as MaterialAutoCompleteTextView).setText(nome)
    }

}