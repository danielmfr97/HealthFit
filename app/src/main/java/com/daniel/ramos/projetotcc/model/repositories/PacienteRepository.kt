package com.daniel.ramos.projetotcc.model.repositories

import com.daniel.ramos.projetotcc.model.entities.Paciente
import io.realm.RealmResults
import io.realm.Sort

class PacienteRepository: RealmRepository<Paciente>(Paciente::class.java) {
    fun getAllSortedByDate(): RealmResults<Paciente> {
        return realm.where(clazz).findAll().sort("created", Sort.ASCENDING)
    }

    fun getAllSortedByName(): RealmResults<Paciente> {
        return realm.where(clazz).findAll().sort("nome", Sort.ASCENDING)
    }


}