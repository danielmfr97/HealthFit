package com.daniel.ramos.projetotcc.presenter.listeners

interface OnFiltroSelecionado {
    fun executar(exercicioSelecionado: String, dataInicio: Long, dataFim: Long)
}