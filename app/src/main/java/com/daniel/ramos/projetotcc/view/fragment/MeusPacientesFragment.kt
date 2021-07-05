package com.daniel.ramos.projetotcc.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.ramos.projetotcc.databinding.FragmentMeusPacientesBinding
import com.daniel.ramos.projetotcc.presenter.MeusPacientesPresenter
import com.daniel.ramos.projetotcc.view.activity.MainActivity

class MeusPacientesFragment : Fragment() {
    private var _binding: FragmentMeusPacientesBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: MeusPacientesPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMeusPacientesBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        configurarRecyclerView()
        configurarFab()
        return binding.root
    }

    private fun inicializarPresenter() {
        presenter = MeusPacientesPresenter(this)
    }

    private fun configurarRecyclerView() {
        binding.rvPacientes.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(MainActivity.instance)
            adapter = presenter.getPacientesAdapter()
        }
    }

    private fun configurarFab() {
        binding.fabAddPaciente.setOnClickListener {
            presenter.abrirDialogAddPaciente()
        }
    }

}