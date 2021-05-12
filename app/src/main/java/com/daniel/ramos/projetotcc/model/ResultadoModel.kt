package com.daniel.ramos.projetotcc.model

import com.daniel.ramos.projetotcc.model.entities.Resultado
import com.daniel.ramos.projetotcc.model.repositories.RealmRepository
import com.daniel.ramos.projetotcc.model.repositories.ResultadoRepository
import io.realm.RealmResults
import java.util.*

class ResultadoModel {
    private val resultadoRepository = ResultadoRepository()

    val all get() = resultadoRepository.procurarTodos()

    fun salvarResultado(resultadoDado: Resultado) {
        resultadoRepository.atualizarObject(object : RealmRepository.AtualizarListener {
            override fun atualizar() {
                val resultado = resultadoRepository.novo(UUID.randomUUID().toString())
                resultado.tipo_exercicio = resultadoDado.tipo_exercicio
                resultado.paciente_id = resultadoDado.paciente_id
                resultado.exercicio_id = resultadoDado.exercicio_id
                resultado.acertos = resultadoDado.acertos
                resultado.erros = resultadoDado.erros
                resultado.velocidade_acao_media = resultadoDado.velocidade_acao_media
                resultado.created = resultadoDado.created
            }
        })
    }

    fun procurarResultadosPorPacienteId(id: String): RealmResults<Resultado> {
        return resultadoRepository.getResultadorPorPacienteId(id)
    }
}