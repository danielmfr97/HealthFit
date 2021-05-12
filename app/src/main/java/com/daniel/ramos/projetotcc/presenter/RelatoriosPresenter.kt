package com.daniel.ramos.projetotcc.presenter

import android.widget.ArrayAdapter
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.adapters.PacientesArrayAdapter
import com.daniel.ramos.projetotcc.presenter.adapters.ResultadosAdapter
import com.daniel.ramos.projetotcc.presenter.listeners.OnPacienteSelecionado
import com.daniel.ramos.projetotcc.view.fragment.RelatoriosFragment

class RelatoriosPresenter(private val view: RelatoriosFragment) {
    private val pacienteModel = ModelFactory.getPacienteModel
    private val resultadoModel = ModelFactory.getResultadoModel

    private val onPacienteSelecionado = object : OnPacienteSelecionado {
        override fun executar(paciente: Paciente) {
            view.setAutoCompleteText(paciente.nome)
            view.setResultadosAdapter(getResultadosAdapter(paciente))
        }
    }
    fun getAdapterSpinner(): ArrayAdapter<Paciente> {
        val items = pacienteModel.all
        val adapter = PacientesArrayAdapter(view.requireContext(), R.layout.list_pacientes, items, onPacienteSelecionado)
        return adapter
    }

    fun getResultadosAdapter(paciente: Paciente): ResultadosAdapter {
        return ResultadosAdapter(resultadoModel.all, true)
    }
}