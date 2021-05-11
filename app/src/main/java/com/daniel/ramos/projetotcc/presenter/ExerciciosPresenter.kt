package com.daniel.ramos.projetotcc.presenter

import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.adapters.ExerciciosAdapter
import com.daniel.ramos.projetotcc.view.fragment.ExerciciosFragment

class ExerciciosPresenter(private val view: ExerciciosFragment) {
    private val exercicioModel = ModelFactory.getExercicioModel

    fun getAdapterExercicios(): ExerciciosAdapter {
        return ExerciciosAdapter(exercicioModel.all, true)
    }

}