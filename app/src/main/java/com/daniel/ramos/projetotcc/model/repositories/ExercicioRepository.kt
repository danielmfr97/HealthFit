package com.daniel.ramos.projetotcc.model.repositories

import com.daniel.ramos.projetotcc.model.entities.Exercicio
import io.realm.RealmResults
import io.realm.Sort

class ExercicioRepository: RealmRepository<Exercicio>(Exercicio::class.java) {
    fun getExerciciosSortedByName(): RealmResults<Exercicio> {
        return realm.where(clazz).findAll().sort("nomeExercicio", Sort.ASCENDING)
    }


}