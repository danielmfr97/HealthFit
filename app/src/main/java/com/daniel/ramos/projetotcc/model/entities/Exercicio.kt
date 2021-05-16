package com.daniel.ramos.projetotcc.model.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

// JSON sempre enviado, mesmo quando parar o exercicio.
open class Exercicio : RealmObject() {
    @PrimaryKey
    var id: String = ""

    var nomeExercicio: String = ""

    // Ao contar todos os sensores é concluido um ciclo
    var ciclosExercicio: Int? = null

    // True or False se terá timeout
    var timeOutSensor: Boolean? = null //

    // Tempo para o dispostiivo desligar a luz
    var timeout: Long? = null // ms, caso timeOutSensor false -> return 0

    // lista com os sensores ativos
    var sensoresUsados: String? = null

    // A priori será sempre RANDOM e Sequencial
    var tipoExercicio: String? = null // Tipo de exercicio
}