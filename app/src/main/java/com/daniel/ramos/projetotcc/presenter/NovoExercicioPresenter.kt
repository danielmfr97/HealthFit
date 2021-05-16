package com.daniel.ramos.projetotcc.presenter

import android.widget.ArrayAdapter
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.model.entities.Exercicio
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.adapters.ColorArrayAdapter
import com.daniel.ramos.projetotcc.presenter.enums.Cores
import com.daniel.ramos.projetotcc.presenter.enums.TipoExercicio
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.fragment.NovoExercicioFragment

class NovoExercicioPresenter(private val view: NovoExercicioFragment) {

    private val exercicioModel = ModelFactory.getExercicioModel

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

    private fun getFitSpotsAsString(listaSensores: List<Boolean>) : String {
        val listIterator = listaSensores.iterator()
        val sensoresString = ""
        while (listIterator.hasNext()) {
            sensoresString.plus("${listIterator.next()}")
            if (listIterator.hasNext())
                sensoresString.plus(",")
        }
        return sensoresString
    }

    fun salvarExercicio() {
       val exercicioDado = Exercicio()
        exercicioDado.nomeExercicio = view.getNomeExercicio()
        exercicioDado.ciclosExercicio = view.getNumeroCiclos()
        exercicioDado.tipoExercicio = view.getTipoExercicio()
        exercicioDado.timeOutSensor = view.getTimeoutOption()
        exercicioDado.timeout = view.getTimeout()
        exercicioDado.sensoresUsados = getFitSpotsAsString(view.getFitSpots())
        exercicioModel.salvarExercicio(exercicioDado)
        MainActivity.openToastShort("Exercício salvo")
    }
}