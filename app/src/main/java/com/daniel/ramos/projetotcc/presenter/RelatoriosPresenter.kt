package com.daniel.ramos.projetotcc.presenter

import android.util.Log
import android.widget.ArrayAdapter
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.model.entities.Resultado
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.adapters.PacientesArrayAdapter
import com.daniel.ramos.projetotcc.presenter.adapters.ResultadosAdapter
import com.daniel.ramos.projetotcc.presenter.listeners.OnPacienteSelecionado
import com.daniel.ramos.projetotcc.presenter.utils.RegexUtils
import com.daniel.ramos.projetotcc.view.fragment.RelatoriosFragment
import java.util.*

private const val TAG = "RelatoriosPresenter"
class RelatoriosPresenter(private val view: RelatoriosFragment) {
    private val pacienteModel = ModelFactory.getPacienteModel
    private val resultadoModel = ModelFactory.getResultadoModel

    init {
        popularResultados()
    }

    private fun popularResultados() {
        for (i in 0 until pacienteModel.all.size) {
            val paciente = pacienteModel.all[i]
            val resultado = Resultado()
            resultado.id = UUID.randomUUID().toString()
            resultado.created = Date()
            resultado.paciente_id = paciente!!.id
            resultado.exercicio_id = "b2331c99-410f-442b-b184-deef41226e3e"
            resultado.tempo_total = 2.5.toString()
            resultado.created = Date()
            resultadoModel.salvarResultado(resultado)
        }
    }

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
        return ResultadosAdapter(resultadoModel.procurarResultadosPorPacienteId(paciente.id), true)
    }
}