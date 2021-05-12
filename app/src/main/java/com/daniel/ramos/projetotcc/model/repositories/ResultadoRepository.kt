package com.daniel.ramos.projetotcc.model.repositories

import com.daniel.ramos.projetotcc.model.entities.Resultado
import io.realm.RealmResults

class ResultadoRepository: RealmRepository<Resultado>(Resultado::class.java)  {
    fun getResultadorPorPacienteId(id: String): RealmResults<Resultado> {
        return realm.where(clazz).equalTo("paciente_id", id).findAll()
    }
}