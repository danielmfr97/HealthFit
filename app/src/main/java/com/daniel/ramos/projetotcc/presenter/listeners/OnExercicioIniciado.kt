package com.daniel.ramos.projetotcc.presenter.listeners

import com.daniel.ramos.projetotcc.model.entities.Exercicio
import com.daniel.ramos.projetotcc.model.entities.Paciente

interface OnExercicioIniciado {
    fun iniciar(
        exercicio: Exercicio,
        pacienteSelecionado: Paciente?
    )
}