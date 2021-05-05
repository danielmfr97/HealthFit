package com.daniel.ramos.projetotcc.presenter

import android.view.ContextMenu
import android.widget.ArrayAdapter
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.presenter.adapters.ColorArrayAdapter
import com.daniel.ramos.projetotcc.presenter.enums.Cores
import com.daniel.ramos.projetotcc.presenter.enums.TipoExercicio
import com.daniel.ramos.projetotcc.view.fragment.NovoExercicioFragment

class NovoExercicioPresenter(private val view: NovoExercicioFragment) {

    fun getAdapterTipoExercicio(): ArrayAdapter<String> {
        val items = listOf<String>(TipoExercicio.ALEATORIO.nome, TipoExercicio.SEQUENCIA.nome)
        val adapter = ArrayAdapter(view.requireContext(), R.layout.support_simple_spinner_dropdown_item, items)
        return adapter
    }

    fun getAdapterSelecionarCor(): ArrayAdapter<Cores> {
        val items = listOf(Cores.VERMELHO, Cores.AZUL, Cores.VERDE)
        val adapter = ColorArrayAdapter(view.requireContext(), R.layout.support_simple_spinner_dropdown_item, items)
        return adapter
    }

}