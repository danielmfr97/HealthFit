package com.daniel.ramos.projetotcc.model

import com.daniel.ramos.projetotcc.model.entities.Exercicio
import com.daniel.ramos.projetotcc.model.repositories.ExercicioRepository
import com.daniel.ramos.projetotcc.model.repositories.RealmRepository
import java.lang.Exception
import java.util.*

class ExercicioModel {
    private val exercicioRepository = ExercicioRepository()

    val all get() = exercicioRepository.procurarTodos()

    fun salvarExercicio(exercicioDado: Exercicio) {
        exercicioRepository.atualizarObject(object : RealmRepository.AtualizarListener {
            override fun atualizar() {
                try {
                    val exercicio = exercicioRepository.novo(UUID.randomUUID().toString())
                    exercicio.exercicioDuracao =  exercicioDado.exercicioDuracao
                    exercicio.timeOutSensor =  exercicioDado.timeOutSensor
                    exercicio.timeout =  exercicioDado.timeout
                    exercicio.delayContarErro =  exercicioDado.delayContarErro
                    exercicio.sensoresUsados =  exercicioDado.sensoresUsados
                    exercicio.tipoExericicio =  exercicioDado.tipoExericicio
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }
}