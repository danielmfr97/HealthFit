package com.daniel.ramos.projetotcc.model

import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.model.repositories.PacienteRepository
import com.daniel.ramos.projetotcc.model.repositories.RealmRepository
import java.util.*

class PacienteModel {
    private val pacienteRepository = PacienteRepository()

    val all get() = pacienteRepository.procurarTodos()

    fun salvarPaciente(pacienteDados: Paciente) {
        pacienteRepository.atualizarObject(object : RealmRepository.AtualizarListener {
            override fun atualizar() {
                val paciente = pacienteRepository.novo(UUID.randomUUID().toString())
                paciente.nome = pacienteDados.nome
                paciente.dataNascimento = pacienteDados.dataNascimento
                paciente.sexo = pacienteDados.sexo
            }
        })
    }
}