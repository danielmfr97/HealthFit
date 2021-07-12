package com.daniel.ramos.projetotcc.presenter.listeners

import com.daniel.ramos.projetotcc.model.entities.Paciente

interface OnPacienteAdicionado {
    fun novoPaciente(paciente: Paciente)
}