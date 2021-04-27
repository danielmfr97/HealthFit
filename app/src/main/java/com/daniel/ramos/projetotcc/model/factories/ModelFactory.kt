package com.daniel.ramos.projetotcc.model.factories

import com.daniel.ramos.projetotcc.model.PacienteModel

object ModelFactory {
    private var pacienteModel: PacienteModel? = null

    val getPacienteModel: PacienteModel
        get() {
            if (pacienteModel == null) pacienteModel = PacienteModel()
            return pacienteModel as PacienteModel
        }
}