package com.daniel.ramos.projetotcc.presenter.listeners

import com.daniel.ramos.projetotcc.model.entities.Paciente

interface OnPacienteSelecionado {
    fun executar(paciente: Paciente);
}