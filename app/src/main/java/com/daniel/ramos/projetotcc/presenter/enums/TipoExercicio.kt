package com.daniel.ramos.projetotcc.presenter.enums

enum class TipoExercicio(val nome: String, val descricao: String) {
    ALEATORIO("Aleatório", "O próximo dispositivo a ligar será selecionado aleatoriamente"),
    SEQUENCIA("Sequência", "Configure a lógica de cores dos dispositivos que seguirão uma sequência para ligar")
}