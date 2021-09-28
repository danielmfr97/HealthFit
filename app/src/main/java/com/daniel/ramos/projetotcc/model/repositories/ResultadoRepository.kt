package com.daniel.ramos.projetotcc.model.repositories

import com.daniel.ramos.projetotcc.model.entities.Resultado
import io.realm.RealmResults
import io.realm.Sort

class ResultadoRepository: RealmRepository<Resultado>(Resultado::class.java)  {
    fun getResultadorPorPacienteId(id: String): RealmResults<Resultado> {
        return realm.where(clazz).equalTo("paciente_id", id).findAll()
    }

    fun queryResultados(queryTipoExercicio: String?, queryDataInicio: Long?, queryDataFim: Long?): RealmResults<Resultado> {
        val resultados: RealmResults<Resultado>
        if (queryTipoExercicio.isNullOrEmpty() || queryDataInicio == null || queryDataFim == null)
            resultados = realm.where(clazz).findAll()
        else
            resultados = realm.where(clazz).contains("exercicio_tipo_id", queryTipoExercicio)
            .between("created", queryDataInicio, queryDataFim)
            .sort("created", Sort.DESCENDING)
            .findAll()
        return resultados
    }
}