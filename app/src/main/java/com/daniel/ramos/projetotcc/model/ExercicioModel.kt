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
                    exercicio.tipoExercicioID = exercicioDado.tipoExercicioID
                    exercicio.nomeExercicio = exercicioDado.nomeExercicio
                    exercicio.ciclosExercicio = exercicioDado.ciclosExercicio
                    exercicio.tipoExercicio = exercicioDado.tipoExercicio
                    exercicio.timeOutSensor = exercicioDado.timeOutSensor
                    exercicio.tempoRandom = exercicioDado.tempoRandom
                    exercicio.timeout = exercicioDado.timeout
                    exercicio.sensor1 = exercicioDado.sensor1
                    exercicio.sensor2 = exercicioDado.sensor2
                    exercicio.sensor3 = exercicioDado.sensor3
                    exercicio.sensor4 = exercicioDado.sensor4
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun getExercicioPorId(exercicioId: String?): Exercicio? {
        return exercicioRepository.procurarPorId(exercicioId)
    }
}