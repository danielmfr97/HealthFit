package com.daniel.ramos.projetotcc.model.factories

import com.daniel.ramos.projetotcc.model.ExercicioModel
import com.daniel.ramos.projetotcc.model.PacienteModel
import com.daniel.ramos.projetotcc.model.ResultadoModel
import com.daniel.ramos.projetotcc.presenter.BluetoothServiceA

object ModelFactory {
    private var pacienteModel: PacienteModel? = null
    private var exercicioModel: ExercicioModel? = null
    private var resultadoModel: ResultadoModel? = null
    private var bluetoothServiceA: BluetoothServiceA? = null

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

    val getBluetoothServiceA: BluetoothServiceA
        get() {
            if (ModelFactory.bluetoothServiceA == null)
                ModelFactory.bluetoothServiceA = BluetoothServiceA()
            return ModelFactory.bluetoothServiceA as BluetoothServiceA
        }
}