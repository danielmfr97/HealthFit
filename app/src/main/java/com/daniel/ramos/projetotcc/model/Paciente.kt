package com.daniel.ramos.projetotcc.model

import androidx.room.PrimaryKey
import io.realm.RealmObject

open class Paciente : RealmObject() {

    @PrimaryKey
    var id: String = ""
    var nome: String = ""
    var sexo: String = ""
    var dataNascimento: String = ""
}