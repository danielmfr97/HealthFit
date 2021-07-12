package com.daniel.ramos.projetotcc.presenter.enums

enum class TipoExercicio(val id: String, val nome: String, val descricao: String, val valorJSON: Int) {
    ALEATORIO("a144dcc7-0bca-43d7-91b3-995026f88f16","Aleatório", "O próximo dispositivo a ligar será selecionado aleatoriamente", 1),
    SEQUENCIA("8fd7b0fc-0c02-4fac-b357-f23aacebac11","Sequência", "Configure a lógica de cores dos dispositivos que seguirão uma sequência para ligar", 0)
}