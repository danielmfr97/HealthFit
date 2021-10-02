package com.daniel.ramos.projetotcc.model.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Paciente : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var nome: String = ""
    var sexo: String = ""
    var dataNascimento: String = ""
    var created: Long = 0
}