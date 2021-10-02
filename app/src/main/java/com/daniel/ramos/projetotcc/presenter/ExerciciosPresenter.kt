package com.daniel.ramos.projetotcc.presenter

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.model.entities.Exercicio
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.adapters.ExerciciosAdapter
import com.daniel.ramos.projetotcc.presenter.listeners.OnExercicioIniciado
import com.daniel.ramos.projetotcc.view.fragment.ExerciciosFragment

class ExerciciosPresenter(private val view: ExerciciosFragment) {
    private val exercicioModel = ModelFactory.getExercicioModel

    fun getAdapterExercicios(): ExerciciosAdapter {
        return ExerciciosAdapter(exercicioModel.getExercicioSortedByName(), true, onExercicioIniciado)
    }

    private val onExercicioIniciado = object : OnExercicioIniciado {
        override fun iniciar(
            exercicio: Exercicio,
            pacienteSelecionado: Paciente?
        ) {
            val bundle = bundleOf("exercicioId" to exercicio.id, "pacienteId" to pacienteSelecionado?.id)
            view.findNavController().navigate(R.id.action_exerciciosFragment_to_exercicioIniciadoFragment, bundle)
        }
    }
}