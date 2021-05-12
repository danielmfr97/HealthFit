package com.daniel.ramos.projetotcc.model.factories

import com.daniel.ramos.projetotcc.model.ExercicioModel
import com.daniel.ramos.projetotcc.model.PacienteModel
import com.daniel.ramos.projetotcc.model.ResultadoModel

object ModelFactory {
    private var pacienteModel: PacienteModel? = null
    private var exercicioModel: ExercicioModel? = null
    private var resultadoModel: ResultadoModel? = null

    val getPacienteModel: PacienteModel
        get() {
            if (pacienteModel == null) pacienteModel = PacienteModel()
            return pacienteModel as PacienteModel
        }

    val getExercicioModel: ExercicioModel
    get() {
        if (exercicioModel == null) exercicioModel = ExercicioModel()
        return exercicioModel as ExercicioModel
    }

    val getResultadoModel: ResultadoModel
    get() {
        if (resultadoModel == null) resultadoModel = ResultadoModel()
        return resultadoModel as ResultadoModel
    }
}