package com.daniel.ramos.projetotcc.presenter

import android.widget.ArrayAdapter
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.model.entities.Resultado
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.adapters.PacientesArrayAdapter
import com.daniel.ramos.projetotcc.presenter.adapters.ResultadosAdapter
import com.daniel.ramos.projetotcc.presenter.dialogs.DialogFiltrarPesquisa
import com.daniel.ramos.projetotcc.presenter.listeners.OnFiltroSelecionado
import com.daniel.ramos.projetotcc.presenter.listeners.OnPacienteSelecionado
import com.daniel.ramos.projetotcc.view.fragment.RelatoriosFragment
import java.util.*

private const val TAG = "RelatoriosPresenter"
class RelatoriosPresenter(private val view: RelatoriosFragment) {
    private val pacienteModel = ModelFactory.getPacienteModel
    private val resultadoModel = ModelFactory.getResultadoModel

//    init {
//        popularResultados()
//    }
////TODO: Remover popular
//    private fun popularResultados() {
//        for (i in 0 until pacienteModel.all.size) {
//            val paciente = pacienteModel.all[i]
//            val resultado = Resultado()
//            resultado.id = UUID.randomUUID().toString()
//            resultado.paciente_id = paciente!!.id
//            resultado.exercicio_id = UUID.randomUUID().toString()
//            resultado.exercicio_tipo_id = "8fd7b0fc-0c02-4fac-b357-f23aacebac11"
//            resultado.tempo_total = "2"
//            resultado.acertos = "10"
//            resultado.erros = "0"
//            resultado.created = 1626095800097L
//            resultadoModel.salvarResultado(resultado)
//        }
//    }

    private val onPacienteSelecionado = object : OnPacienteSelecionado {
        override fun executar(paciente: Paciente) {
            val adapter = getResultadosAdapter(paciente)
            view.setAutoCompleteText(paciente.nome)
            view.setResultadosAdapter(adapter)
        }
    }

    fun getAdapterSpinner(): ArrayAdapter<Paciente> {
        val items = pacienteModel.getAllSortedByName()
        val adapter = PacientesArrayAdapter(view.requireContext(), R.layout.list_pacientes, items, onPacienteSelecionado)
        return adapter
    }

    fun getResultadosAdapter(paciente: Paciente): ResultadosAdapter {
        return ResultadosAdapter(resultadoModel.procurarResultadosPorPacienteId(paciente.id), false)
    }

    fun openDialogFiltrarPesquisa() {
        DialogFiltrarPesquisa(onFiltroSelecionado).exibir()
    }

    private val onFiltroSelecionado = object : OnFiltroSelecionado {
        override fun executar(exercicioSelecionado: String?, dataInicio: Long?, dataFim: Long?) {
            view.setAdapterFilters(exercicioSelecionado, dataInicio, dataFim)
        }
    }
}