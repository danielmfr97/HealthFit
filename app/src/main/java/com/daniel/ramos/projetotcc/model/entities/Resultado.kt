package com.daniel.ramos.projetotcc.model.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Resultado : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var paciente_id: String = ""
    var exercicio_id: String = ""
    var acertos: String? = null
    var erros: String? = null
    var tempo_total: String = ""
    var created : Date? = null
}