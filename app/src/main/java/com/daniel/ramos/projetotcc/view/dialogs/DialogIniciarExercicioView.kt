package com.daniel.ramos.projetotcc.view.dialogs

import android.view.View
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.DialogIniciarExercicioBinding
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.adapters.PacientesArrayAdapter
import com.daniel.ramos.projetotcc.presenter.listeners.OnPacienteSelecionado
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.activity.MainActivity.Companion.context
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class DialogIniciarExercicioView {
    private val pacienteModel = ModelFactory.getPacienteModel
    private var pacienteSelecionado: Paciente? = null

    private var binding = DialogIniciarExercicioBinding.inflate(
        MainActivity.instance!!.layoutInflater
    )
    val view = binding.root

    init {
         val onPacienteSelecionado = object : OnPacienteSelecionado {
            override fun executar(paciente: Paciente) {
                pacienteSelecionado = paciente
                (binding.spPacientes.editText as MaterialAutoCompleteTextView).setText(paciente.nome)
            }
        }
        configurarSpinnerPaciente(onPacienteSelecionado)
    }



    private fun configurarSpinnerPaciente(onPacienteSelecionado: OnPacienteSelecionado) {
        val adapter = PacientesArrayAdapter(context, R.layout.list_pacientes, pacienteModel.getAllSortedByName(), onPacienteSelecionado)
        (binding.spPacientes.editText as MaterialAutoCompleteTextView).setAdapter(adapter)
    }

    fun getView(): View {
        return view
    }

    fun getPacienteSelecionado(): Paciente? {
        return pacienteSelecionado
    }

    fun isPacienteSelecionado(): Boolean {
        if (getPacienteSelecionado() == null) {
            binding.spPacientes.error = "Selecione um paciente"
            return false
        }
        return true
    }

}