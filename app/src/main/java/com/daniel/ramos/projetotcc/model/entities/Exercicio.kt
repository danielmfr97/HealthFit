package com.daniel.ramos.projetotcc.model.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Exercicio : RealmObject() {
    @PrimaryKey
    var id: String = ""
    // Random, Sequence
    var tipoExericicio = ""
    // Numero de dispositivos usados para atividade
    var dispositivos: String = ""
    var cor: String = ""
    // Especifica se a luz desligará quando houver hit, timeout ou ambos
    var desligarLuz = ""
    // Tempo para o proximo dispositivo ligar a luz
    var delayLuzTempo: String = ""
    // TEmpo que o exercicio irá duras
    var exercicioDuracao: String = ""
    // Numero de vezes que o exercicio se repete
    var ciclosExercicio: String = ""
}