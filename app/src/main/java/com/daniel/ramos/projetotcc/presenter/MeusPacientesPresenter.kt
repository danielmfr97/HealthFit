package com.daniel.ramos.projetotcc.presenter

import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.adapters.PacientesAdapter
import com.daniel.ramos.projetotcc.presenter.dialogs.DialogAddPaciente
import com.daniel.ramos.projetotcc.presenter.listeners.OnPacienteAdicionado
import com.daniel.ramos.projetotcc.view.fragment.MeusPacientesFragment

class MeusPacientesPresenter(private val view: MeusPacientesFragment) {
    private val pacienteModel = ModelFactory.getPacienteModel

    fun abrirDialogAddPaciente() {
        DialogAddPaciente(onPacienteAdicionado).exibir()
    }

    fun getPacientesAdapter(): PacientesAdapter {
        return PacientesAdapter(pacienteModel.getAllSortedByDateCreated(), true)
    }

    private val onPacienteAdicionado = object : OnPacienteAdicionado {
        override fun novoPaciente(paciente: Paciente) {
            pacienteModel.salvarPaciente(paciente)
        }
    }
}