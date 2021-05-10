package com.daniel.ramos.projetotcc.model.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

// JSON sempre enviado, mesmo quando parar o exercicio.
open class Exercicio : RealmObject() {
    @PrimaryKey
    var id: String = ""

    // TEmpo que o exercicio irá duras
    var exercicioDuracao: Long? = null // milli seconds

    // True or False se terá timeout
    var timeOutSensor: Boolean? = null //

    // Tempo para o dispostiivo desligar a luz
    var timeout: Long? = null // ms, caso timeOutSensor false -> return 0

    // Tempo que o usuario conta erro, ou seja se demorar mt tempo com pé em cima so conta erro dps
    var delayContarErro: Long? = null // threshold para erro do usuario

    // Numero de dispositivos usados para atividade
    var sensoresUsados: Int? = null // Se enviar 4 reduza um para enviar o dado no json

    // A priori será sempre RANDOM
    var tipoExericicio: String? = null // Tipo de exercicio
}