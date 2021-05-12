package com.daniel.ramos.projetotcc.view.dialogs

import android.view.View
import com.daniel.ramos.projetotcc.databinding.DialogIniciarExercicioBinding
import com.daniel.ramos.projetotcc.view.activity.MainActivity

class DialogIniciarExercicioView {
    private var binding = DialogIniciarExercicioBinding.inflate(
        MainActivity.instance!!.layoutInflater
    )
    val view = binding.root

    init {

    }

    fun getView(): View {
        return view
    }

    
}