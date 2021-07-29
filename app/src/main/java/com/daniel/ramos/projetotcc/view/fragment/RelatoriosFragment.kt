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
    private lateinit var mAdapter: ResultadosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRelatoriosBinding.inflate(layoutInflater, container, false)
        inicializarPresenter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configurarApagarFiltro()
        configurarSpinnerPaciente()
        configurarRecyclerView()
        configurarFabFiltro()
    }

    private fun inicializarPresenter() {
        presenter = RelatoriosPresenter(this)
    }

    private fun configurarApagarFiltro() {
        binding.btnFiltro.setOnClickListener { removerFiltros() }
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

    private fun configurarFabFiltro() {
        binding.fabFiltrarPesquisa.setOnClickListener {
            presenter.openDialogFiltrarPesquisa()
        }
    }

    fun setAdapterFilters(exercicioSelecionado: String?, dataInicio: Long?, dataFim: Long?) {
        mAdapter.filtrarDados(exercicioSelecionado, dataInicio, dataFim)
        binding.btnFiltro.visibility = View.VISIBLE
        showEmptyView()
    }

    fun setResultadosAdapter(resultadosAdapter: ResultadosAdapter) {
        mAdapter = resultadosAdapter
        binding.rvResultados.adapter = mAdapter
        showEmptyView()
    }

    fun setAutoCompleteText(nome: String) {
        (binding.spinnerPacientes.editText as MaterialAutoCompleteTextView).setText(nome)
    }

    fun removerFiltros() {
        mAdapter.filtrarDados(null, null, null)
        binding.btnFiltro.visibility = View.GONE
    }

    fun showEmptyView() {
        if (mAdapter.itemCount == 0)
            binding.emptyView.visibility = View.VISIBLE
        else
            binding.emptyView.visibility = View.GONE
    }
}