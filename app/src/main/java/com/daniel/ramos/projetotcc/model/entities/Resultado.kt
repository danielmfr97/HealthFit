package com.daniel.ramos.projetotcc.model.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Resultado : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var paciente_id: String = ""
    var exercicio_id: String = ""
    var tipo_exercicio: String = ""
    var acertos: String = ""
    var erros: String = ""
    var velocidade_acao_media: String = ""
    var created : Date? = null
}